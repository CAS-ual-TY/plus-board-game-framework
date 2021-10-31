package sweng_plus.framework.userinterface;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class InputHandler
{
    private final Window window;
    
    public InputHandler(Window window)
    {
        this.window = window;
    }
    
    public void setup()
    {
        // Callback für Tasten; wird bei jedem drücken, wiederholtem drücken oder loslassen gecallt
        // TODO Input Handler
        glfwSetKeyCallback(window.getWindowHandle(), (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // Aktuell: Fenster schliessen bei ESC
        });
        
        // TODO Input Handler
        //glfwSetMouseButtonCallback()
        //glfwSetCursorEnterCallback()
        //glfwSetScrollCallback()
        //glfwSetCursorPosCallback()
        //glfwSetFramebufferSizeCallback() // Für Fenster skalierungen
    }
    
    public void free()
    {
        glfwFreeCallbacks(window.getWindowHandle());
    }
}
