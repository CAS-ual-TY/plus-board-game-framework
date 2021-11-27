package sweng_plus.framework.boardgame.gui;

import sweng_plus.framework.boardgame.gui.widget.NodeWidget;
import sweng_plus.framework.boardgame.nodes_board.NodeBoard;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.widget.base.IWidget;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BoardWidgetMapper
{
    public static HashMap<INode, ? extends NodeWidget> mapBoardToWidgets(NodeBoard board, Function<INode, NodeWidget> widgetFactory)
    {
        return mapListToWidgets(board.getNodes(), widgetFactory);
    }
    
    public static HashMap<INode, ? extends NodeWidget> mapListToWidgets(List<? extends INode> nodes, Function<INode, ? extends NodeWidget> widgetFactory)
    {
        HashMap<INode, NodeWidget> map = new HashMap<>(nodes.size());
        
        for(INode node : nodes)
        {
            map.put(node, widgetFactory.apply(node));
        }
        
        List<NodeWidget> nodesLinked = new LinkedList<>();
        
        for(NodeWidget node : map.values())
        {
            System.out.println("Doing for: " + node);
            linkNodeWidgets(node, map, nodesLinked);
        }
        
        if(nodesLinked.size() != map.values().size())
        {
            System.out.println(nodesLinked.size());
            nodesLinked.stream().map(n -> n.getNode()).forEach(System.out::println);
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
