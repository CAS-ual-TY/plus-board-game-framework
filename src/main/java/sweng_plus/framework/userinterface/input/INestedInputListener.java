package sweng_plus.framework.userinterface.input;

import java.util.List;

public interface INestedInputListener extends IInputListener
{
    /**
     * @return All sub-{@link IInputListener}s of this {@link INestedInputListener}.
     */
    List<? extends IInputListener> getInputListeners();
    
    /**
     * Calls {@link IInputListener#mouseButtonPressed(int, int, int, int)} for all {@link IInputListener}s
     * returned by {@link #getInputListeners()}.
     */
    @Override
    default void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        for(IInputListener listener : getInputListeners())
        {
            listener.mouseButtonPressed(mouseX, mouseY, mouseButton, mods);
        }
    }
    
    /**
     * Calls {@link IInputListener#mouseButtonReleased(int, int, int, int)} for all {@link IInputListener}s
     * returned by {@link #getInputListeners()}.
     */
    @Override
    default void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
        for(IInputListener listener : getInputListeners())
        {
            listener.mouseButtonReleased(mouseX, mouseY, mouseButton, mods);
        }
    }
    
    /**
     * Calls {@link IInputListener#keyPressed(int, int)} for all {@link IInputListener}s
     * returned by {@link #getInputListeners()}.
     */
    @Override
    default void keyPressed(int key, int mods)
    {
        for(IInputListener listener : getInputListeners())
        {
            listener.keyPressed(key, mods);
        }
    }
    
    /**
     * Calls {@link IInputListener#keyRepeated(int, int)} for all {@link IInputListener}s
     * returned by {@link #getInputListeners()}.
     */
    @Override
    default void keyRepeated(int key, int mods)
    {
        for(IInputListener listener : getInputListeners())
        {
            listener.keyRepeated(key, mods);
        }
    }
    
    /**
     * Calls {@link IInputListener#keyReleased(int, int)} for all {@link IInputListener}s
     * returned by {@link #getInputListeners()}.
     */
    @Override
    default void keyReleased(int key, int mods)
    {
        for(IInputListener listener : getInputListeners())
        {
            listener.keyReleased(key, mods);
        }
    }
    
    /**
     * Calls {@link IInputListener#charTyped(char)} for all {@link IInputListener}s
     * returned by {@link #getInputListeners()}.
     */
    @Override
    default void charTyped(char character)
    {
        for(IInputListener listener : getInputListeners())
        {
            listener.charTyped(character);
        }
    }
}
