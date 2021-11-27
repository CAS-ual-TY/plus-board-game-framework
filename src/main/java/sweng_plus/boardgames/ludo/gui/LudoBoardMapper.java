package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gamelogic.LudoNodeType;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.gui.BoardWidgetMapper;
import sweng_plus.framework.boardgame.nodes_board.Node;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LudoBoardMapper
{
    public static final int NODE_WIDTH = 80;
    public static final double SQRT2 = Math.sqrt(2);
    public static final double HALF_SQRT3 = Math.sqrt(3) * 0.5D;
    
    @SuppressWarnings("unchecked")
    public static HashMap<INode, LudoNodeWidget> mapLudoBoard(Screen screen, LudoBoard board, Texture texture)
    {
        int teams = board.getTeamsAmount();
        
        HashMap<INode, LudoNodeWidget> fullMap = new HashMap<>();
        
        MapFunction function = createMapFunction(teams,
                LudoBoard.HOUSES_PER_CORNER, LudoBoard.NEUTRAL_NODES_PER_CORNER);
        
        java.util.function.Function<INode, LudoNodeWidget> widgetFactory = (node) ->
        {
            if(node instanceof LudoNode ludoNode)
            {
                int team = board.getTeamIndex(ludoNode.getColor());
                Coordinates coords = function.map(ludoNode, team);
                
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
            
            System.out.println("Mapper: Mapping team: " + team);
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
    
    public static MapFunction createMapFunction(final int teams, int houses, int neutrals)
    {
        Function[] functions = new Function[teams];
        Coordinates[][] offsets = new Coordinates[teams][3];
    
        Coordinates[] corners = new Coordinates[teams];
    
        final int defaultWidth = NODE_WIDTH;
        int defaultHalfWidth = defaultWidth / 2;
    
        if(teams == 3)
        {
            int height = halfSqrt3(defaultWidth);
            int halfHeight = height / 2;
    
            corners[0] = new Coordinates(-defaultHalfWidth, -halfHeight); // Top Left
            corners[1] = new Coordinates(defaultWidth - defaultHalfWidth, -halfHeight); // Top Right
            corners[2] = new Coordinates(0, height - halfHeight); // Bottom
    
            functions[0] = (i) -> new Coordinates(0, -i * defaultWidth); // Top
    
            functions[1] = (i) ->
            {
                int distance = i * defaultWidth;
                //return new Coordinates(distance - distance / 2, halfSqrt3(distance));
                return new Coordinates(halfSqrt3(distance), distance - distance / 2);
            }; // Bottom Right
    
            functions[2] = (i) ->
            {
                int distance = i * defaultWidth;
                //return new Coordinates(- distance / 2, halfSqrt3(distance));
                return new Coordinates(-halfSqrt3(distance), distance - distance / 2);
            }; // Bottom Left
        }
        else if(teams == 4)
        {
            corners[0] = new Coordinates(-defaultHalfWidth, -defaultHalfWidth); // Top Left
            corners[1] = new Coordinates(defaultWidth-defaultHalfWidth, -defaultHalfWidth); // Top Right
            corners[2] = new Coordinates(defaultWidth-defaultHalfWidth, defaultWidth-defaultHalfWidth); // Bottom Right
            corners[3] = new Coordinates(-defaultHalfWidth, defaultWidth-defaultHalfWidth); // Bottom Left
    
            functions[0] = (i) -> new Coordinates(0, - i * defaultWidth); // Top
            functions[1] = (i) -> new Coordinates(i * defaultWidth, 0); // Right
            functions[2] = (i) -> new Coordinates(0, i * defaultWidth); // Bottom
            functions[3] = (i) -> new Coordinates(- i * defaultWidth, 0); // Left
        }
        
        Coordinates halfW = new Coordinates(-defaultHalfWidth, -defaultHalfWidth);
        
        for(int i = 0; i < corners.length; ++i)
        {
            corners[i] = corners[i].add(halfW);
            corners[i] = corners[i].add(corners[i]);
        }
        
        for(int team = 0; team < teams; ++team)
        {
            int next = (team + 1) % teams;
    
            offsets[team][0] = corners[team];
            offsets[team][1] = corners[team].between(corners[next]);
            offsets[team][2] = corners[next];
        }
    
        Arrays.asList(offsets[0]).forEach(c -> System.out.println("  Coord: " + c));
        
        return (node, team) ->
        {
            int row;
            int i;
            
            switch(node.getNodeType())
            {
                case OUTSIDE:
                case START:
                    row = 2;
                    i = 4;
                    if(node.getNodeType() == LudoNodeType.OUTSIDE)
                    {
                        Coordinates offset = offsets[team][row];
                        Function function = functions[team];
                        
                        i -= node.getIndex() / 2;
                        offset = offset.add(functions[team].map(i));
                        
                        i = node.getIndex() == 0 || node.getIndex() == 3 ? 3 : 4;
                        offset = offset.add(functions[(team + 1) % teams].map(i));
                        
                        return offset;
                    }
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
            
            Coordinates offset = offsets[team][row];
            Function function = functions[team];
            
            return offset.add(function.map(i));
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
    
    public interface MapFunction
    {
        Coordinates map(LudoNode node, int team);
    }
    
    public interface Function
    {
        Coordinates map(int i);
    }
    
    public interface BoardCornerWidgetConsumer
    {
        void forBoardCorner(List<LudoNode> outside, LudoNode start, LudoNode homeEntrance, List<LudoNode> home, Node last, List<Node> allNodes);
    }
    
    public record Coordinates(int x, int y)
    {
        public Coordinates between(Coordinates coords)
        {
            return new Coordinates((x() + coords.x()) / 2, (y() + coords.y()) / 2);
        }
    
        public Coordinates add(Coordinates coords)
        {
            return new Coordinates(x() + coords.x(), y() + coords.y());
        }
    
        @Override
        public String toString()
        {
            return "Coordinates{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
