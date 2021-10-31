package sweng_plus.framework.boardgame;

import sweng_plus.framework.userinterface.Window;

import static org.lwjgl.glfw.GLFW.*;

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
    
    default Window createWindow()
    {
        Window window = new Window(getWindowTitle())
                .hint(GLFW_VISIBLE, GLFW_FALSE) // Setzt das Fenster unsichtbar
                .hint(GLFW_RESIZABLE, GLFW_TRUE); // Setzt das Fenster skalierbar
        
        window.init();
        
        return window;
    }
}
