package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.networking.*;
import sweng_plus.framework.boardgame.nodes_board.Dice;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;

import java.util.*;
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
    public Map<LudoFigure, List<LudoNode>> movableFigures;
    
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
            for(LudoClient client : Ludo.instance().getNetworking().getHostManager().getAllClients())
            {
                Ludo.instance().getNetworking().getHostManager()
                        .sendMessageToClient(client,
                                new StartGameMessage(client.getTeamIndex(), teams.length, currentTeamIndex));
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
            Ludo.instance().getNetworking().getHostManager().sendMessageToAllClients(
                    new RolledMessage(latestRoll));
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
            Ludo.instance().getNetworking().getHostManager().sendMessageToAllClients(
                    new FigureSelectedMessage(selectedFigure));
        }
    }
    
    public void endPhaseSelectFigure(int figure)
    {
        System.out.println("                Logic: endPhaseSelectFigure");
        
        if(isServer && isGameWon(currentTeamIndex))
        {
            Ludo.instance().getNetworking().getHostManager().sendMessageToAllClients(
                    new WinMessage(currentTeamIndex));
            return;
        }
        
        // maximum numbers of consecutive rolls reached (standard MIN_CONSECUTIVE_ROLLS, if no movable figures - MAX_CONSECUTIVE_ROLLS rolls
        if(latestRoll != 6 && numConsecutiveRolls >= maxCurrentConsecutiveRolls())
        {
            nextTeam();
            numConsecutiveRolls = 0;
        }
    }
    
    public LudoFigure getFigureForIndex(int selectedFigure)
    {
        return movableFigures.keySet().stream().filter(fig -> fig.getIndex() == selectedFigure).findFirst().orElse(null);
    }
    
    public LudoFigure startPhaseMoveFigure(int selectedFigure)
    {
        System.out.println("                Logic: startPhaseMoveFigure");
        
        if(selectedFigure >= 0)
        {
            LudoFigure figure = getFigureForIndex(selectedFigure);
            if(figure != null)
            {
                figure.getCurrentNode().removeFigure(figure);
                figure.setCurrentNode(null);
            }
            return figure;
        }
        else
        {
            return null;
        }
    }
    
    public LudoNode getTargetNode(LudoFigure figure)
    {
        System.out.println("                Logic: getTargetNode");
        
        if(figure != null)
        {
            return movableFigures.get(figure).get(0);
        }
        else
        {
            return null;
        }
        
    }
    
    public Optional<LudoFigure> endPhaseMoveFigure(LudoFigure figure, LudoNode target)
    {
        Optional<LudoFigure> prevFigure = Optional.empty();
        System.out.println("                Logic: endPhaseMoveFigure");
        
        if(!target.getFigures().isEmpty())
        {
            prevFigure = Optional.ofNullable(target.getFigures().get(0));
        }
        
        ludoBoard.moveFigure(figure, target);
        
        return prevFigure;
    }
    
    public void moveFigureToOutside(LudoFigure figure)
    {
        
        System.out.println("                Logic: moveFigureToOutside");
        
        LudoNode outsideNode = ludoBoard.getFreeOutsideNode(figure);
        figure.getCurrentNode().removeFigure(figure);
        ludoBoard.moveFigure(figure, outsideNode);
        
    }
    
    public int roll()
    {
        return latestRoll = dice.roll();
    }
    
    public void setLatestRoll(int latestRoll)
    {
        this.latestRoll = latestRoll;
    }
    
    public void moveFigure(LudoFigure figure, LudoNode selectedNode)
    {
        if(selectedNode.isOccupied())
        {
            ludoBoard.moveFigureToOutside(selectedNode.getFigures().get(0));
        }
        ludoBoard.moveFigure(figure, selectedNode);
    }
    
    public int maxCurrentConsecutiveRolls()
    {
        TeamColor currentTeam = teams[currentTeamIndex];
        
        // Could move on previous turn
        if(movableFigures.size() > 0)
        {
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
                .filter((figure) -> (figure.getCurrentNode()).getNodeType() != LudoNodeType.OUTSIDE)
                .toList();
        
        // Check for figures remaining on the board
        for(LudoFigure figure : remainingTeamFigures)
        {
            // At least one figure out on the board
            if((figure.getCurrentNode()).getNodeType() != LudoNodeType.HOME)
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
    
    private Map<LudoFigure, List<LudoNode>> getMovableFigures(int roll)
    {
        Map<LudoFigure, List<LudoNode>> movableFigures = new HashMap<>(4);
        
        if(mustMoveFromStart(teams[currentTeamIndex]))
        {
            LudoFigure startFigure = ludoBoard.getStartNode(currentTeamIndex).getFigures().get(0);
            
            movableFigures.put(startFigure, ludoBoard.getForwardNodes(startFigure, roll, createMovablePredicate(startFigure)));
        }
        else if(latestRoll == 6 && !ludoBoard.isOutsideEmpty(teams[currentTeamIndex]))
        {
            for(LudoNode outsideNode : ludoBoard.getOutsideNodes(currentTeamIndex))
            {
                
                if(outsideNode.isOccupied())
                {
                    movableFigures.put(outsideNode.getFigures().get(0), List.of(ludoBoard.getStartNode(currentTeamIndex)));
                }
            }
        }
        else
        {
            for(LudoFigure teamFigure : ludoBoard.getTeamFigures(teams[currentTeamIndex]))
            {
                List<LudoNode> forwardNodes = ludoBoard.getForwardNodes(teamFigure, roll, createMovablePredicate(teamFigure));
                if(forwardNodes.size() > 0)
                {
                    movableFigures.put(teamFigure, forwardNodes);
                }
                // not needed until customs rules are implemented
                else if(latestRoll == 6 && (teamFigure.getCurrentNode()).getNodeType() == LudoNodeType.OUTSIDE)
                {
                    movableFigures.put(teamFigure, List.of(ludoBoard.getStartNode(currentTeamIndex)));
                }
            }
        }
        return movableFigures;
    }
    
    public boolean isGameWon(int team)
    {
        if(!gameWon)
        {
            return gameWon = ludoBoard.isHomeFull(teams[currentTeamIndex]);
        }
        return true;
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
    
    private static Predicate<LudoNode> createMovablePredicate(LudoFigure figure)
    {
        return (node -> {
            if(node.getTeam().equals(figure.getTeam()))
            {
                if(node.getNodeType() == LudoNodeType.HOME)
                {
                    return !node.isOccupied();
                }
                return node.getNodeType() != LudoNodeType.START;
            }
            return true;
        });
    }
}
