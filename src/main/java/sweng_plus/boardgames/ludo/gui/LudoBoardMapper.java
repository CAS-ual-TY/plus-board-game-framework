package sweng_plus.boardgames.ludo.gui;

import org.joml.RoundingMode;
import org.joml.Vector2d;
import org.joml.Vector2i;
import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gamelogic.LudoNodeType;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.gui.BoardWidgetMapper;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.util.HashMap;

public class LudoBoardMapper
{
    public static final int NODE_WIDTH = 72;
    public static final double SQRT2 = Math.sqrt(2);
    public static final double HALF_SQRT3 = Math.sqrt(3) * 0.5D;
    
    @SuppressWarnings("unchecked")
    public static HashMap<INode, LudoNodeWidget> mapLudoBoard(Screen screen, LudoBoard board, Texture texture)
    {
        int teams = board.getTeamsAmount();
        
        HashMap<INode, LudoNodeWidget> fullMap = new HashMap<>();
        
        PositionFunction function = createMapFunction(teams,
                LudoBoard.HOUSES_PER_CORNER, LudoBoard.NEUTRAL_NODES_PER_CORNER);
        
        java.util.function.Function<INode, LudoNodeWidget> widgetFactory = (node) ->
        {
            if(node instanceof LudoNode ludoNode)
            {
                int team = board.getTeamIndex(ludoNode.getColor());
                Vector2i coords = function.map(ludoNode, team);
                
                return new LudoNodeWidget(screen.getScreenHolder(),
                        new Dimensions(NODE_WIDTH, NODE_WIDTH, AnchorPoint.M, coords.x(), coords.y()), ludoNode, texture);
            }
            else
            {
                throw new IllegalArgumentException();
            }
        };
        
        HashMap<INode, LudoNodeWidget>[] maps = new HashMap[board.getTeamsAmount()];
        
        for(int team = 0; team < teams; ++team)
        {
            maps[team] = (HashMap<INode, LudoNodeWidget>)
                    BoardWidgetMapper.mapListToWidgets(board.getFullCornerNodes(team), widgetFactory);
            fullMap.putAll(maps[team]);
        }
        
        LudoNodeWidget homeEntrance;
        
        for(int to = 0; to < teams; ++to)
        {
            int from = (to - 1 + teams) % teams;
            
            homeEntrance = maps[to].get(board.getHomeEntranceNode(to));
            homeEntrance.getNode().getBackwardNodes().stream()
                    .map(n -> maps[from].get(n)).forEach(homeEntrance::addBackwardNode);
        }
        
        return fullMap;
    }
    
    public static PositionFunction createMapFunction(final int teams, int houses, int neutrals)
    {
        Vector2i[] functions = new Vector2i[teams];
        Vector2i[][] offsets = new Vector2i[teams][3];
        
        Vector2i[] corners = new Vector2i[teams];
        
        int outsideDistance = 3;
        
        final int defaultWidth = NODE_WIDTH;
        int defaultHalfWidth = defaultWidth / 2;
        
        if(teams == 2)
        {
            int height = halfSqrt3(defaultWidth);
            int halfHeight = height / 2;
            
            corners[0] = new Vector2i(-defaultHalfWidth, -halfHeight); // Top Left
            corners[1] = new Vector2i(defaultWidth - defaultHalfWidth, -halfHeight); // Top Right
            corners[2] = new Vector2i(0, height - halfHeight); // Bottom
            
            functions[0] = new Vector2i(0, -defaultWidth); // Top
            functions[1] = new Vector2i(halfSqrt3(defaultWidth), defaultWidth - defaultHalfWidth); // Bottom Right
            functions[2] = new Vector2i(-halfSqrt3(defaultWidth), defaultWidth - defaultHalfWidth); // Bottom Left
        }
        if(teams <= 4)
            ;
        else if(teams <= 6)
            outsideDistance -= 1;
        else
            outsideDistance -= 2;
        
        double angle = -2D * Math.PI / teams;
        double radius = 0.5D * defaultWidth / Math.sin(Math.PI / teams);
        
        for(int team = 0; team < teams; ++team)
        {
            double functionAngle = team * angle;
            double cornerAngle = functionAngle - 0.5D * angle;
            corners[team] = new Vector2d(-Math.sin(cornerAngle), -Math.cos(cornerAngle)).normalize()
                    .mul(radius).get(RoundingMode.HALF_UP, new Vector2i());
            functions[team] = new Vector2d(-Math.sin(functionAngle), -Math.cos(functionAngle)).normalize()
                    .mul(defaultWidth).get(RoundingMode.HALF_UP, new Vector2i());
        }
        
        for(Vector2i corner : corners)
        {
            corner.mul(2);
        }
        
        for(int team = 0; team < teams; ++team)
        {
            int next = (team + 1) % teams;
            
            offsets[team][0] = new Vector2i(corners[team]);
            offsets[team][1] = new Vector2i(corners[team]).add(corners[next]).div(2);
            offsets[team][2] = new Vector2i(corners[next]);
        }
        
        final int finalOutsideDistance = outsideDistance;
        return (node, team) ->
        {
            int row;
            int i;
            
            switch(node.getNodeType())
            {
                case OUTSIDE:
                    if(node.getNodeType() == LudoNodeType.OUTSIDE)
                    {
                        Vector2i offset = new Vector2i(offsets[team][2]);
                        offset.add(new Vector2i(functions[team]).mul(finalOutsideDistance + node.getIndex() / 2));
                        offset.add(new Vector2i(functions[(team + 1) % teams])
                                .mul(finalOutsideDistance + (node.getIndex() == 0 || node.getIndex() == 3 ? 0 : 1)));
                        
                        return offset;
                    }
                
                case START:
                    row = 2;
                    i = 4;
                    break;
                
                case HOME_ENTRANCE:
                    row = 1;
                    i = 4;
                    break;
                
                case HOME:
                    row = 1;
                    i = 3 - node.getIndex();
                    break;
                
                default: //Neutral
                    if(node.getIndex() < 4)
                    {
                        row = 2;
                        i = 3 - node.getIndex();
                    }
                    else
                    {
                        row = 0;
                        team = (team + 1) % teams;
                        i = node.getIndex() - 3;
                    }
                    break;
            }
            
            Vector2i offset = new Vector2i(offsets[team][row]);
            Vector2i function = new Vector2i(functions[team]);
            
            return offset.add(function.mul(i));
        };
    }
    
    public static int sqrt2(int x)
    {
        return (int) Math.round(SQRT2 * x);
    }
    
    public static int halfSqrt3(int x)
    {
        return (int) Math.round(HALF_SQRT3 * x);
    }
    
    public interface PositionFunction
    {
        Vector2i map(LudoNode node, int team);
    }
}
