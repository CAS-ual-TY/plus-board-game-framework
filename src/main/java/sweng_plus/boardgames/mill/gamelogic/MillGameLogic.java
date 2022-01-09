package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gamelogic.networking.FigureNodeSelectedMessage;
import sweng_plus.boardgames.mill.gamelogic.networking.MillClient;
import sweng_plus.boardgames.mill.gamelogic.networking.StartGameMessage;
import sweng_plus.boardgames.mill.gamelogic.networking.WinMessage;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;

import java.util.*;
import java.util.stream.Collectors;

public class MillGameLogic
{
    public final TeamColor[] teams;
    private final boolean isServer;
    
    private MillBoard millBoard;
    public int currentTeamIndex;
    
    protected Map<MillFigure, List<MillNode>> movableFigures;
    protected List<MillFigure> takeableFigures;
    
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
    
    public void endPhaseSelectFigure(int figure)
    {
        System.out.println("                Logic: endPhaseSelectFigure");
        
        if(isServer && isGameWon(currentTeamIndex))
        {
            Mill.instance().getNetworking().getHostManager().sendMessageToAllClients(
                    new WinMessage(currentTeamIndex));
            return;
        }
        
        nextTeam();

    }
    
    public void startPhaseSelectTargetNode(MillFigure figure)
    {
        System.out.println("                Logic: startPhaseSelectNode");
        movableFigures.get(figure);
    }
    
    public void tellClientsFigureNodeSelected(int selectedFigure, int selectedNode)
    {
        if(isServer)
        {
            Mill.instance().getNetworking().getHostManager().sendMessageToAllClients(
                    new FigureNodeSelectedMessage(selectedFigure, selectedNode));
        }
    }
    /*
    public void tellClientsNodeSelected(int selectedNode)
    {
    
    }
     */
    
    public MillFigure startPhaseMoveFigure(int selectedFigure)
    {
        System.out.println("                Logic: startPhaseMoveFigure");
        
        if(selectedFigure >= 0)
        {
            MillFigure figure = getFigureForIndex(selectedFigure);
            if(figure != null)
            {
                MillNode oldNode = figure.getCurrentNode();
                figure.getCurrentNode().removeFigure(figure);
                figure.setCurrentNode(null);
    
                // TODO mill status updaten
                updateMillStatusForNeighbours(oldNode);
                
                
            }
            return figure;
        }
        else
        {
            return null;
        }
    }
    /*
    public MillNode getTargetNode(MillFigure figure)
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
    
     */
    
    public boolean endPhaseMoveFigure(MillFigure figure, MillNode target)
    {
        System.out.println("                Logic: endPhaseMoveFigure");
        
        millBoard.moveFigure(figure, target);
        
        return isMillCreated(figure);
    }
    
    public void moveFigureToOutside(MillFigure figure)
    {
        
        System.out.println("                Logic: moveFigureToOutside");
        
        millBoard.takeFigure(figure);
    }
    
    
    public void startPhaseTakeFigure()
    {
        System.out.println("                Logic: startPhaseTakeFigure");
        takeableFigures = millBoard.getActiveTeamFigures(millBoard.getTeamFromIndex((currentTeamIndex+1)%teams.length));
    }
    
    public MillFigure getFigureForIndex(int selectedFigure)
    {
        return movableFigures.keySet().stream().filter(fig -> fig.getIndex() == selectedFigure).findFirst().orElse(null);
    }
    
    public MillNode getNodeForIndex(int node)
    {
        return millBoard.getFieldNode(node);
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
    
        boolean mill = false;
    
        if(index % 2 == 0)
        {
            
    
            if(checkForMillAndSetStatus(movedFigure,
                    millBoard.getFieldNode((index + 1) % MillBoard.NODES_PER_CIRCLE + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE)*MillBoard.NODES_PER_CIRCLE),
                    millBoard.getFieldNode((index + 2) % MillBoard.NODES_PER_CIRCLE)))
            {
                mill = true;
            }
            if(checkForMillAndSetStatus(movedFigure,
                    millBoard.getFieldNode(Math.floorMod(index - 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE)*MillBoard.NODES_PER_CIRCLE),   // Math.floorMod for positive remainder
                    millBoard.getFieldNode(Math.floorMod(index - 2, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE)*MillBoard.NODES_PER_CIRCLE)))
            {
                mill = true;
            }
        }
        
        else
        {
            // direct neighbours
            if(checkForMillAndSetStatus(movedFigure,
                    millBoard.getFieldNode(Math.floorMod(index + 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE)*MillBoard.NODES_PER_CIRCLE),
                    millBoard.getFieldNode(Math.floorMod(index - 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE)*MillBoard.NODES_PER_CIRCLE)))
            {
                mill = true;
            }
    
            if(index <= MillBoard.NODES_PER_CIRCLE - 1)
            {
                if(checkForMillAndSetStatus(movedFigure,
                        millBoard.getFieldNode(index + MillBoard.NODES_PER_CIRCLE),
                        millBoard.getFieldNode(index + 2 * MillBoard.NODES_PER_CIRCLE)))
                {
                    mill = true;
                }
            }
            else if(index <= MillBoard.NODES_PER_CIRCLE*2 - 1)
            {
                if(checkForMillAndSetStatus(movedFigure,
                        millBoard.getFieldNode(index - MillBoard.NODES_PER_CIRCLE),
                        millBoard.getFieldNode(index + MillBoard.NODES_PER_CIRCLE)))
                {
                    mill = true;
                }
            }
            else if (index < MillBoard.NODES_PER_CIRCLE*3 - 1)
            {
                if(checkForMillAndSetStatus(movedFigure,
                        millBoard.getFieldNode(index - 2 * MillBoard.NODES_PER_CIRCLE),
                        millBoard.getFieldNode(index - MillBoard.NODES_PER_CIRCLE)))
                {
                    mill = true;
                }
            }
        }
        movedFigure.setInMill(mill);
        return mill;
    }
    
    private boolean checkForMillAndSetStatus(MillFigure centerFigure, MillNode neighbour1, MillNode neighbour2) {
        // whole line occupied
        if(neighbour1.isOccupied() && neighbour2.isOccupied()) {
            // all figures have same colour
            if(neighbour1.getFigures().get(0).getTeam().equals(centerFigure.getTeam()) && neighbour2.getFigures().get(0).getTeam().equals(centerFigure.getTeam())) {
                neighbour1.getFigures().get(0).setInMill(true);
                neighbour2.getFigures().get(0).setInMill(true);
                centerFigure.setInMill(true);
                return true;
            }
        }
        return false;
    }
    
    public void updateMillStatusForNeighbours(MillNode startNode)
    {
        // index even -> corner (+/-2)
        // odd
        //      direct neighbours (+/-1)
        //      (+/-)(1/2) circles
        //          + 2 if <= 7
        //          +/- 1 if > 7 && <= 15
        //          - 2 if > 15
        
        // TODO requires testing
        int index = startNode.getIndex();
        
        MillNode node;
        
        if(index%2 == 0)
        {
            node = millBoard.getFieldNode((index + 1)%MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied()) {
                updateInMillStatus(node.getFigures().get(0));
            }
            node = millBoard.getFieldNode((index + 2)%MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied()) {
                updateInMillStatus(node.getFigures().get(0));
            }
            node = millBoard.getFieldNode(Math.floorMod(index - 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE)*MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied()) {
                updateInMillStatus(node.getFigures().get(0));
            }
            node = millBoard.getFieldNode(Math.floorMod(index - 2, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE)*MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied()) {
                updateInMillStatus(node.getFigures().get(0));
            }
        }
        else {
            node = millBoard.getFieldNode((index + 1)%MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied()) {
                updateInMillStatus(node.getFigures().get(0));
            }
            node = millBoard.getFieldNode(Math.floorMod(index - 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE)*MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied()) {
                updateInMillStatus(node.getFigures().get(0));
            }
    
            if (index <= 7)
            {
                node = millBoard.getFieldNode(index + MillBoard.NODES_PER_CIRCLE);
                if(node.isOccupied()) {
                    updateInMillStatus(node.getFigures().get(0));
                }
                node = millBoard.getFieldNode(index + 2 * MillBoard.NODES_PER_CIRCLE);
                if(node.isOccupied()) {
                    updateInMillStatus(node.getFigures().get(0));
                }
            }
            else if (index <= 15)
            {
                node = millBoard.getFieldNode(index - MillBoard.NODES_PER_CIRCLE);
                if(node.isOccupied()) {
                    updateInMillStatus(node.getFigures().get(0));
                }
                node = millBoard.getFieldNode(index + MillBoard.NODES_PER_CIRCLE);
                if(node.isOccupied()) {
                    updateInMillStatus(node.getFigures().get(0));
                }
            }
            else
            {
        
                node = millBoard.getFieldNode(index - 2 * MillBoard.NODES_PER_CIRCLE);
                if(node.isOccupied()) {
                    updateInMillStatus(node.getFigures().get(0));
                }
                node = millBoard.getFieldNode(index - MillBoard.NODES_PER_CIRCLE);
                if(node.isOccupied()) {
                    updateInMillStatus(node.getFigures().get(0));
                }
            }
        }
        
    }
    
    private void updateInMillStatus(MillFigure figure)
    {
        figure.setInMill(isMillCreated(figure));
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
    
    public boolean isGameWon(int teamIndex) {
        return !(millBoard.getTeamFromIndex(teamIndex).equals(gameWon()));  // other Team has less than 3 active figures
    }
    
    public Map<MillFigure, List<MillNode>> getMovableFigures()
    {
        Map<MillFigure, List<MillNode>> movableFigures;
    
        movableFigures = getUnplacedFigures().stream().collect(Collectors.toMap(figure -> figure, figure -> millBoard.getFreeFieldNodes()));
    
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
    
    public List<MillFigure> getUnplacedFigures()
    {
        return millBoard.getActiveTeamFigures(teams[currentTeamIndex]).stream().filter(figure -> !figure.isAlreadyPlaced()).collect(Collectors.toList());
    }
    
    private MillNode takeFigure(MillFigure figure)
    {
        return millBoard.takeFigure(figure);
    }
    
    private void nextTeam()
    {
        currentTeamIndex = (currentTeamIndex + 1) % teams.length;
    }
    
    
    
    
    // TODO Figur auswählen
        // TODO Aufbau oder Zugphase
    // TODO Zielfeld auswählen
    // TODO auf Mühle prüfen
    // TODO gegnerische Figur zum Entfernen auswählen
        // TODO Wincondition
}
