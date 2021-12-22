package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gamelogic.networking.MillClient;
import sweng_plus.boardgames.mill.gamelogic.networking.StartGameMessage;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;

import java.util.*;
import java.util.stream.Collectors;

public class MillGameLogic
{
    private TeamColor[] teams;
    private final boolean isServer;
    
    private MillBoard millBoard;
    private int currentTeamIndex;
    
    protected Map<MillFigure, List<MillNode>> movableFigures;
    
    public MillGameLogic(TeamColor[] teams, boolean isServer) {
        this.teams = teams;
        this.isServer = isServer;
        
        this.millBoard = new MillBoard(teams);
    }
    
    public TeamColor[] getTeams()
    {
        return teams;
    }
    
    public MillBoard getMillBoard()
    {
        return millBoard;
    }
    
    public void startGame()
    {
        System.out.println("                Logic: startGame");
        currentTeamIndex = 0;
    
        if(isServer)
        {
            for(MillClient client : Mill.instance().getNetworking().getHostManager().getAllClients())
            {
                Mill.instance().getNetworking().getHostManager()
                        .sendMessageToClient(client,
                                new StartGameMessage(client.getTeamIndex(), teams.length, currentTeamIndex));
            }
        }
    }
    
    public void startPhaseSelectFigure()
    {
        System.out.println("                Logic: startPhaseSelectFigure");
        movableFigures = getMovableFigures();
    }
    
    public void tellClientsFigureSelected(int selectedFigure)
    {
    
    }
    
    public void startPhaseSelectTargetNode(MillFigure figure)
    {
        System.out.println("                Logic: startPhaseSelectNode");
        movableFigures.get(figure);
    }
    
    public void tellClientsNodeSelected(int selectedNode)
    {
    
    }
    
    public void startPhaseTakeFigure(MillFigure figure)
    {
        System.out.println("                Logic: startPhaseTakeFigure");
        
    }
    
    public boolean isMillCreated(MillFigure movedFigure)
    {
        // index even -> corner (+/-2)
        // odd
        //      direct neighbours (+/-1)
        //      (+/-)(1/2) circles
        //          + 2 if <= 7
        //          +/- 1 if > 7 && <= 15
        //          - 2 if > 15
        
        // TODO requires testing
        int index = movedFigure.getCurrentNode().getIndex();
        
        if(index%2 == 0)
        {
            return (millBoard.getFieldNode((index + 1)%MillBoard.NODES_PER_CIRCLE).isOccupied() && millBoard.getFieldNode((index + 2)%MillBoard.NODES_PER_CIRCLE).isOccupied()) ||
                    (millBoard.getFieldNode((index - 1)%MillBoard.NODES_PER_CIRCLE).isOccupied() && millBoard.getFieldNode((index - 2)%MillBoard.NODES_PER_CIRCLE).isOccupied());
        }
        else if (index <= 7)
        {
            return (millBoard.getFieldNode((index + 1)%MillBoard.NODES_PER_CIRCLE).isOccupied() && millBoard.getFieldNode((index - 1)%MillBoard.NODES_PER_CIRCLE).isOccupied()) ||
                    (millBoard.getFieldNode(index + MillBoard.NODES_PER_CIRCLE).isOccupied() && millBoard.getFieldNode(index + 2 * MillBoard.NODES_PER_CIRCLE).isOccupied());
        }
        else if (index <= 15)
        {
            return (millBoard.getFieldNode((index + 1)%MillBoard.NODES_PER_CIRCLE).isOccupied() && millBoard.getFieldNode((index - 1)%MillBoard.NODES_PER_CIRCLE).isOccupied()) ||
                    (millBoard.getFieldNode(index - MillBoard.NODES_PER_CIRCLE).isOccupied() && millBoard.getFieldNode(index + MillBoard.NODES_PER_CIRCLE).isOccupied());
        }
        else
        {
            return (millBoard.getFieldNode((index + 1)%MillBoard.NODES_PER_CIRCLE).isOccupied() && millBoard.getFieldNode((index - 1)%MillBoard.NODES_PER_CIRCLE).isOccupied()) ||
                    (millBoard.getFieldNode(index - 2 * MillBoard.NODES_PER_CIRCLE).isOccupied() && millBoard.getFieldNode(index - MillBoard.NODES_PER_CIRCLE).isOccupied());
        }
    }
    
    public TeamColor gameWon() {
        for(TeamColor team : teams)
        {
            if(millBoard.getActiveTeamFigures(team).size() < 3) {
                return team;
            }
        }
        return null;
    }
    
    public boolean isGameWon() {
        return gameWon() != null;
    }
    
    private Map<MillFigure, List<MillNode>> getMovableFigures()
    {
        Map<MillFigure, List<MillNode>> movableFigures = getUnplacedFigures();
    
        if(movableFigures.isEmpty())
        {
            movableFigures = new HashMap<>(MillBoard.FIGURES_PER_TEAM);

            // normal moving - only 1 node away
            if(millBoard.getActiveTeamFigures(teams[currentTeamIndex]).size() > MillBoard.MAX_FIGURES_TO_JUMP)
            {
                for(MillFigure teamFigure : millBoard.getActiveTeamFigures(teams[currentTeamIndex]))
                {
                    List<MillNode> nodes = millBoard.getForwardAndBackwardNodes(teamFigure, 1, millNode -> !millNode.isOccupied());
                    if(!nodes.isEmpty())
                    {
                        movableFigures.put(teamFigure, nodes);
                    }
                }
            }
            else
            {
                for(MillFigure teamFigure : millBoard.getActiveTeamFigures(teams[currentTeamIndex]))
                {
                    List<MillNode> nodes = millBoard.getFieldNodes().stream().filter(millNode -> !millNode.isOccupied()).collect(Collectors.toList());
                    if(!nodes.isEmpty())
                    {
                        movableFigures.put(teamFigure, nodes);
                    }
                }
            }
        }
        return movableFigures;
    }
    
    private Map<MillFigure, List<MillNode>> getUnplacedFigures()
    {
        return millBoard.getActiveTeamFigures(teams[currentTeamIndex]).stream().filter(figure -> !figure.isAlreadyPlaced()).collect(Collectors.toMap(figure -> figure, figure -> millBoard.getFieldNodes()));
    }
    
    private void takeFigure(MillFigure figure)
    {
        millBoard.takeFigure(figure);
    }
    
    public static void main(String... args)
    {
        System.out.println("test");
        
        MillGameLogic logic = new MillGameLogic(new TeamColor[] {TeamColor.RED, TeamColor.BLUE}, false);
        MillBoard board = logic.getMillBoard();
        List<MillFigure> figures = board.getActiveTeamFigures(TeamColor.RED);
    
        System.out.println();
        System.out.println("NumNodes = " + board.getFieldNodes().size());
        
    
        for(int i = 0; i < figures.size(); i++)
        {
            board.moveFigure(figures.get(i), board.getFieldNodes().get((i*2) +1));
        }
        System.out.println("NumUnplacedM = " + logic.getUnplacedFigures().size());
    
        
        System.out.println("FWFields = " + logic.getMovableFigures().get(figures.get(figures.size()-1)).size());
        System.out.println();
    
        System.out.println("FWFieldsB = " + logic.getMovableFigures().get(figures.get(figures.size()-1)).size() + " - " + figures.size());
        for(int i = 1; i < MillBoard.FIGURES_PER_TEAM; i++)
        {
            logic.takeFigure(figures.get(0));
            
            if(logic.getMovableFigures().get(figures.get(0)) != null)
            {
                System.out.println("FWFieldsF = " + logic.getMovableFigures().get(figures.get(0)).size() + " - " + figures.size());
            }
            else
            {
                System.out.println("FWFieldsB = " + logic.getMovableFigures().get(figures.get(figures.size()-1)).size() + " - " + figures.size());
            }
    
            System.out.println("NumTaken = " + board.getTakenTeamFigures(TeamColor.RED).size());
        }
        figures.remove(figures.get(0));
        System.out.println("FWFields = " + logic.getMovableFigures().size() + " - " + figures.size());
        System.out.println();
        
        System.out.println("NumFigures = " + logic.getUnplacedFigures().size());
        
        //logic.millBoard.removeFigureFromTeam(TeamColor.RED);
        //logic.getMillBoard().
        //System.out.println();
    }
    
    
    // TODO Figur auswählen
        // TODO Aufbau oder Zugphase
    // TODO Zielfeld auswählen
    // TODO auf Mühle prüfen
    // TODO gegnerische Figur zum Entfernen auswählen
        // TODO Wincondition
}
