package sweng_plus.framework.userinterface;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class InputHandler
{
    private final Window window;
    
    private final List<TrackedKey> keys;
    private final TrackedKey[] mouseButtons;
    private final List<Character> chars;
    
    private double mouseX;
    private double mouseY;
    
    public InputHandler(Window window)
    {
        this.window = window;
        
        this.keys = new LinkedList<>();
        
        this.mouseButtons = new TrackedKey[GLFW_MOUSE_BUTTON_LAST + 1];
        for(int i = 0; i <= GLFW_MOUSE_BUTTON_LAST; ++i)
        {
            mouseButtons[i] = new TrackedKey(i);
        }
        
        this.chars = new LinkedList<>();
    }
    
    public void setup()
    {
        // Callback für Maus Position
        glfwSetCursorPosCallback(window.getWindowHandle(), (window, mouseX, mouseY) ->
        {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        });
        
        // Callback für Tasten; wird bei jedem drücken, wiederholtem drücken (= GLFW_REPEAT) oder loslassen gecallt
        glfwSetKeyCallback(window.getWindowHandle(), (window, key, scancode, action, mods) ->
        {
            TrackedKey trackedKey = getKeyTracking(key);
            if(trackedKey != null)
            {
                handleTrackedKeyCallback(trackedKey, action, mods);
            }
            
            if(action == GLFW_PRESS && key == GLFW_KEY_ESCAPE)
            {
                // Fenster schliessen bei ESC TODO irgendwann entfernen
                glfwSetWindowShouldClose(window, true);
            }
        });
        
        // Callback für Maus; wird bei jedem drücken oder loslassen gecallt
        glfwSetMouseButtonCallback(window.getWindowHandle(), (window, mouseButton, action, mods) ->
        {
            TrackedKey trackedKey = getMouseButtonTracking(mouseButton);
            if(trackedKey != null)
            {
                handleTrackedKeyCallback(trackedKey, action, mods);
            }
        });
        
        // Callback für Tasten, aber nicht jegliche gedrückten, sondern nur, wenn schreibbare Chars daraus resultieren
        // Gut für Chat; also z.B. die Tasten Shift+1 produzieren nicht 2 Callbacks, sondern nur einen, welcher '!' ist
        // Passt sich auch an Tastatur und Sprache an
        glfwSetCharCallback(window.getWindowHandle(), (window, charCode) ->
        {
            char character = (char) charCode;
            
            if(!this.chars.contains(character))
                this.chars.add(character);
        });
        
        
        // TODO Input Handler
        //glfwSetScrollCallback() // Mausrad
    }
    
    public void free()
    {
        glfwFreeCallbacks(window.getWindowHandle());
    }
    
    public void postUpdate()
    {
        for(TrackedKey trackedKey : this.keys)
        {
            trackedKey.setUnchanged();
        }
        
        for(TrackedKey trackedKey : this.mouseButtons)
        {
            trackedKey.setUnchanged();
        }
        
        this.chars.clear();
    }
    
    protected TrackedKey getMouseButtonTracking(int mouseButton)
    {
        if(mouseButton >= 0 && mouseButton < mouseButtons.length)
        {
            return mouseButtons[mouseButton];
        }
        
        return null;
    }
    
    protected TrackedKey getKeyTracking(int key)
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
    
    protected void handleTrackedKeyCallback(TrackedKey trackedKey, int action, int mods)
    {
        if(trackedKey != null && !trackedKey.getChanged())
        {
            if(action == GLFW_PRESS)
            {
                trackedKey.setPressed(true, mods);
            }
            else if(action == GLFW_RELEASE)
            {
                trackedKey.setPressed(false, mods);
            }
        }
    }
    
    public InputHandler registerKeyTracking(int key)
    {
        if(!isKeyTracked(key))
        {
            this.keys.add(new TrackedKey(key));
        }
        
        return this;
    }
    
    public void inputScreen(IInputListener listener)
    {
        for(TrackedKey trackedKey : mouseButtons)
        {
            if(trackedKey.getChanged())
            {
                if(trackedKey.getPressed())
                {
                    listener.mouseButtonPressed(this.getMouseX(), this.getMouseY(), trackedKey.getKey(), trackedKey.getMods());
                }
                else
                {
                    listener.mouseButtonReleased(this.getMouseX(), this.getMouseY(), trackedKey.getKey(), trackedKey.getMods());
                }
            }
        }
        
        for(TrackedKey trackedKey : keys)
        {
            if(trackedKey.getChanged())
            {
                if(trackedKey.getPressed())
                {
                    listener.keyPressed(trackedKey.getKey(), trackedKey.getMods());
                }
                else
                {
                    listener.keyReleased(trackedKey.getKey(), trackedKey.getMods());
                }
            }
        }
        
        for(char c : this.chars)
        {
            listener.charTyped(c);
        }
    }
    
    public boolean isKeyTracked(int key)
    {
        return getKeyTracking(key) != null;
    }
    
    public int getMouseX()
    {
        return (int) mouseX;
    }
    
    public int getMouseY()
    {
        return (int) mouseY;
    }
    
    public boolean isMouseButtonDown(int mouseButton)
    {
        return glfwGetMouseButton(window.getWindowHandle(), mouseButton) == GLFW_PRESS;
    }
    
    public boolean isKeyDown(int key)
    {
        return glfwGetKey(window.getWindowHandle(), key) == GLFW_PRESS;
    }
    
    public static class TrackedKey
    {
        private final int key;
        private boolean pressed;
        
        private boolean changed;
        private int mods;
        
        public TrackedKey(int key)
        {
            this.key = key;
            this.pressed = false;
            this.changed = false;
            this.mods = 0;
        }
        
        public int getKey()
        {
            return key;
        }
        
        public void setPressed(boolean pressed, int mods)
        {
            this.pressed = pressed;
            
            this.changed = true;
            this.mods = mods;
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
        
        public int getMods()
        {
            return mods;
        }
    }
}
