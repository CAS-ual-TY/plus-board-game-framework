package sweng_plus.framework.boardgame.gui;

import sweng_plus.framework.boardgame.gui.widget.NodeWidget;
import sweng_plus.framework.boardgame.nodes_board.NodeBoard;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.widget.base.IWidget;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class BoardWidgetMapper
{
    public static HashMap<INode, NodeWidget> mapBoardToWidgets(NodeBoard board, Function<INode, NodeWidget> widgetFactory)
    {
        return mapListToWidgets(board.getNodes(), widgetFactory);
    }
    
    public static HashMap<INode, NodeWidget> mapListToWidgets(List<INode> nodes, Function<INode, NodeWidget> widgetFactory)
    {
        HashMap<INode, NodeWidget> map = new HashMap<>(nodes.size());
        
        for(INode node : nodes)
        {
            map.put(node, widgetFactory.apply(node));
        }
        
        List<NodeWidget> nodesLinked = new LinkedList<>();
        
        for(NodeWidget node : map.values())
        {
            linkNodeWidgets(node, map, nodesLinked);
        }
        
        if(!nodesLinked.isEmpty())
        {
            throw new IllegalStateException("Something went wrong!"); //TODO different exception?
        }
        
        return map;
    }
    
    public static void linkNodeWidgets(NodeWidget widget, HashMap<INode, NodeWidget> map, List<NodeWidget> nodesLinked)
    {
        if(nodesLinked.contains(widget))
        {
            return;
        }
        
        nodesLinked.add(widget);
        
        widget.getNode().getForwardNodes().stream().map(map::get).forEach(w ->
        {
            widget.addForwardNode(w);
            linkNodeWidgets(w, map, nodesLinked);
        });
        
        widget.getNode().getBackwardNodes().stream().map(map::get).forEach(w ->
        {
            widget.addBackwardNode(w);
            linkNodeWidgets(w, map, nodesLinked);
        });
    }
}
