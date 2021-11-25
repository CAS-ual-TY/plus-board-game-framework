package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.NodeBoard;
import sweng_plus.framework.boardgame.nodes_board.Node;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LudoBoard extends NodeBoard
{
    public static final int HOUSES_PER_CORNER = 4;
    public static final int NEUTRAL_NODES_PER_CORNER = 8;
    
    private TeamColor[] teams;
    private List<LudoNode>[] outside;
    private LudoNode[] start;
    private LudoNode[] homeEntrance;
    private List<LudoNode>[] home;
    private List<Node>[] fullCorner;
    
    public LudoBoard(TeamColor[] teams)
    {
        super(new LinkedList<>(), new LinkedList<>());
        this.teams = teams;
        
        setupBoard();
    }
    
    @SuppressWarnings("unchecked")
    private void setupBoard()
    {
        outside = new List[teams.length];
        start = new LudoNode[teams.length];
        homeEntrance = new LudoNode[teams.length];
        home = new List[teams.length];
        fullCorner = new List[teams.length];
        
        Node[] last = new Node[teams.length];
        
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
            last[i].addForwardNode(homeEntrance[(i + 1) % teams.length]);
        }
    }
    
    private static void createBoardCorner(TeamColor team, BoardCornerConsumer consumer)
    {
        ArrayList<LudoNode> outside = new ArrayList<>(HOUSES_PER_CORNER);
        LudoNode start;
        LudoNode homeEntrance;
        ArrayList<LudoNode> home = new ArrayList<>(HOUSES_PER_CORNER);
        ArrayList<Node> allNodes = new ArrayList<>(outside.size() + 2 + home.size() + NEUTRAL_NODES_PER_CORNER);
        
        Node last = null;
        LudoNode current = null;
        Node previous = null;
        
        start = new LudoNode(team, LudoNodeType.START);
        
        for(int i = 0; i < HOUSES_PER_CORNER; i++)
        {
            outside.add(current = new LudoNode(team, LudoNodeType.OUTSIDE));
            current.addForwardNode(start);
        }
        
        homeEntrance = new LudoNode(team, LudoNodeType.HOME_ENTRANCE);
        homeEntrance.addForwardNode(start);
        
        for(int i = 0; i < HOUSES_PER_CORNER; i++)
        {
            home.add(current = new LudoNode(team, LudoNodeType.HOME));
            
            if(previous != null)
            {
                previous.addForwardNode(current);
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
            allNodes.add(last = new Node());
            
            previous.addForwardNode(last);
            
            previous = last;
        }
        
        consumer.forBoardCorner(outside, start, homeEntrance, home, last, allNodes);
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
    
    public List<Node> getFullCornerNodes(int team)
    {
        return fullCorner[team];
    }
    
    public interface BoardCornerConsumer
    {
        void forBoardCorner(List<LudoNode> outside, LudoNode start, LudoNode homeEntrance, List<LudoNode> home, Node last, List<Node> allNodes);
    }
}
