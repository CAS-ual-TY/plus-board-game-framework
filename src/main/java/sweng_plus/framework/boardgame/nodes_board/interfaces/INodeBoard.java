package sweng_plus.framework.boardgame.nodes_board.interfaces;

import java.util.List;
import java.util.function.Predicate;

public interface INodeBoard<N extends INode<F, N>, F extends INodeFigure<N, F>>
{
    List<N> getNodes();
    
    List<F> getFigures();
    
    void addNode(N node);
    
    default void addNodes(List<N> nodes)
    {
        nodes.forEach(this::addNode);
    }
    
    void addFigure(F figure);
    
    default void addFigures(List<F> figures)
    {
        figures.forEach(this::addFigure);
    }
    
    boolean isNodeOnBoard(N node);
    
    boolean isFigureOnBoard(F figure);
    
    default List<N> getForwardNodes(N start, int distance, Predicate<N> predicate)
    {
        if(!isNodeOnBoard(start))
        {
            return List.of();
        }
        return start.getDistantForwardNodes(distance, predicate);
    }
    
    default List<N> getForwardNodes(F figure, int distance, Predicate<N> predicate)
    {
        return getForwardNodes(figure.getCurrentNode(), distance, predicate);
    }
    
    default List<N> getBackwardNodes(N start, int distance, Predicate<N> predicate)
    {
        if(!isNodeOnBoard(start))
        {
            return List.of();
        }
        return start.getDistantBackwardNodes(distance, predicate);
    }
    
    default List<N> getBackwardNodes(F figure, int distance, Predicate<N> predicate)
    {
        return getBackwardNodes(figure.getCurrentNode(), distance, predicate);
    }
    
    default List<N> getForwardAndBackwardNodes(N start, int distance, Predicate<N> predicate)
    {
        if(!isNodeOnBoard(start))
        {
            return List.of();
        }
        List<N> distantNodes = start.getDistantForwardNodes(distance, predicate);
        distantNodes.addAll(start.getDistantBackwardNodes(distance, predicate));
        
        return distantNodes;
    }
    
    default List<N> getForwardAndBackwardNodes(F figure, int distance, Predicate<N> predicate)
    {
        return getForwardAndBackwardNodes(figure.getCurrentNode(), distance, predicate);
    }
    
    default void moveFigure(F figure, N target)
    {
        if(!isFigureOnBoard(figure) || !isNodeOnBoard(target))
        {
            return;
        }
        
        if(figure.getCurrentNode() != null)
        {
            figure.getCurrentNode().removeFigure(figure);
        }
    
        placeFigure(figure, target);
    }
    
    default void placeFigure(F figure, N target) {
        if(!isFigureOnBoard(figure) || !isNodeOnBoard(target))
        {
            return;
        }
        figure.setCurrentNode(target);
        target.addFigure(figure);
    }
    
    default void linkNodes(N backwardNode, N forwardNode)
    {
        INode.linkNodes(backwardNode, forwardNode);
    }
}
