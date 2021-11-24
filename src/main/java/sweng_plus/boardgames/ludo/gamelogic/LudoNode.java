package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.TeamNode;

public class LudoNode extends TeamNode
{
    private final LudoNodeType nodeType;
    
    public LudoNode(TeamColor color, LudoNodeType nodeType)
    {
        super(color);
        this.nodeType = nodeType;
    }
    
    public LudoNodeType getNodeType()
    {
        return nodeType;
    }
}
