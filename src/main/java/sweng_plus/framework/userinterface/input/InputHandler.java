package sweng_plus.framework.userinterface.input;

import sweng_plus.framework.userinterface.Window;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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
        
        keys = new LinkedList<>();
        
        mouseButtons = new TrackedKey[GLFW_MOUSE_BUTTON_LAST + 1];
        for(int i = 0; i <= GLFW_MOUSE_BUTTON_LAST; ++i)
        {
            mouseButtons[i] = new TrackedKey(i);
        }
        
        chars = new LinkedList<>();
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
            
            if(!chars.contains(character))
            {
                chars.add(character);
            }
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
        for(TrackedKey trackedKey : keys)
        {
            trackedKey.clear();
        }
        
        for(TrackedKey trackedKey : mouseButtons)
        {
            trackedKey.clear();
        }
        
        chars.clear();
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
        for(TrackedKey trackedKey : keys)
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
        if(action == GLFW_PRESS)
        {
            trackedKey.setPressed(mods);
        }
        else if(action == GLFW_REPEAT)
        {
            trackedKey.setRepeated(mods);
        }
        else if(action == GLFW_RELEASE)
        {
            trackedKey.setReleased(mods);
        }
    }
    
    public InputHandler registerKeyTracking(int key)
    {
        if(!isKeyTracked(key))
        {
            keys.add(new TrackedKey(key));
        }
        
        return this;
    }
    
    public void inputListener(IInputListener listener)
    {
        for(TrackedKey trackedKey : mouseButtons)
        {
            trackedKey.getPressed((mods) -> listener.mouseButtonPressed(getMouseX(), getMouseY(), trackedKey.getKey(), mods));
            trackedKey.getReleased((mods) -> listener.mouseButtonReleased(getMouseX(), getMouseY(), trackedKey.getKey(), mods));
        }
        
        for(TrackedKey trackedKey : keys)
        {
            trackedKey.getPressed((mods) -> listener.keyPressed(trackedKey.getKey(), mods));
            trackedKey.getRepeated((mods) -> listener.keyRepeated(trackedKey.getKey(), mods));
            trackedKey.getReleased((mods) -> listener.keyReleased(trackedKey.getKey(), mods));
        }
        
        for(char c : chars)
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
        return (int) (mouseX * window.getScaleFactor());
    }
    
    public int getMouseY()
    {
        return (int) (mouseY * window.getScaleFactor());
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
        private List<Integer> newPressed;
        private List<Integer> pressed;
        private List<Integer> repeated;
        private List<Integer> released;
        
        public TrackedKey(int key)
        {
            this.key = key;
            newPressed = new LinkedList<>();
            pressed = new LinkedList<>();
            repeated = new LinkedList<>();
            released = new LinkedList<>();
        }
        
        public int getKey()
        {
            return key;
        }
        
        public void setPressed(Integer mods)
        {
            if(!released.contains(mods) && !pressed.contains(mods) && !newPressed.contains(mods))
            {
                newPressed.add(mods);
            }
        }
        
        public void setRepeated(Integer mods)
        {
            if((pressed.contains(mods) || newPressed.contains(mods)) && !repeated.contains(mods))
            {
                repeated.add(mods);
            }
        }
        
        public void setReleased(Integer mods)
        {
            if((pressed.contains(mods) || newPressed.contains(mods)) && !released.contains(mods))
            {
                pressed.remove(mods);
                newPressed.remove(mods);
                released.add(mods);
            }
        }
        
        public void getPressed(Consumer<Integer> consumer)
        {
            for(int mods : newPressed)
            {
                consumer.accept(mods);
            }
        }
        
        public void getRepeated(Consumer<Integer> consumer)
        {
            for(int mods : repeated)
            {
                consumer.accept(mods);
            }
        }
        
        public void getReleased(Consumer<Integer> consumer)
        {
            for(int mods : released)
            {
                consumer.accept(mods);
            }
        }
        
        public void clear()
        {
            pressed.addAll(newPressed);
            newPressed.clear();
            repeated.clear();
            released.clear();
        }
    }
}
