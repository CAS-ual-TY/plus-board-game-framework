package sweng_plus.framework.boardgame;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.input.IInputListener;

import java.util.List;

public interface IGame extends IScreenHolder
{
    default void preInit() {}
    
    default void init() {}
    
    default void postInit() {}
    
    default void cleanup() {}
    
    String getWindowTitle();
    
    void update();
    
    void render(float deltaTick);
    
    List<IInputListener> getInputListeners();
    
    default int getTicksPerSecond()
    {
        return 20;
    }
}
