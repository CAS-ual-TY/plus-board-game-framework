package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gamelogic.networking.*;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MillGameLogic
{
    public final TeamColor[] teams;
    private final boolean isServer;
    
    private MillBoard millBoard;
    private int currentTeamIndex;
    
    protected Map<MillFigure, List<MillNode>> movableFigures;
    protected List<MillFigure> takeableFigures;
    
    public MillGameLogic(TeamColor[] teams, boolean isServer)
    {
        this.teams = teams;
        this.isServer = isServer;
        
        millBoard = new MillBoard(teams);
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
        setCurrentTeamIndex(getMillBoard().getTeamIndex(TeamColor.WHITE));
        //setCurrentTeamIndex(0);
        
        if(isServer)
        {
            for(MillClient client : Mill.instance().getNetworking().getHostManager().getAllClients())
            {
                Mill.instance().getNetworking().getHostManager()
                        .sendMessageToClient(client,
                                new StartGameMessage(client.getTeamIndex(), teams.length, getCurrentTeamIndex()));
            }
        }
    }
    
    public void startPhaseSelectFigure()
    {
        System.out.println("                Logic: startPhaseSelectFigure");
        movableFigures = getMovableFigures();
    }
    
    public void endPhaseSelectFigure()
    {
        System.out.println("                Logic: endPhaseSelectFigure");
        
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
    
    public void tellClientsFigureTaken(int selectedFigure)
    {
        if(isServer)
        {
            Mill.instance().getNetworking().getHostManager().sendMessageToAllClients(
                    new FigureTakenMessage(selectedFigure));
            
            if(isGameWon())
            {
                Mill.instance().getNetworking().getHostManager().sendMessageToAllClients(
                        new WinMessage((millBoard.getTeamIndex(getLosingTeam()) + 1) % 2));
                
            }
        }
    }
    
    public MillFigure startPhaseMoveFigure(int selectedFigure)
    {
        System.out.println("                Logic: startPhaseMoveFigure");
        
        if(selectedFigure >= 0)
        {
            MillFigure figure = getMovableFigureForIndex(selectedFigure);
            if(figure != null)
            {
                MillNode oldNode = figure.getCurrentNode();
                figure.getCurrentNode().removeFigure(figure);
                figure.setCurrentNode(null);
                
                updateMillStatusForNeighbours(oldNode);
                
                
            }
            return figure;
        }
        else
        {
            return null;
        }
    }
    
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
        takeableFigures = millBoard.getActiveTeamFigures(millBoard.getTeamFromIndex((getCurrentTeamIndex() + 1) % teams.length)).stream().filter(figure -> figure.isAlreadyPlaced() && !figure.isInMill()).collect(Collectors.toList());
    }
    
    public void endPhaseTakeFigure(MillFigure figure)
    {
        System.out.println("                Logic: endPhaseTakeFigure");
        
        moveFigureToOutside(figure);
    }
    
    public MillFigure getMovableFigureForIndex(int selectedFigure)
    {
        return movableFigures.keySet().stream().filter(fig -> fig.getIndex() == selectedFigure).findFirst().orElse(null);
    }
    
    public List<MillFigure> getTakeableFigure()
    {
        return takeableFigures;
    }
    
    public MillFigure getTakeableFigureForIndex(int selectedFigure)
    {
        return takeableFigures.stream().filter(fig -> fig.getIndex() == selectedFigure).findFirst().orElse(null);
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
        
        int index = movedFigure.getCurrentNode().getIndex();
        
        boolean mill = false;
        
        if(index % 2 == 0)
        {
            if(checkForMillAndSetStatus(movedFigure,
                    millBoard.getFieldNode((index + 1) % MillBoard.NODES_PER_CIRCLE + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE),
                    millBoard.getFieldNode((index + 2) % MillBoard.NODES_PER_CIRCLE + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE)))
            {
                mill = true;
            }
            else if(checkForMillAndSetStatus(movedFigure,
                    millBoard.getFieldNode(Math.floorMod(index - 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE),   // Math.floorMod for positive remainder
                    millBoard.getFieldNode(Math.floorMod(index - 2, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE)))
            {
                mill = true;
            }
        }
        
        else
        {
            // direct neighbours
            if(checkForMillAndSetStatus(movedFigure,
                    millBoard.getFieldNode(Math.floorMod(index + 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE),
                    millBoard.getFieldNode(Math.floorMod(index - 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE)))
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
            else if(index <= MillBoard.NODES_PER_CIRCLE * 2 - 1)
            {
                if(checkForMillAndSetStatus(movedFigure,
                        millBoard.getFieldNode(index - MillBoard.NODES_PER_CIRCLE),
                        millBoard.getFieldNode(index + MillBoard.NODES_PER_CIRCLE)))
                {
                    mill = true;
                }
            }
            else if(index <= MillBoard.NODES_PER_CIRCLE * 3 - 1)
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
    
    private boolean checkForMillAndSetStatus(MillFigure centerFigure, MillNode neighbour1, MillNode neighbour2)
    {
        // whole line occupied
        if(neighbour1.isOccupied() && neighbour2.isOccupied())
        {
            // all figures have same colour
            if(neighbour1.getFigures().get(0).getTeam().equals(centerFigure.getTeam()) && neighbour2.getFigures().get(0).getTeam().equals(centerFigure.getTeam()))
            {
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
        
        // outside or invalid node - no action required
        if(startNode.getIndex() >= millBoard.getFieldNodes().size())
        {
            return;
        }
        
        int index = startNode.getIndex();
        
        MillNode node;
        
        if(index % 2 == 0)
        {
            node = millBoard.getFieldNode((index + 1) % MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied())
            {
                updateInMillStatus(node.getFigures().get(0));
            }
            node = millBoard.getFieldNode((index + 2) % MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied())
            {
                updateInMillStatus(node.getFigures().get(0));
            }
            node = millBoard.getFieldNode(Math.floorMod(index - 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied())
            {
                updateInMillStatus(node.getFigures().get(0));
            }
            node = millBoard.getFieldNode(Math.floorMod(index - 2, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied())
            {
                updateInMillStatus(node.getFigures().get(0));
            }
        }
        else
        {
            node = millBoard.getFieldNode(Math.floorMod(index + 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied())
            {
                updateInMillStatus(node.getFigures().get(0));
            }
            node = millBoard.getFieldNode(Math.floorMod(index - 1, MillBoard.NODES_PER_CIRCLE) + Math.floorDiv(index, MillBoard.NODES_PER_CIRCLE) * MillBoard.NODES_PER_CIRCLE);
            if(node.isOccupied())
            {
                updateInMillStatus(node.getFigures().get(0));
            }
            
            
            if(index <= MillBoard.NODES_PER_CIRCLE * 3)
            {
                node = millBoard.getFieldNode((index + MillBoard.NODES_PER_CIRCLE) % (MillBoard.NUM_CIRCLES * MillBoard.NODES_PER_CIRCLE));
                if(node.isOccupied())
                {
                    updateInMillStatus(node.getFigures().get(0));
                }
                node = millBoard.getFieldNode((index + 2 * MillBoard.NODES_PER_CIRCLE) % (MillBoard.NUM_CIRCLES * MillBoard.NODES_PER_CIRCLE));
                if(node.isOccupied())
                {
                    updateInMillStatus(node.getFigures().get(0));
                }
            }
        }
        
    }
    
    private void updateInMillStatus(MillFigure figure)
    {
        figure.setInMill(isMillCreated(figure));
    }
    
    private TeamColor getLosingTeam()
    {
        for(TeamColor team : teams)
        {
            if(millBoard.getActiveTeamFigures(team).size() < 3)
            {   // Game is lost if own team has less than 3 active figures
                return team;
            }
        }
        return null;
    }
    
    public boolean isWinningTeam(int teamIndex)
    {
        return (getLosingTeam() != null && millBoard.getTeamFromIndex(teamIndex) != (getLosingTeam()));  // other Team has less than 3 active figures
    }
    
    public boolean isGameWon()
    {
        return getLosingTeam() != null;  // other Team has less than 3 active figures
    }
    
    public Map<MillFigure, List<MillNode>> getMovableFigures()
    {
        Map<MillFigure, List<MillNode>> movableFigures;
        
        movableFigures = getUnplacedFigures().stream().collect(Collectors.toMap(figure -> figure, figure -> millBoard.getFreeFieldNodes()));
        
        if(movableFigures.isEmpty())
        {
            movableFigures = new HashMap<>(MillBoard.FIGURES_PER_TEAM);
            
            // normal moving - only 1 node away
            if(millBoard.getActiveTeamFigures(teams[getCurrentTeamIndex()]).size() > MillBoard.MAX_FIGURES_TO_JUMP)
            {
                for(MillFigure teamFigure : millBoard.getActiveTeamFigures(teams[getCurrentTeamIndex()]))
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
                for(MillFigure teamFigure : millBoard.getActiveTeamFigures(teams[getCurrentTeamIndex()]))
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
        return millBoard.getActiveTeamFigures(teams[getCurrentTeamIndex()]).stream().filter(figure -> !figure.isAlreadyPlaced()).collect(Collectors.toList());
    }
    
    private MillNode takeFigure(MillFigure figure)
    {
        return millBoard.takeFigure(figure);
    }
    
    private void nextTeam()
    {
        setCurrentTeamIndex((getCurrentTeamIndex() + 1) % teams.length);
    }
    
    public void setTurnTeam(int team)
    {
        setCurrentTeamIndex(team);
    }
    
    public int getCurrentTeamIndex()
    {
        return currentTeamIndex;
    }
    
    public TeamColor getCurrentTeam()
    {
        return teams[currentTeamIndex];
    }
    
    public TeamColor getOtherTeam()
    {
        return teams[(currentTeamIndex + 1) % teams.length];
    }
    
    public void setCurrentTeamIndex(int currentTeamIndex)
    {
        this.currentTeamIndex = currentTeamIndex;
    }
}
