package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.networking.FigureSelectedMessage;
import sweng_plus.boardgames.ludo.gamelogic.networking.LudoClient;
import sweng_plus.boardgames.ludo.gamelogic.networking.RolledMessage;
import sweng_plus.boardgames.ludo.gamelogic.networking.StartGameMessage;
import sweng_plus.framework.boardgame.nodes_board.Dice;
import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class LudoGameLogic
{
    private static final int MAX_CONSECUTIVE_ROLLS = 3;
    private static final int MIN_CONSECUTIVE_ROLLS = 1;
    
    public TeamColor[] teams;
    public LudoBoard ludoBoard;
    public Dice<Integer> dice;
    public final boolean isServer;
    
    public int currentTeamIndex;
    public LudoTurnPhase currentTurnPhase;
    
    public int latestRoll;
    public Map<LudoFigure, List<INode>> movableFigures;
    
    public boolean gameWon;
    
    public int numConsecutiveRolls;
    
    public LudoGameLogic(TeamColor[] teams, boolean isServer)
    {
        this.teams = teams;
        ludoBoard = new LudoBoard(teams);
        dice = Dice.D6;
        
        this.isServer = isServer;
        
        currentTeamIndex = 0;
        currentTurnPhase = LudoTurnPhase.ROLL;
        
        gameWon = false;
        
        numConsecutiveRolls = 0;
    }
    
    public LudoBoard getLudoBoard()
    {
        return ludoBoard;
    }
    
    public void startGame()
    {
        //currentTeamIndex = new Random().nextInt(teams.length);
        currentTeamIndex = 0; //TODO
        
        if(isServer)
        {
            try
            {
                for(LudoClient client : Ludo.instance().getHostManager().getAllClients())
                {
                    Ludo.instance().getHostManager()
                            .sendMessageToClient(client,
                                    new StartGameMessage(client.getTeamIndex(), teams.length, currentTeamIndex));
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void startPhaseRoll() // called by Host on start and by previous
    {
        System.out.println("                Logic: startPhaseRoll");
        currentTurnPhase = LudoTurnPhase.ROLL;
        numConsecutiveRolls++;
    }
    
    public void tellClientsRoll()
    {
        // wird vom client aufgerufen
        // würfeln
        roll();
        
        if(isServer)
        {
            try
            {
                Ludo.instance().getHostManager().sendMessageToAllClients(
                        new RolledMessage(latestRoll));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void startPhaseSelectFigure() // called by previous
    {
        System.out.println("                Logic: startPhaseSelectFigure");
        currentTurnPhase = LudoTurnPhase.SELECT_FIGURE;
        movableFigures = getMovableFigures(latestRoll);
        
        // send clients diceResult result
    }
    
    public void tellClientsFigureSelected(int selectedFigure)
    {
        if(isServer)
        {
            try
            {
                Ludo.instance().getHostManager().sendMessageToAllClients(
                        new FigureSelectedMessage(selectedFigure));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void endPhaseSelectFigure(int selectedFigure)
    {
        System.out.println("                Logic: endPhaseSelectFigure");
        
        if(selectedFigure >= 0)
        {
            movableFigures.keySet().stream().filter(figure -> figure.getIndex() == selectedFigure).findFirst().ifPresent((figure) ->
                    moveFigure(figure, (LudoNode) movableFigures.get(figure).get(0)));
        }
        
        // maximum numbers of consecutive rolls reached (standard MIN_CONSECUTIVE_ROLLS, if no movable figures - MAX_CONSECUTIVE_ROLLS rolls
        if(latestRoll != 6 && numConsecutiveRolls >= maxCurrentConsecutiveRolls())
        {
            nextTeam();
            numConsecutiveRolls = 0;
        }
    }
    
    public int roll()
    {
        return latestRoll = dice.roll();
    }
    
    public void setLatestRoll(int latestRoll)
    {
        this.latestRoll = latestRoll;
    }
    
    public void moveFigure(NodeFigure figure, LudoNode selectedNode)
    {
        if(selectedNode.isOccupied())
        {
            ludoBoard.moveFigureToOutside((LudoFigure) selectedNode.getNodeFigures().get(0));
        }
        ludoBoard.moveFigure(figure, selectedNode);
    }
    
    public int maxCurrentConsecutiveRolls()
    {
        TeamColor currentTeam = teams[currentTeamIndex];
        
        // Could move on previous turn
        if(movableFigures.size() > 0) {
            return MIN_CONSECUTIVE_ROLLS;
        }
        
        // All Figures are outside
        if(ludoBoard.isHomeFull(currentTeam))
        {
            return MAX_CONSECUTIVE_ROLLS;
        }
        else if(ludoBoard.isOutsideFull(currentTeam))
        {
            return MAX_CONSECUTIVE_ROLLS;
        }
        
        // All Figures don't further impact the diceResult count
        List<LudoFigure> remainingTeamFigures = Arrays.stream(ludoBoard.getTeamFigures(currentTeam))
                .filter((figure) -> ((LudoNode) figure.getCurrentNode()).getNodeType() != LudoNodeType.OUTSIDE)
                .toList();
        
        // Check for figures remaining on the board
        for(LudoFigure figure : remainingTeamFigures)
        {
            // At least one figure out on the board
            if(((LudoNode) figure.getCurrentNode()).getNodeType() != LudoNodeType.HOME)
            {
                return MIN_CONSECUTIVE_ROLLS;
            }
        }
        
        // Check if all figures in the house can't move anymore
        List<LudoNode> homeNodes = ludoBoard.getHomeNodes(currentTeamIndex);
        
        if(teams.length != 2)
        {
            for(int i = (homeNodes.size() - remainingTeamFigures.size()); i < homeNodes.size(); i++)
            {
                // At least one figure in the house could move - not on last reachable node
                if(!homeNodes.get(i).isOccupied())
                {
                    return MIN_CONSECUTIVE_ROLLS;
                }
            }
            return MAX_CONSECUTIVE_ROLLS;
        }
        else
        {
            switch(remainingTeamFigures.size())
            {
                // Only one end-house node occupied
                case 1 -> {
                    if(homeNodes.get(1).isOccupied() || homeNodes.get(3).isOccupied())
                    {
                        return MAX_CONSECUTIVE_ROLLS;
                    }
                }
                // either both end-house nodes occupied or two consecutive nodes
                case 2 -> {
                    if(homeNodes.get(1).isOccupied())
                    {
                        if(homeNodes.get(3).isOccupied() || homeNodes.get(0).isOccupied())
                        {
                            return MAX_CONSECUTIVE_ROLLS;
                        }
                    }
                    else if(homeNodes.get(3).isOccupied())
                    {
                        if(homeNodes.get(0).isOccupied() || homeNodes.get(4).isOccupied())
                        {
                            return MAX_CONSECUTIVE_ROLLS;
                        }
                    }
                }
                // both end-house nodes and one of the beginning-house nodes occupied
                case 3 -> {
                    if(homeNodes.get(1).isOccupied() && homeNodes.get(3).isOccupied() && (homeNodes.get(0).isOccupied() || homeNodes.get(2).isOccupied()))
                    {
                        return MAX_CONSECUTIVE_ROLLS;
                    }
                }
                // all home nodes full
                case 4 -> {
                    if(ludoBoard.isHomeFull(teams[currentTeamIndex]))
                    {
                        return MAX_CONSECUTIVE_ROLLS;
                    }
                }
                default -> throw new IllegalStateException("Non implemented figure count: " + remainingTeamFigures.size());
            }
            return MIN_CONSECUTIVE_ROLLS;
        }
    }
    
    private Map<LudoFigure, List<INode>> getMovableFigures(int roll)
    {
        Map<LudoFigure, List<INode>> movableFigures = new HashMap<>(4);
        
        if(mustMoveFromStart(teams[currentTeamIndex]))
        {
            LudoFigure startFigure = (LudoFigure) ludoBoard.getStartNode(currentTeamIndex).getNodeFigures().get(0);
            
            movableFigures.put(startFigure, ludoBoard.getForwardNodes(startFigure, roll, createMovablePredicate(startFigure)));
        }
        else if (latestRoll == 6 && !ludoBoard.isOutsideEmpty(teams[currentTeamIndex])) {
            for(LudoNode outsideNode : ludoBoard.getOutsideNodes(currentTeamIndex))
            {
                
                if(outsideNode.isOccupied()) {
                    movableFigures.put((LudoFigure) outsideNode.getNodeFigures().get(0), List.of(ludoBoard.getStartNode(currentTeamIndex)));
                }
            }
        }
        else
        {
            for(LudoFigure teamFigure : ludoBoard.getTeamFigures(teams[currentTeamIndex]))
            {
                List<INode> forwardNodes = ludoBoard.getForwardNodes(teamFigure, roll, createMovablePredicate(teamFigure));
                if(forwardNodes.size() > 0)
                {
                    movableFigures.put(teamFigure, forwardNodes);
                }
                // not needed until customs rules are implemented
                else if(latestRoll == 6 && ((LudoNode) teamFigure.getCurrentNode()).getNodeType() == LudoNodeType.OUTSIDE)
                {
                    movableFigures.put(teamFigure, List.of(ludoBoard.getStartNode(currentTeamIndex)));
                }
            }
        }
        return movableFigures;
    }
    
    private void nextTeam()
    {
        currentTeamIndex = (currentTeamIndex + 1) % teams.length;
    }
    
    public void setTurnTeam(int team)
    {
        currentTeamIndex = team;
    }
    
    private boolean mustMoveFromStart(TeamColor teamColor)
    {
        return ludoBoard.isOwnStartOccupied(teamColor) && !ludoBoard.isOutsideEmpty(teamColor);
    }
    
    private static Predicate<INode> createMovablePredicate(LudoFigure figure)
    {
        return (node -> {
            if(!(node instanceof LudoNode ludoNode))
            {
                return true;
            }
            if(ludoNode.getColor().equals(figure.getColor()))
            {
                if(ludoNode.getNodeType() == LudoNodeType.HOME)
                {
                    return !ludoNode.isOccupied();
                }
                return ludoNode.getNodeType() != LudoNodeType.START;
            }
            return true;
        });
    }
}
