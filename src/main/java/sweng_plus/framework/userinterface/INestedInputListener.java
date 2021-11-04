package sweng_plus.framework.userinterface;

import java.util.List;

public interface INestedInputListener extends IInputListener
{
    List<? extends IInputListener> getListeners();
    
    default void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.mouseButtonPressed(mouseX, mouseY, mouseButton, mods);
        }
    }
    
    default void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.mouseButtonReleased(mouseX, mouseY, mouseButton, mods);
        }
    }
    
    default void keyPressed(int key, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.keyPressed(key, mods);
        }
    }
    
    default void keyReleased(int key, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.keyReleased(key, mods);
        }
    }
    
    default void charTyped(char character)
    {
        for(IInputListener listener : getListeners())
        {
            listener.charTyped(character);
        }
    }
}
