package sweng_plus.framework.boardgame;

import sweng_plus.framework.userinterface.gui.IScreenHolder;

public interface IGame extends IScreenHolder
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
