package sweng_plus.framework.boardgame.nodes_board;

public class TeamNode extends Node
{
    protected TeamColor color;
    
    public TeamNode(TeamColor color)
    {
        this.color = color;
    }
    
    public TeamColor getColor()
    {
        return color;
    }
}
