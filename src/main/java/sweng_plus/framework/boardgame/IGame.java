package sweng_plus.framework.boardgame;

public interface IGame
{
    default void preInit() {}
    
    default void init() {}
    
    default void postInit() {}
    
    default void cleanup() {}
    
    String getWindowTitle();
    
    void update();
    
    void render(float deltaTick);
    
    default int getTicksPerSecond()
    {
        return 20;
    }
}
