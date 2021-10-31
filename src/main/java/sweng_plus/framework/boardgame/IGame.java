package sweng_plus.framework.boardgame;

public interface IGame
{
    public default void preInit() {}
    
    public default void init() {}
    
    public default void postInit() {}
    
    public default void cleanup() {}
    
    public String getWindowTitle();
    
    public void update();
    
    public void render(float deltaTick);
    
    public default int getTicksPerSecond()
    {
        return 20;
    }
}
