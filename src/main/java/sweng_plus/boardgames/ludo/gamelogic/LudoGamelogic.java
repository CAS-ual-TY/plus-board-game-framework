package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.Board;
import sweng_plus.framework.boardgame.nodes_board.Node;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.TeamNode;

import java.util.ArrayList;
import java.util.List;

public class LudoGamelogic
{
    private Board board;
    private TeamColor[] teams;
    List<LudoNode>[] outside;
    LudoNode[] start;
    LudoNode[] homeEntrance;
    List<LudoNode>[] home;
    
    
    public LudoGamelogic(TeamColor[] teams)
    {
    
    }
    
    public Board getBoard()
    {
        return board;
    }
    
    public void setBoard(Board board)
    {
        this.board = board;
    }
    
    private Board createBoard()
    {
        outside = new List[teams.length];
        start = new LudoNode[teams.length];
        homeEntrance = new LudoNode[teams.length];
        home = new List[teams.length];
        
        Node[] last = new Node[teams.length];
        
        for(int i = 0; i < teams.length; i++) {
            final int finalI = i;
            createBoardCorner(teams[i], (cornerOutside, cornerStart, cornerHomeEntrance, cornerHome, cornerLast) -> {
                outside[finalI] = cornerOutside;
                start[finalI] = cornerStart;
                homeEntrance[finalI] = cornerHomeEntrance;
                home[finalI] = cornerHome;
                last[finalI] = cornerLast;
            });
        }
        for(int i = 0; i < teams.length; i++) {
            last[i].addForwardNode(homeEntrance[(i+1)%teams.length]);
        }
        
        return null;
    }
    
    private static void createBoardCorner(TeamColor team, BoardCornerConsumer consumer)
    {
        int numFigures = 4;
        int numNeutralNodes = 8;
        
        ArrayList<LudoNode> outside = new ArrayList<>(numFigures);
        LudoNode start;
        LudoNode homeEntrance;
        ArrayList<LudoNode> home = new ArrayList<>(numFigures);
        Node last = null;
        LudoNode current = null;
        Node previous = null;

        
        start = new LudoNode(team, LudoNodeType.START);
    
        for(int i = 0; i < numFigures; i++)
        {
            outside.add(current = new LudoNode(team, LudoNodeType.OUTSIDE));
            current.addForwardNode(start);
        }
        
        
        homeEntrance = new LudoNode(team, LudoNodeType.HOME_ENTRANCE);
        homeEntrance.addForwardNode(start);
        
        
        for(int i = 0; i < numFigures; i++)
        {
            home.add(current = new LudoNode(team, LudoNodeType.HOME));
            
            if(previous != null)
            {
                previous.addForwardNode(current);
            }
            previous = current;
        }
        
        previous = start;
        for(int i = 0; i < numNeutralNodes; i++)
        {
            last = new Node();
            
            previous.addForwardNode(last);
            
            previous = last;
        }
        
        consumer.forBoardCorner(outside, start, homeEntrance, home, last);
    }
    
    public TeamColor[] getTeams()
    {
        return teams;
    }
    

    public interface BoardCornerConsumer
    {
        void forBoardCorner(List<LudoNode> outside, LudoNode start, LudoNode homeEntrance, List<LudoNode> home, Node last);
    }
 }
