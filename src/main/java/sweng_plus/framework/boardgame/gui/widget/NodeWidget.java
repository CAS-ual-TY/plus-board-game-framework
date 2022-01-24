package sweng_plus.framework.boardgame.gui.widget;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeFigure;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.ArrayList;
import java.util.List;

public class NodeWidget<W extends NodeWidget<W, N, F>, N extends INode<F, N>, F extends INodeFigure<N, F>> extends Widget
{
    protected N node;
    
    protected List<W> forwardNodes;
    protected List<W> backwardNodes;
    
    public NodeWidget(IScreenHolder screenHolder, Dimensions dimensions, N node)
    {
        super(screenHolder, dimensions);
        
        this.node = node;
        
        forwardNodes = new ArrayList<>(node.getForwardNodes().size());
        backwardNodes = new ArrayList<>(node.getBackwardNodes().size());
    }
    
    public N getNode()
    {
        return node;
    }
    
    public List<W> getForwardNodes()
    {
        return forwardNodes;
    }
    
    public List<W> getBackwardNodes()
    {
        return backwardNodes;
    }
    
    public void addForwardNode(W node)
    {
        forwardNodes.add(node);
    }
    
    public void addBackwardNode(W node)
    {
        backwardNodes.add(node);
    }
}
