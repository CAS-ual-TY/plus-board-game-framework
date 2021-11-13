package sweng_plus.framework.userinterface;

import java.util.List;

public interface INestedInputListener extends IInputListener
{
    List<? extends IInputListener> getListeners();
    
    @Override
    default void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.mouseButtonPressed(mouseX, mouseY, mouseButton, mods);
        }
    }
    
    @Override
    default void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.mouseButtonReleased(mouseX, mouseY, mouseButton, mods);
        }
    }
    
    @Override
    default void keyPressed(int key, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.keyPressed(key, mods);
        }
    }
    
    @Override
    default void keyRepeated(int key, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.keyRepeated(key, mods);
        }
    }
    
    @Override
    default void keyReleased(int key, int mods)
    {
        for(IInputListener listener : getListeners())
        {
            listener.keyReleased(key, mods);
        }
    }
    
    @Override
    default void charTyped(char character)
    {
        for(IInputListener listener : getListeners())
        {
            listener.charTyped(character);
        }
    }
}
