package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.userinterface.gui.util.Color4f;

public class TeamColor
{
    protected String name;
    protected Color4f color;
    
    public TeamColor(String name, Color4f color)
    {
        this.name = name;
        this.color = color;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Color4f getColor()
    {
        return color;
    }
}
