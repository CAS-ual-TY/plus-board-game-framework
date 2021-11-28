package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.NodeBoard;
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
    
    private TeamColor[] teams;
    private List<LudoNode>[] outside;
    private LudoNode[] start;
    private LudoNode[] homeEntrance;
    private List<LudoNode>[] home;
    private List<LudoNode>[] fullCorner;
    
    public LudoBoard(TeamColor[] teams)
    {
        super(new LinkedList<>(), new LinkedList<>());
        this.teams = teams;
        
        setupBoard();
    }
    
    public int getTeamIndex(TeamColor team)
    {
        for(int i = 0; i < teams.length; ++i)
        {
            if(team == teams[i])
            {
                return i;
            }
        }
        
        return -1;
    }
    
    @SuppressWarnings("unchecked")
    private void setupBoard()
    {
        outside = new List[teams.length];
        start = new LudoNode[teams.length];
        homeEntrance = new LudoNode[teams.length];
        home = new List[teams.length];
        fullCorner = new List[teams.length];
        
        LudoNode[] last = new LudoNode[teams.length];
        
        for(int i = 0; i < teams.length; i++)
        {
            final int finalI = i;
            createBoardCorner(teams[i], (cornerOutside, cornerStart, cornerHomeEntrance, cornerHome, cornerLast, allNodes) ->
            {
                outside[finalI] = cornerOutside;
                start[finalI] = cornerStart;
                homeEntrance[finalI] = cornerHomeEntrance;
                home[finalI] = cornerHome;
                last[finalI] = cornerLast;
                addAllNodes(fullCorner[finalI] = allNodes);
            });
        }
        
        for(int i = 0; i < teams.length; i++)
        {
            INode.linkNodes(last[i], homeEntrance[(i + 1) % teams.length]);
        }
    }
    
    private static void createBoardCorner(TeamColor team, BoardCornerConsumer consumer)
    {
        ArrayList<LudoNode> outside = new ArrayList<>(HOUSES_PER_CORNER);
        LudoNode start;
        LudoNode homeEntrance;
        ArrayList<LudoNode> home = new ArrayList<>(HOUSES_PER_CORNER);
        ArrayList<LudoNode> allNodes = new ArrayList<>(TOTAL_NODES_PER_CORNER);
        
        LudoNode last = null;
        LudoNode current = null;
        LudoNode previous = null;
        
        start = new LudoNode(team, LudoNodeType.START, 0);
        
        for(int i = 0; i < HOUSES_PER_CORNER; i++)
        {
            outside.add(current = new LudoNode(team, LudoNodeType.OUTSIDE, i));
            INode.linkNodes(current, start);
        }
        
        homeEntrance = new LudoNode(team, LudoNodeType.HOME_ENTRANCE, 0);
        INode.linkNodes(homeEntrance, start);
        
        for(int i = 0; i < HOUSES_PER_CORNER; i++)
        {
            home.add(current = new LudoNode(team, LudoNodeType.HOME, i));
            
            if(previous != null)
            {
                INode.linkNodes(previous, current);
            }
            previous = current;
        }
        
        allNodes.addAll(outside);
        allNodes.add(start);
        allNodes.add(homeEntrance);
        allNodes.addAll(home);
        
        previous = start;
        for(int i = 0; i < NEUTRAL_NODES_PER_CORNER; i++)
        {
            allNodes.add(last = new LudoNode(team, LudoNodeType.NEUTRAL, i));
            
            INode.linkNodes(previous, last);
            
            previous = last;
        }
        
        consumer.forBoardCorner(outside, start, homeEntrance, home, last, allNodes);
    }
    
    public int getTeamsAmount()
    {
        return teams.length;
    }
    
    public TeamColor[] getTeams()
    {
        return teams;
    }
    
    public List<LudoNode> getOutsideNodes(int team)
    {
        return outside[team];
    }
    
    public LudoNode getStartNode(int team)
    {
        return start[team];
    }
    
    public LudoNode getHomeEntranceNode(int team)
    {
        return homeEntrance[team];
    }
    
    public List<LudoNode> getHomeNodes(int team)
    {
        return home[team];
    }
    
    public List<LudoNode> getFullCornerNodes(int team)
    {
        return fullCorner[team];
    }
    
    public interface BoardCornerConsumer
    {
        void forBoardCorner(List<LudoNode> outside, LudoNode start, LudoNode homeEntrance, List<LudoNode> home, LudoNode last, List<LudoNode> allNodes);
    }
}
