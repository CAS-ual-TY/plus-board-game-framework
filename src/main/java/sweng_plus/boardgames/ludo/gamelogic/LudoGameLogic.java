package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.Dice;
import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    
    public void startPhaseRoll()
    {
        currentTurnPhase = LudoTurnPhase.ROLL;
        
        // TODO next phase
    }
    
    public void endPhaseRoll()
    {
        
        // TODO next phase
        startPhaseSelectFigure();
    }
    
    public void startPhaseSelectFigure()
    {
        
        // TODO auszuwählende Figuren bestimmen (bewegbar mit roll-ergebnis)
        
        currentTurnPhase = LudoTurnPhase.SELECT_FIGURE;
    
        Map<NodeFigure, List<INode>> movableFigures = getMovableFigures(latestRoll);
        // TODO send clients selected Figure and roll result
        if(isServer)
        {
            // TODO send clients roll result
            // TODO send clients selectable figures
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
        if(latestRoll != 6)
        {
            nextTeam();
        }
        else
        {
            numConsecutiveRolls++;
        }
        startPhaseRoll();
    }
    
    public int roll()
    {
        return latestRoll = dice.roll();
    }
    
    public LudoNode selectNode(NodeFigure figure)
    {
        // TODO add correct Predicate
        List<INode> forwardNodes = ludoBoard.getForwardNodes(figure, latestRoll, (x) -> true);
        
        // wait for User Input
        return (LudoNode) forwardNodes.get(0);
    }
    
    public void moveFigure(NodeFigure figure, LudoNode selectedNode)
    {
        ludoBoard.moveFigure(figure, selectedNode);
    }
    
    public int maxCurrentConsecutiveRolls() {
        TeamColor currentTeam = teams[currentTeamIndex];
        
        // All Figures are outside
        if(ludoBoard.isHomeFull(currentTeam)) {
            return MAX_CONSECUTIVE_ROLLS;
        }
    
        // All Figures don't further impact the roll count
        List<NodeFigure> remainingTeamFigures = Arrays.stream(ludoBoard.getTeamFigures(currentTeam)).filter((figure) -> ((LudoNode)figure.getCurrentNode()).getNodeType() == LudoNodeType.OUTSIDE).toList();
    
        for(NodeFigure figure : remainingTeamFigures)
        {
            // At least one figure out on the board
            if(((LudoNode)figure.getCurrentNode()).getNodeType() != LudoNodeType.HOME) {
                return MIN_CONSECUTIVE_ROLLS;
            }
        }
        
        List<LudoNode> homeNodes = ludoBoard.getHomeNodes(currentTeamIndex);
        for(int i = (homeNodes.size() - remainingTeamFigures.size()); i < homeNodes.size(); i++)
        {
            // At least one figure in the house could move - not on last reachable node
            if(!homeNodes.get(i).isOccupied()) {
                return MIN_CONSECUTIVE_ROLLS;
            }
        }
        return MAX_CONSECUTIVE_ROLLS;
        
    }
    
    private Map<NodeFigure, List<INode>> getMovableFigures(int roll)
    {
        Predicate<INode> pred = (x) -> true;
        // TODO add correct Predicate
        Map<NodeFigure, List<INode>> movableFigures = new HashMap<>(4);
        
        for(NodeFigure teamFigure : ludoBoard.getTeamFigures(teams[currentTeamIndex]))
        {
            movableFigures.put(teamFigure, ludoBoard.getForwardNodes(teamFigure, roll, pred));
        }
        return movableFigures;
    }
    
    private void nextTeam()
    {
        currentTeamIndex = (currentTeamIndex + 1) % teams.length;
    }
}
