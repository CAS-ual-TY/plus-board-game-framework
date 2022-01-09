package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.NodeBoard;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MillBoard extends NodeBoard<MillNode, MillFigure>
{
    public static final int FIGURES_PER_TEAM = 9;
    public static final int MAX_FIGURES_TO_JUMP = 3;
    public static final int NUM_CIRCLES = 3;
    public static final int NODES_PER_CIRCLE = 8;
    
    private MillTeam[] millTeams;
    private List<MillNode> fieldNodes;
    
    public MillBoard(TeamColor[] teamColors)
    {
        super(new LinkedList<>(), new LinkedList<>());
        
        setupBoard(teamColors);
        
    }
    
    @Override
    public void moveFigure(MillFigure figure, MillNode target)
    {
        super.moveFigure(figure, target);
        figure.setAlreadyPlaced(true);
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
    
    public TeamColor getTeamFromIndex(int teamIndex)
    {
        if(teamIndex > 0 && teamIndex < millTeams.length) {
            return millTeams[teamIndex].color();
        }
        return null;
    }
    
    public List<MillFigure> getActiveTeamFigures(TeamColor team)
    {
        return millTeams[getTeamIndex(team)].activeFigures();
    }
    
    public List<MillFigure> getTakenTeamFigures(TeamColor team)
    {
        return millTeams[getTeamIndex(team)].takenFigures();
    }
    
    public MillNode takeFigure(TeamColor team, MillFigure figure)
    {
        MillNode outsideNode = null;
        if(team.equals(figure.getTeam()))
        {
            getActiveTeamFigures(team).remove(figure);
            millTeams[getTeamIndex(team)].takenFigures().add(figure);
    
            outsideNode = getFreeOutsideNode(team);
            moveFigure(figure, outsideNode);
        }
        return outsideNode;
    }
    public MillNode takeFigure(MillFigure figure)
    {
        return takeFigure(figure.getTeam(), figure);
    }
    
    public List<MillNode> getFieldNodes() {
        return fieldNodes;
    }
    
    public List<MillNode> getFreeFieldNodes() {
        return fieldNodes.stream().filter(Predicate.not(MillNode::isOccupied)).collect(Collectors.toList());
    }
    
    public MillNode getFieldNode(int index) {
        return fieldNodes.get(index);
    }
    
    private MillNode getFreeOutsideNode(TeamColor team) {
        for(MillNode outsideNode : millTeams[getTeamIndex(team)].outsideNodes())
        {
            if(!outsideNode.isOccupied())
            {
                return outsideNode;
            }
        }
        return null;
    }
    
    
    private void setupBoard(TeamColor[] teamColors)
    {
        
        millTeams = new MillBoard.MillTeam[teamColors.length];
        fieldNodes = new ArrayList<>(NUM_CIRCLES*NODES_PER_CIRCLE);
        
        // create 3 non-connected circles
        for(int i = 0; i < NUM_CIRCLES; i++) {
            fieldNodes.addAll(createCircle(i));
        }
        // create connections between circles - every second node is connected
        for(int i = 1; i < fieldNodes.size(); i+=2)
        {
            INode.linkNodes(fieldNodes.get(i), fieldNodes.get((i+NODES_PER_CIRCLE)% fieldNodes.size()));
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
            for(MillNode node : fieldNodes)
            {
                outsideNode.addForwardNode(node);
            }
        }
    }
    
    private List<MillNode> createCircle(int circleNumber)
    {
        List<MillNode> circleNodes = new ArrayList<>(NODES_PER_CIRCLE);
        for(int i = 0; i < NODES_PER_CIRCLE; i++) {
            circleNodes.add(new MillNode(TeamColor.NEUTRAL, i + circleNumber*NODES_PER_CIRCLE));
            
            if(i > 0)
            {
                linkNodes(circleNodes.get(i-1), circleNodes.get(i));
            }
        }
        linkNodes(circleNodes.get(circleNodes.size()-1), circleNodes.get(0));
        
        addNodes(circleNodes);
        return circleNodes;
    }
    
    private MillTeam createMillTeam(TeamColor team)
    {
        MillNode[] outsideNodes = new MillNode[FIGURES_PER_TEAM];
        List<MillFigure> figures = new ArrayList<>(FIGURES_PER_TEAM);
        for(int i = 0; i < FIGURES_PER_TEAM; i++) {
            figures.add(new MillFigure(team, i));
            outsideNodes[i] = new MillNode(team, i);
        }
        
        addNodes(List.of(outsideNodes));
        addFigures(figures);
    
        for(int i = 0; i < FIGURES_PER_TEAM; i++) {
            placeFigure(figures.get(i), outsideNodes[i]);
        }
        return new MillTeam(team, figures, new LinkedList<>(), outsideNodes);
    }
    
    
    private record MillTeam(TeamColor color, List<MillFigure> activeFigures, List<MillFigure> takenFigures, MillNode[] outsideNodes)
    {
    }
}


