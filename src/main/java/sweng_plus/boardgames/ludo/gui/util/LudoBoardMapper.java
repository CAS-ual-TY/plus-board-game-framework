package sweng_plus.boardgames.ludo.gui.util;

import org.joml.RoundingMode;
import org.joml.Vector2d;
import org.joml.Vector2i;
import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.gui.BoardWidgetMapper;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Dimensions;

import java.util.HashMap;

public class LudoBoardMapper
{
    public static final int NODE_WIDTH = 72;
    public static final double SQRT2 = Math.sqrt(2);
    public static final double HALF_SQRT3 = Math.sqrt(3) * 0.5D;
    
    @SuppressWarnings("unchecked")
    public static HashMap<LudoNode, LudoNodeWidget> mapLudoBoard(Screen screen, LudoBoard board, Texture nodeTexture, Texture figureTexture)
    {
        int teams = board.getTeamsAmount();
        
        HashMap<LudoNode, LudoNodeWidget> fullMap = new HashMap<>();
        
        PositionFunction function = createMapFunction(teams,
                LudoBoard.HOUSES_PER_CORNER, LudoBoard.NEUTRAL_NODES_PER_CORNER);
        
        java.util.function.Function<LudoNode, LudoNodeWidget> widgetFactory = (node) ->
        {
            int team = board.getTeamIndex(node.getTeam());
            Vector2i coords = function.map(node, team);
            
            return new LudoNodeWidget(screen.getScreenHolder(),
                    new Dimensions(NODE_WIDTH, NODE_WIDTH, AnchorPoint.M, coords.x(), coords.y()),
                    node, nodeTexture, figureTexture);
            
        };
        
        HashMap<LudoNode, LudoNodeWidget>[] maps = new HashMap[board.getTeamsAmount()];
        
        for(int team = 0; team < teams; ++team)
        {
            maps[team] = BoardWidgetMapper.mapListToWidgets(board.getFullCornerNodes(team), widgetFactory);
            fullMap.putAll(maps[team]);
        }
        
        for(int to = 0; to < teams; ++to)
        {
            int from = (to - 1 + teams) % teams;
            
            LudoNodeWidget homeEntrance = maps[to].get(board.getHomeEntranceNode(to));
            homeEntrance.getNode().getBackwardNodes().stream()
                    .map(fullMap::get).forEach(homeEntrance::addBackwardNode);
            homeEntrance.getNode().getBackwardNodes().stream()
                    .map(fullMap::get).forEach(n -> n.addForwardNode(homeEntrance));
        }
        
        return fullMap;
    }
    
    public static PositionFunction createMapFunction(final int teams, int houses, int neutrals)
    {
        Vector2i[] functions = new Vector2i[teams];
        Vector2i[][] offsets = new Vector2i[teams][3];
        
        Vector2i[] corners = new Vector2i[teams];
        
        int outsideDistance;
        
        final int defaultWidth = NODE_WIDTH;
        int defaultHalfWidth = defaultWidth / 2;
        
        if(teams == 2)
        {
            outsideDistance = 3;
            
            corners[0] = new Vector2d(-1, 1).normalize().mul(defaultHalfWidth)
                    .get(RoundingMode.HALF_UP, new Vector2i()); // Left
            corners[1] = new Vector2i(corners[0]).mul(-1); // Right
            
            functions[0] = new Vector2d(-1, -1).normalize().mul(defaultWidth)
                    .get(RoundingMode.HALF_UP, new Vector2i()); // Top
            functions[1] = new Vector2i(functions[0]).mul(-1); // Bottom
        }
        else
        {
            if(teams <= 4)
            {
                outsideDistance = 3;
            }
            else if(teams <= 6)
            {
                outsideDistance = 2;
            }
            else
            {
                outsideDistance = 1;
            }
            
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
        
        return createMapFunction(teams, offsets, functions, outsideDistance);
    }
    
    private static PositionFunction createMapFunction(int teams, Vector2i[][] offsets, Vector2i[] functions, int outsideDistance)
    {
        return (node, team) ->
        {
            int row;
            int i;
            
            switch(node.getNodeType())
            {
                case OUTSIDE:
                    if(teams == 2)
                    {
                        Vector2i offset = new Vector2i(offsets[team][2]);
                        Vector2i y = new Vector2i(functions[team]);
                        Vector2i x = new Vector2i(-y.y(), y.x());
                        offset.add(y.mul(outsideDistance + node.getIndex() / 2));
                        offset.add(x.mul(node.getIndex() == 0 || node.getIndex() == 3 ? 2 : 3));
                        
                        return offset;
                    }
                    else
                    {
                        Vector2i offset = new Vector2i(offsets[team][2]);
                        Vector2i y = new Vector2i(functions[team]);
                        Vector2i x = new Vector2i(functions[(team + 1) % teams]);
                        offset.add(y.mul(outsideDistance + node.getIndex() / 2));
                        offset.add(x.mul(outsideDistance + (node.getIndex() == 0 || node.getIndex() == 3 ? 0 : 1)));
                        
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
                    if(teams == 2 && node.getIndex() > 1)
                    {
                        i = 3 + node.getIndex();
                    }
                    else
                    {
                        i = 3 - node.getIndex();
                    }
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
