package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.TeamNode;

public class LudoNode extends TeamNode
{
    private final LudoNodeType nodeType;
    
    protected final int index;
    
    public LudoNode(TeamColor color, LudoNodeType nodeType, int index)
    {
        super(color);
        this.nodeType = nodeType;
        this.index = index;
    }
    
    public LudoNodeType getNodeType()
    {
        return nodeType;
    }
    
    public int getIndex()
    {
        return index;
    }
    
    @Override
    public String toString()
    {
        return "LudoNode{" +
                ", color=" + color +
                "nodeType=" + nodeType +
                ", index=" + index +
                '}';
    }
}
