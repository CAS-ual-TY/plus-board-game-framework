package sweng_plus.boardgames.mill.gui.util;

import org.joml.Vector2i;
import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gamelogic.MillBoard;
import sweng_plus.boardgames.mill.gamelogic.MillFigure;
import sweng_plus.boardgames.mill.gamelogic.MillNode;
import sweng_plus.boardgames.mill.gui.MillNodeStyle;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.style.ColoredQuadStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MillBoardMapper
{
    public static Widget[] createWidgetsAllNodes(BiFunction<Dimensions, MillNode, Widget> function, MillBoard board, int size)
    {
        return createWidgets(function, board, board::getAllNodes, size);
    }
    
    public static Widget[] createWidgets(BiFunction<Dimensions, MillNode, Widget> function, MillBoard board, Supplier<List<MillNode>> nodeSupplier, int size)
    {
        Widget[] widgets = new Widget[nodeSupplier.get().size()];
        Vector2i vector;
        for(int i = 0; i < nodeSupplier.get().size(); i++)
        {
            vector = getCoordsForIndex(nodeSupplier.get().get(i).getIndex()).mul(size);
            widgets[i] = function.apply(new Dimensions(64, 64, AnchorPoint.M, vector.x(), vector.y()), nodeSupplier.get().get(i));
        }
        
        
        return widgets;
    }
    
    public static Widget[] createSimpleWidgets(IScreenHolder screenHolder, MillBoard board, int size)
    {
        return createWidgetsAllNodes((dimensions, node) -> new SimpleWidget(screenHolder, dimensions, new MillNodeStyle(node)), board, size);
    }
    
    public static Widget[] createMovableTeamButtonWidgets(IScreenHolder screenHolder, MillBoard board, int size, Consumer<MillNode> consumer, TeamColor team)
    {
        return createWidgets((dimensions, node) ->
                        new FunctionalButtonWidget(screenHolder, dimensions, new HoverStyle(new ColoredQuadStyle(Color4f.HALF_VISIBLE), new ColoredQuadStyle(Color4f.WHITE)), () -> consumer.accept(node))
                , board, () -> Mill.instance().getGameLogic().getMovableFigures().keySet().stream().map(MillFigure::getCurrentNode).collect(Collectors.toList()), size);
    }
    
    public static Widget[] createTakeableTeamButtonWidgets(IScreenHolder screenHolder, MillBoard board, int size, Consumer<MillFigure> consumer, TeamColor team)
    {
        return createWidgets((dimensions, node) ->
                        new FunctionalButtonWidget(screenHolder, dimensions, new HoverStyle(new ColoredQuadStyle(Color4f.HALF_VISIBLE), new ColoredQuadStyle(Color4f.WHITE)), () -> consumer.accept(node.getFigures().get(0)))
                , board, () -> Mill.instance().getGameLogic().getTakeableFigure().stream().map(MillFigure::getCurrentNode).collect(Collectors.toList()), size);
    }
    
    public static Widget[] createNodeButtonWidgets(IScreenHolder screenHolder, MillBoard board, int size, Consumer<MillNode> consumer, MillFigure figure)
    {
        return createWidgets((dimensions, node) ->
                        new FunctionalButtonWidget(screenHolder, dimensions, new HoverStyle(new ColoredQuadStyle(Color4f.HALF_VISIBLE), new ColoredQuadStyle(Color4f.WHITE)), () -> consumer.accept(node))
                , board, () -> Mill.instance().getGameLogic().getMovableFigures().get(figure), size);
    }
    
    public static Vector2i getCoordsForIndex(int index)
    {
        switch(index)
        {
            case 0:
                return new Vector2i(-3, -3);
            case 1:
                return new Vector2i(0, -3);
            case 2:
                return new Vector2i(3, -3);
            case 3:
                return new Vector2i(3, 0);
            case 4:
                return new Vector2i(3, 3);
            case 5:
                return new Vector2i(0, 3);
            case 6:
                return new Vector2i(-3, 3);
            case 7:
                return new Vector2i(-3, 0);
            case 8:
                return new Vector2i(-2, -2);
            case 9:
                return new Vector2i(0, -2);
            case 10:
                return new Vector2i(2, -2);
            case 11:
                return new Vector2i(2, 0);
            case 12:
                return new Vector2i(2, 2);
            case 13:
                return new Vector2i(0, 2);
            case 14:
                return new Vector2i(-2, 2);
            case 15:
                return new Vector2i(-2, 0);
            case 16:
                return new Vector2i(-1, -1);
            case 17:
                return new Vector2i(0, -1);
            case 18:
                return new Vector2i(1, -1);
            case 19:
                return new Vector2i(1, 0);
            case 20:
                return new Vector2i(1, 1);
            case 21:
                return new Vector2i(0, 1);
            case 22:
                return new Vector2i(-1, 1);
            case 23:
                return new Vector2i(-1, 0);
            default:
            {
                final int outsideNodesPerColumn = 3;
                int numFieldNodes = MillBoard.NODES_PER_CIRCLE * MillBoard.NUM_CIRCLES;
                int outsideNodeIndex = (index - numFieldNodes) % MillBoard.FIGURES_PER_TEAM;
                int xPos;
                int yPos = (outsideNodeIndex % outsideNodesPerColumn) - outsideNodesPerColumn / 2;
                
                if(index < numFieldNodes + MillBoard.FIGURES_PER_TEAM)
                {
                    xPos = -4 - outsideNodeIndex / outsideNodesPerColumn;
                }
                else if(index < numFieldNodes + 2 * MillBoard.FIGURES_PER_TEAM)
                {
                    xPos = 4 + outsideNodeIndex / outsideNodesPerColumn;
                }
                else
                {
                    xPos = 0;
                    yPos = 0;
                }
                return new Vector2i(xPos, yPos);
            }
        }
    }
}
