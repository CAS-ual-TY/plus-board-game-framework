package sweng_plus.framework.boardgame.gui;

import sweng_plus.framework.boardgame.gui.widget.NodeWidget;
import sweng_plus.framework.boardgame.nodes_board.NodeBoard;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeFigure;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BoardWidgetMapper
{
    public static <W extends NodeWidget<W, N, F>, N extends INode<F, N>, F extends INodeFigure<N, F>> HashMap<N, W> mapBoardToWidgets(NodeBoard<N, F> board, Function<N, W> widgetFactory)
    {
        return mapListToWidgets(board.getNodes(), widgetFactory);
    }
    
    public static <W extends NodeWidget<W, N, F>, N extends INode<F, N>, F extends INodeFigure<N, F>> HashMap<N, W> mapListToWidgets(List<N> nodes, Function<N, W> widgetFactory)
    {
        HashMap<N, W> map = new HashMap<>(nodes.size());
        
        for(N node : nodes)
        {
            map.put(node, widgetFactory.apply(node));
        }
        
        List<W> nodesLinked = new LinkedList<>();
        
        for(W node : map.values())
        {
            linkNodeWidgets(node, map, nodesLinked);
        }
        
        if(nodesLinked.size() != map.values().size())
        {
            throw new IllegalStateException("Something went wrong!"); //TODO different exception?
        }
        
        return map;
    }
    
    public static <W extends NodeWidget<W, N, F>, N extends INode<F, N>, F extends INodeFigure<N, F>> void linkNodeWidgets(W widget, HashMap<N, W> map, List<W> nodesLinked)
    {
        if(nodesLinked.contains(widget))
        {
            return;
        }
        
        nodesLinked.add(widget);
        
        widget.getNode().getForwardNodes().stream().map(map::get).filter(Objects::nonNull).forEach(w ->
        {
            widget.addForwardNode(w);
            linkNodeWidgets(w, map, nodesLinked);
        });
        
        widget.getNode().getBackwardNodes().stream().map(map::get).filter(Objects::nonNull).forEach(w ->
        {
            widget.addBackwardNode(w);
            linkNodeWidgets(w, map, nodesLinked);
        });
    }
}
