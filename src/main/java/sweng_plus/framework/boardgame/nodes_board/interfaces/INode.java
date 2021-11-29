package sweng_plus.framework.boardgame.nodes_board.interfaces;

import sweng_plus.framework.boardgame.nodes_board.NodeFigure;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface INode
{
    List<INode> getForwardNodes();
    
    default List<INode> getForwardNodes(Predicate<INode> predicate)
    {
        return getForwardNodes().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    default List<INode> getDistantForwardNodes(int distance, Predicate<INode> predicate)
    {
        return getDistantNodes(distance, predicate, this, INode::getForwardNodes);
    }
    
    void addForwardNode(INode forwardNode);
    
    default void addForwardNodes(List<INode> forwardNodes)
    {
        forwardNodes.forEach(this::addForwardNode);
    }
    
    List<INode> getBackwardNodes();
    
    void addBackwardNode(INode backwardNode);
    
    default void addBackwardNodes(List<INode> backwardNodes)
    {
        backwardNodes.forEach(this::addBackwardNode);
    }
    
    default List<INode> getBackwardNodes(Predicate<INode> predicate)
    {
        return getBackwardNodes().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    default List<INode> getDistantBackwardNodes(int distance, Predicate<INode> predicate)
    {
        return getDistantNodes(distance, predicate, this, INode::getBackwardNodes);
    }
    
    List<NodeFigure> getNodeFigures();
    
    void addNodeFigure(NodeFigure fieldFigure);
    
    default void addNodeFigures(List<NodeFigure> fieldFigures)
    {
        fieldFigures.forEach(this::addNodeFigure);
    }
    
    void removeNodeFigure(NodeFigure fieldFigure);
    
    default boolean isOccupied() {return getNodeFigures().size() > 0;}
    
    static List<INode> getDistantNodes(int distance, Predicate<INode> predicate, INode currentNode, BiFunction<INode, Predicate<INode>, List<INode>> function)
    {
        if(distance < 1)
        {
            throw new RuntimeException("Distance of forward fields must be greater than 0");
        }
        else if(distance == 1)
        {
            return function.apply(currentNode, predicate);
        }
        else
        {
            return function.apply(currentNode, (x) -> true)
                    .stream()
                    .filter(predicate)
                    .map(node -> getDistantNodes(distance - 1, predicate, node, function))
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList());
        }
    }
    
    static void linkNodes(INode from, INode to)
    {
        from.addForwardNode(to);
        to.addBackwardNode(from);
    }
}
