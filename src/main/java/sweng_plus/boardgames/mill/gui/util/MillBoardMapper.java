package sweng_plus.boardgames.mill.gui.util;

import org.joml.Vector2i;
import sweng_plus.boardgames.mill.gamelogic.MillBoard;
import sweng_plus.boardgames.mill.gamelogic.MillNode;
import sweng_plus.boardgames.mill.gui.MillNodeStyle;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.function.BiFunction;
import java.util.function.IntConsumer;

public class MillBoardMapper
{
    public static Widget[] createWidgets(BiFunction<Dimensions, MillNode, Widget> function, MillBoard board, int size)
    {
        Widget[] widgets = new Widget[board.getFieldNodes().size()];
        Vector2i vector;
        for(MillNode node : board.getFieldNodes())
        {
            vector = getCoordsForIndex(node.getIndex()).mul(size);
            widgets[node.getIndex()] = function.apply(new Dimensions(AnchorPoint.M, vector.x(), vector.y()), node);
        }
        
        return widgets;
    }
    
    public static Widget[] createSimpleWidgets(IScreenHolder screenHolder, MillBoard board, int size)
    {
        return createWidgets((dimensions, node) -> new SimpleWidget(screenHolder, dimensions, new MillNodeStyle(node)), board, size);
    }
    
    public static Widget[] createButtonWidgets(IScreenHolder screenHolder, MillBoard board, int size, IntConsumer consumer, TeamColor team)
    {
        return createWidgets((dimensions, node) ->
        {
            if(node.getTeam() == team)
            {
                return new FunctionalButtonWidget(screenHolder, dimensions, new MillNodeStyle(node), () -> consumer.accept(node.getIndex()));
            }
            else
            {
                return new SimpleWidget(screenHolder, dimensions, new MillNodeStyle(node));
            }
        }, board, size);
    }
    
    public static Vector2i getCoordsForIndex(int index)
    {
        return switch(index)
                {
                    case 0 -> new Vector2i(-3, -3);
                    case 1 -> new Vector2i(0, -3);
                    case 2 -> new Vector2i(3, -3);
                    case 3 -> new Vector2i(3, 0);
                    case 4 -> new Vector2i(3, 3);
                    case 5 -> new Vector2i(0, 3);
                    case 6 -> new Vector2i(-3, 3);
                    case 7 -> new Vector2i(-3, 0);
                    case 8 -> new Vector2i(-2, -2);
                    case 9 -> new Vector2i(0, -2);
                    case 10 -> new Vector2i(2, -2);
                    case 11 -> new Vector2i(2, 0);
                    case 12 -> new Vector2i(2, 2);
                    case 13 -> new Vector2i(0, 2);
                    case 14 -> new Vector2i(-2, 2);
                    case 15 -> new Vector2i(-2, 0);
                    case 16 -> new Vector2i(-1, -1);
                    case 17 -> new Vector2i(0, -1);
                    case 18 -> new Vector2i(1, -1);
                    case 19 -> new Vector2i(1, 0);
                    case 20 -> new Vector2i(1, 1);
                    case 21 -> new Vector2i(0, 1);
                    case 22 -> new Vector2i(-1, -1);
                    case 23 -> new Vector2i(-1, 0);
                    default -> new Vector2i(0, 0);
                };
    }
}
