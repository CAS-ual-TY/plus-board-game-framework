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