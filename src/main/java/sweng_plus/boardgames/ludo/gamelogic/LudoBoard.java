package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.NodeBoard;
import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LudoBoard extends NodeBoard
{
    public static final int HOUSES_PER_CORNER = 4;
    public static final int NEUTRAL_NODES_PER_CORNER = 8;
    
    // +2 because of: 1x Start, 1x Home Entrance
    public static final int TOTAL_NODES_PER_CORNER = HOUSES_PER_CORNER + 2 + HOUSES_PER_CORNER + NEUTRAL_NODES_PER_CORNER;
    
    private LudoTeam[] ludoTeams;
    
    public LudoBoard(TeamColor[] teamColors)
    {
        super(new LinkedList<>(), new LinkedList<>());
        
        setupBoard(teamColors);
    }
    
    public int getTeamIndex(TeamColor team)
    {
        for(int i = 0; i < ludoTeams.length; ++i)
        {
            if(team == ludoTeams[i].color())
            {
                return i;
            }
        }
        
        return -1;
    }
    
    public NodeFigure[] getTeamFigures(TeamColor team)
    {
        for(LudoTeam value : ludoTeams)
        {
            if(team == value.color())
            {
                return value.figures();
            }
        }
        
        return null;
    }
    
    
    @SuppressWarnings("unchecked")
    private void setupBoard(TeamColor[] teamColors)
    {
        ludoTeams = new LudoTeam[teamColors.length];
        
        for(int i = 0; i < ludoTeams.length; i++)
        {
            final int finalI = i;
            createBoardCorner(teamColors[finalI], (cornerOutside, cornerStart, cornerHomeEntrance, cornerHome, cornerLast, allNodes, figures) ->
            {
                ludoTeams[finalI] = new LudoTeam(teamColors[finalI], figures, cornerOutside, cornerStart, cornerHomeEntrance, cornerHome, cornerLast, allNodes);
                addAllNodes(allNodes);
            });
        }
        
        for(int i = 0; i < ludoTeams.length; i++)
        {
            INode.linkNodes(ludoTeams[i].last(), ludoTeams[(i + 1) % ludoTeams.length].homeEntrance());
        }
    }
    
    private static void createBoardCorner(TeamColor ludoTeam, BoardCornerConsumer consumer)
    {
        ArrayList<LudoNode> outside = new ArrayList<>(HOUSES_PER_CORNER);
        LudoNode start;
        LudoNode homeEntrance;
        ArrayList<LudoNode> home = new ArrayList<>(HOUSES_PER_CORNER);
        ArrayList<LudoNode> allNodes = new ArrayList<>(TOTAL_NODES_PER_CORNER);
        
        LudoNode last = null;
        LudoNode current = null;
        LudoNode previous = null;
        
        start = new LudoNode(ludoTeam, LudoNodeType.START, 0);
        
        for(int i = 0; i < HOUSES_PER_CORNER; i++)
        {
            outside.add(current = new LudoNode(ludoTeam, LudoNodeType.OUTSIDE, i));
            INode.linkNodes(current, start);
        }
        
        homeEntrance = new LudoNode(ludoTeam, LudoNodeType.HOME_ENTRANCE, 0);
        INode.linkNodes(homeEntrance, start);
        
        for(int i = 0; i < HOUSES_PER_CORNER; i++)
        {
            home.add(current = new LudoNode(ludoTeam, LudoNodeType.HOME, i));
            
            if(previous != null)
            {
                INode.linkNodes(previous, current);
            }
            previous = current;
        }
        
        NodeFigure[] figures = createFigures(ludoTeam, outside);
        
        allNodes.addAll(outside);
        allNodes.add(start);
        allNodes.add(homeEntrance);
        allNodes.addAll(home);
        
        previous = start;
        for(int i = 0; i < NEUTRAL_NODES_PER_CORNER; i++)
        {
            allNodes.add(last = new LudoNode(ludoTeam, LudoNodeType.NEUTRAL, i));
            
            INode.linkNodes(previous, last);
            
            previous = last;
        }
        
        consumer.forBoardCorner(outside, start, homeEntrance, home, last, allNodes, figures);
    }
    
    public int getTeamsAmount()
    {
        return ludoTeams.length;
    }
    
    public LudoTeam[] getTeams()
    {
        return ludoTeams;
    }
    
    public List<LudoNode> getOutsideNodes(int team)
    {
        return ludoTeams[team].outside();
    }
    
    public LudoNode getStartNode(int team)
    {
        return ludoTeams[team].start();
    }
    
    public LudoNode getHomeEntranceNode(int team)
    {
        return ludoTeams[team].homeEntrance();
    }
    
    public List<LudoNode> getHomeNodes(int team)
    {
        return ludoTeams[team].home();
    }
    
    public List<LudoNode> getFullCornerNodes(int team)
    {
        return ludoTeams[team].allNodes();
    }
    
    public boolean isOutsideEmpty(TeamColor team)
    {
        for(LudoNode node : ludoTeams[getTeamIndex(team)].outside())
        {
            if(node.isOccupied())
            {
                return false;
            }
        }
        return true;
    }
    
    public boolean isOutsideFull(TeamColor team)
    {
        for(LudoNode node : ludoTeams[getTeamIndex(team)].outside())
        {
            if(!node.isOccupied())
            {
                return false;
            }
        }
        return true;
    }
    
    public boolean isHomeFull(TeamColor team)
    {
        for(LudoNode node : ludoTeams[getTeamIndex(team)].home())
        {
            if(!node.isOccupied())
            {
                return false;
            }
        }
        return true;
    }
    
    private static NodeFigure[] createFigures(TeamColor teamColor, List<LudoNode> nodes)
    {
        NodeFigure[] figures = new NodeFigure[nodes.size()];
        for(int i = 0; i < nodes.size(); i++)
        {
            figures[i] = new NodeFigure(nodes.get(i), teamColor);
            nodes.get(i).addNodeFigure(figures[i]);
        }
        return figures;
    }
    
    public interface BoardCornerConsumer
    {
        void forBoardCorner(List<LudoNode> outside, LudoNode start, LudoNode homeEntrance, List<LudoNode> home, LudoNode last, List<LudoNode> allNodes, NodeFigure[] figures);
    }
    
    private record LudoTeam(TeamColor color, NodeFigure[] figures, List<LudoNode> outside, LudoNode start,
                            LudoNode homeEntrance, List<LudoNode> home, LudoNode last, List<LudoNode> allNodes)
    {
    }
}
