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
}
