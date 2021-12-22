package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.NodeBoard;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MillBoard extends NodeBoard<MillNode, MillFigure>
{
    private static final int FIGURES_PER_TEAM = 9;
    private static final int MAX_FIGURES_TO_JUMP = 3;
    private final int NUM_CIRCLES = 3;
    private final int NODES_PER_CIRCLE = 8;
    
    private MillTeam[] millTeams;
    private MillNode[][] nodeCircles;
    
    public MillBoard(TeamColor[] teamColors)
    {
        super(new LinkedList<>(), new LinkedList<>());
        
        setupBoard(teamColors);
        
    }
    
    public int getTeamIndex(TeamColor team)
    {
        for(int i = 0; i < millTeams.length; ++i)
        {
            if(team == millTeams[i].color())
            {
                return i;
            }
        }
        
        return -1;
    }
    
    public MillFigure[] getTeamFigures(TeamColor team)
    {
        for(MillBoard.MillTeam value : millTeams)
        {
            if(team == value.color())
            {
                return value.figures();
            }
        }
        
        return null;
    }
    
    public MillNode[][] getNodeCircles()
    {
        return nodeCircles;
    }
    
    private void setupBoard(TeamColor[] teamColors)
    {
        
        millTeams = new MillBoard.MillTeam[teamColors.length];
        nodeCircles = new MillNode[3][];
        
        // create 3 non-connected circles
        for(int i = 0; i < NUM_CIRCLES; i++) {
            nodeCircles[i]=createCircle();
        }
        // create connections between circles - every second node is connected
        for(int i = 1; i < NODES_PER_CIRCLE; i+=2)
        {
            for(int j = 0; j < NUM_CIRCLES; j++) {
                INode.linkNodes(nodeCircles[j][i], nodeCircles[(j+1)%NUM_CIRCLES ][i]);
            }
        }
    
        // create both Teams
        List<MillNode> outsideNodes = new ArrayList<>(FIGURES_PER_TEAM*millTeams.length);
        for(int i = 0; i < millTeams.length; i++)
        {
            millTeams[i] = createMillTeam(teamColors[i]);
            outsideNodes.addAll(List.of(millTeams[i].outsideNodes()));
        }
    
        // Connect outsideNodes of Teams with every node in the field
        for(MillNode outsideNode : outsideNodes)
        {
            for(int i = 0; i < NODES_PER_CIRCLE; i++)
            {
                for(MillNode[] nodeCircle : nodeCircles)
                {
                    outsideNode.addForwardNode(nodeCircle[i]);
                }
            }
        }
    }
    
    private MillNode[] createCircle()
    {
        MillNode[] circleNodes = new MillNode[NODES_PER_CIRCLE];
        for(int i = 0; i < NODES_PER_CIRCLE; i++) {
            circleNodes[i] = new MillNode(TeamColor.NEUTRAL, i);
            
            if(i > 0)
            {
                linkNodes(circleNodes[i-1], circleNodes[i]);
            }
        }
        linkNodes(circleNodes[NODES_PER_CIRCLE-1], circleNodes[0]);
        
        addNodes(List.of(circleNodes));
        return circleNodes;
    }
    
    private MillTeam createMillTeam(TeamColor team)
    {
        MillNode[] outsideNodes = new MillNode[FIGURES_PER_TEAM];
        MillFigure[] figures = new MillFigure[FIGURES_PER_TEAM];
        for(int i = 0; i < FIGURES_PER_TEAM; i++) {
            figures[i] = new MillFigure(team, i);
            outsideNodes[i] = new MillNode(team, i);
        }
        
        addNodes(List.of(outsideNodes));
        addFigures(List.of(figures));
    
        for(int i = 0; i < FIGURES_PER_TEAM; i++) {
            placeFigure(figures[i], outsideNodes[i]);
        }
        return new MillTeam(team, figures, outsideNodes);
    }
    
    
    private record MillTeam(TeamColor color, MillFigure[] figures, MillNode[] outsideNodes)
    {
    }
    
    
    
}


