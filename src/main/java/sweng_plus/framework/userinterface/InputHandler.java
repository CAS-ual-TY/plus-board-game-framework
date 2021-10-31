package sweng_plus.framework.userinterface;

import sweng_plus.framework.userinterface.gui.Screen;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class InputHandler
{
    private final Window window;
    
    private List<TrackedKey> keys;
    private List<TrackedKey> mouseButtons;
    
    public InputHandler(Window window)
    {
        this.window = window;
        
        this.keys = new LinkedList<>();
        this.mouseButtons = new LinkedList<>();
    }
    
    public void setup()
    {
        // Callback für Tasten; wird bei jedem drücken, wiederholtem drücken oder loslassen gecallt
        glfwSetKeyCallback(window.getWindowHandle(), (window, key, scancode, action, mods) ->
        {
            TrackedKey trackedKey = getKeyTracking(key);
            
            if(trackedKey != null && !trackedKey.getChanged())
            {
                if(action == GLFW_PRESS)
                {
                    trackedKey.setPressed(true);
                }
                else if(action == GLFW_RELEASE)
                {
                    trackedKey.setPressed(false);
                }
            }
            else if(action == GLFW_PRESS && key == GLFW_KEY_ESCAPE)
            {
                // Fenster schliessen bei ESC TODO irgendwann entfernen
                glfwSetWindowShouldClose(window, true);
            }
            
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
    
    public InputHandler registerKeyTracking(int key)
    {
        if(!isKeyTracked(key))
        {
            this.keys.add(new TrackedKey(key));
        }
        
        return this;
    }
    
    public TrackedKey getKeyTracking(int key)
    {
        for(TrackedKey trackedKey : this.keys)
        {
            if(trackedKey.getKey() == key)
            {
                return trackedKey;
            }
        }
        
        return null;
    }
    
    public boolean isKeyTracked(int key)
    {
        return getKeyTracking(key) != null;
    }
    
    public void inputScreen(Screen screen)
    {
        for(TrackedKey trackedKey : keys)
        {
            if(trackedKey.getChanged())
            {
                if(trackedKey.getPressed())
                {
                    screen.keyPressed(trackedKey.getKey());
                }
                else
                {
                    screen.keyReleased(trackedKey.getKey());
                }
                
                trackedKey.setUnchanged();
            }
        }
    }
    
    public static class TrackedKey
    {
        private final int key;
        private boolean pressed;
        private boolean changed;
        
        public TrackedKey(int key)
        {
            this.key = key;
            this.pressed = false;
            this.changed = false;
        }
        
        public int getKey()
        {
            return key;
        }
        
        public void setPressed(boolean pressed)
        {
            this.pressed = pressed;
            this.changed = true;
        }
        
        /**
         * True = Der Key State (= pressed) wurde gerade gewechselt. Wird wieder False nach dem nächsten Render Tick.
         */
        public boolean getChanged()
        {
            return changed;
        }
        
        public void setUnchanged()
        {
            changed = false;
        }
        
        public boolean getPressed()
        {
            return pressed;
        }
    }
}
