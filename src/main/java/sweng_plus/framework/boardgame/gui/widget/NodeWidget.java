package sweng_plus.framework.boardgame.gui.widget;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.ArrayList;
import java.util.List;

public class NodeWidget extends Widget
{
    protected INode node;
    
    protected List<NodeWidget> forwardNodes;
    protected List<NodeWidget> backwardNodes;
    
    public NodeWidget(IScreenHolder screenHolder, Dimensions dimensions, INode node)
    {
        super(screenHolder, dimensions);
        
        this.node = node;
        
        forwardNodes = new ArrayList<>(node.getForwardNodes().size());
        backwardNodes = new ArrayList<>(node.getBackwardNodes().size());
    }
    
    public INode getNode()
    {
        return node;
    }
    
    public List<NodeWidget> getForwardNodes()
    {
        return forwardNodes;
    }
    
    public List<NodeWidget> getBackwardNodes()
    {
        return backwardNodes;
    }
    
    public void addForwardNode(NodeWidget node)
    {
        forwardNodes.add(node);
    }
    
    public void addBackwardNode(NodeWidget node)
    {
        backwardNodes.add(node);
    }
}
