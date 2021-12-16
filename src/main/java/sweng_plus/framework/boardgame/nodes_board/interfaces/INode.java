package sweng_plus.framework.boardgame.nodes_board.interfaces;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface INode<F extends INodeFigure<N, F>, N extends INode<F, N>>
{
    List<N> getForwardNodes();
    
    default List<N> getForwardNodes(Predicate<N> predicate)
    {
        return getForwardNodes().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    default List<N> getDistantForwardNodes(int distance, Predicate<N> predicate)
    {
        return getDistantNodes(distance, predicate, this, INode::getForwardNodes);
    }
    
    void addForwardNode(N node);
    
    default void addForwardNodes(List<N> nodes)
    {
        nodes.forEach(this::addForwardNode);
    }
    
    void removeForwardNode(N node);
    
    List<N> getBackwardNodes();
    
    void addBackwardNode(N node);
    
    default void addBackwardNodes(List<N> nodes)
    {
        nodes.forEach(this::addBackwardNode);
    }
    
    void removeBackwardNode(N node);
    
    default List<N> getBackwardNodes(Predicate<N> predicate)
    {
        return getBackwardNodes().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    default List<N> getDistantBackwardNodes(int distance, Predicate<N> predicate)
    {
        return getDistantNodes(distance, predicate, this, INode::getBackwardNodes);
    }
    
    List<F> getFigures();
    
    void addFigure(F figure);
    
    default void addFigures(List<F> figures)
    {
        figures.forEach(this::addFigure);
    }
    
    void removeFigure(F figure);
    
    default boolean isOccupied()
    {
        return getFigures().size() > 0;
    }
    
    static <F extends INodeFigure<N, F>, N extends INode<F, N>> List<N> getDistantNodes(int distance, Predicate<N> predicate, INode<F, N> currentNode, BiFunction<INode<F, N>, Predicate<N>, List<N>> function)
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
    
    static <F extends INodeFigure<N, F>, N extends INode<F, N>> void linkNodes(N from, N to)
    {
        from.addForwardNode(to);
        to.addBackwardNode(from);
    }
    
    static <F extends INodeFigure<N, F>, N extends INode<F, N>> void unlinkNodes(N from, N to)
    {
        from.removeForwardNode(to);
        to.removeBackwardNode(from);
    }
}
