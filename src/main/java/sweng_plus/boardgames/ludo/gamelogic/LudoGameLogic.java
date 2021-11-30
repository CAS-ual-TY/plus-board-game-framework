package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.networking.NewTurnMessage;
import sweng_plus.boardgames.ludo.gamelogic.networking.RolledMessage;
import sweng_plus.boardgames.ludo.gamelogic.networking.StartGameMessage;
import sweng_plus.framework.boardgame.nodes_board.Dice;
import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class LudoGameLogic
{
    private static final int MAX_CONSECUTIVE_ROLLS = 3;
    private static final int MIN_CONSECUTIVE_ROLLS = 1;
    
    private TeamColor[] teams;
    private LudoBoard ludoBoard;
    private Dice<Integer> dice;
    private final boolean isServer;
    
    private int currentTeamIndex;
    private LudoTurnPhase currentTurnPhase;
    
    private int latestRoll;
    
    private boolean gameWon;
    
    private int numConsecutiveRolls;
    
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
        currentTeamIndex = new Random().nextInt(teams.length);
        if(isServer)
        {
            try
            {
                Ludo.instance().getHostManager().sendMessageToAllClients(new StartGameMessage());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        startPhaseRoll();
    }
    
    public void startPhaseRoll()
    {
        currentTurnPhase = LudoTurnPhase.ROLL;
        
        numConsecutiveRolls = 0;
        
        if(isServer)
        {
            try
            {
                Ludo.instance().getHostManager().sendMessageToAllClients(new NewTurnMessage(currentTeamIndex));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void endPhaseRoll()
    {
        numConsecutiveRolls++;
        if(isServer)
        {
            // wird vom client aufgerufen
            // würfeln
            roll();
            
            // TODO next phase
            startPhaseSelectFigure();
        }
        
    }
    
    public void startPhaseSelectFigure()
    {
        
        // TODO auszuwählende Figuren bestimmen (bewegbar mit roll-ergebnis)
        
        currentTurnPhase = LudoTurnPhase.SELECT_FIGURE;
        
        
        // TODO send clients selected Figure and roll result
        if(isServer)
        {
            Map<NodeFigure, List<INode>> movableFigures = getMovableFigures(latestRoll);
            
            // send clients roll result
            try
            {
                Ludo.instance().getHostManager().sendMessageToAllClients(new RolledMessage(latestRoll));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            
            // TODO send clients selectable figures
            // Ludo.instance().getHostManager().sendMessageToAllClients(new );
            
            
        }
        
        // TODO clients do prediction
    }
    
    public void endPhaseSelectFigure(int figureIndex)
    {
        
        // TODO next phase

        /*
        TODO max consecutive rolls
            no figure on field - max 3
            else 1 except 6 rolled
         */
        
        // maximum numbers of consecutive rolls reached (standard MIN_CONSECUTIVE_ROLLS, if no movable figures - MAX_CONSECUTIVE_ROLLS rolls
        if(!((latestRoll == 6) || (numConsecutiveRolls < maxCurrentConsecutiveRolls())))
        {
            nextTeam();
        }
        
        startPhaseRoll();
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
        ludoBoard.moveFigure(figure, selectedNode);
    }
    
    public int maxCurrentConsecutiveRolls()
    {
        TeamColor currentTeam = teams[currentTeamIndex];
        
        // All Figures are outside
        if(ludoBoard.isHomeFull(currentTeam))
        {
            return MAX_CONSECUTIVE_ROLLS;
        }
        
        // All Figures don't further impact the roll count
        List<NodeFigure> remainingTeamFigures = Arrays.stream(ludoBoard.getTeamFigures(currentTeam))
                .filter((figure) -> ((LudoNode) figure.getCurrentNode()).getNodeType() == LudoNodeType.OUTSIDE)
                .toList();
        
        // Check for figures remaining on the board
        for(NodeFigure figure : remainingTeamFigures)
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
    
    private Map<NodeFigure, List<INode>> getMovableFigures(int roll)
    {
        Map<NodeFigure, List<INode>> movableFigures = new HashMap<>(4);
        
        for(NodeFigure teamFigure : ludoBoard.getTeamFigures(teams[currentTeamIndex]))
        {
            List<INode> forwardNodes = ludoBoard.getForwardNodes(teamFigure, roll, createMovablePredicate((LudoFigure) teamFigure));
            if(forwardNodes.size() > 0)
            {
                movableFigures.put(teamFigure, forwardNodes);
            }
        }
        return movableFigures;
    }
    
    private void nextTeam()
    {
        currentTeamIndex = (currentTeamIndex + 1) % teams.length;
    }
    
    private static Predicate<INode> createMovablePredicate(LudoFigure figure)
    {
        return (node -> {
            if(!(node instanceof LudoNode ludoNode))
            {
                return false;
            }
            if(ludoNode.getColor().equals(figure.getColor()))
            {
                if(ludoNode.getNodeType() == LudoNodeType.START)
                {
                    return false;
                }
                else if(ludoNode.getNodeType() == LudoNodeType.HOME)
                {
                    return true;
                }
            }
            return true;
        });
    }
}
