package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.userinterface.gui.util.Color4f;

public class TeamColor
{
    public static final TeamColor WHITE = new TeamColor("White", Color4f.WHITE);
    public static final TeamColor BLACK = new TeamColor("Black", Color4f.BLACK);
    public static final TeamColor GREY = new TeamColor("Grey", Color4f.GREY);
    public static final TeamColor RED = new TeamColor("Red", Color4f.RED);
    public static final TeamColor BLUE = new TeamColor("Blue", Color4f.BLUE);
    public static final TeamColor GREEN = new TeamColor("Green", Color4f.GREEN);
    public static final TeamColor YELLOW = new TeamColor("Yellow", Color4f.YELLOW);
    public static final TeamColor PURPLE = new TeamColor("Purple", Color4f.PURPLE);
    public static final TeamColor CYAN = new TeamColor("Cyan", Color4f.CYAN);
    
    public static final TeamColor[] TEAMS_2 = {RED, BLUE};
    public static final TeamColor[] TEAMS_3 = {RED, GREEN, BLUE};
    public static final TeamColor[] TEAMS_4 = {RED, YELLOW, GREEN, BLUE};
    public static final TeamColor[] TEAMS_5 = {RED, YELLOW, GREEN, BLUE, PURPLE};
    public static final TeamColor[] TEAMS_6 = {RED, YELLOW, GREEN, CYAN, BLUE, PURPLE};
    
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
    
    @Override
    public String toString()
    {
        return "TeamColor{" +
                "name='" + name +
                '}';
    }
}
