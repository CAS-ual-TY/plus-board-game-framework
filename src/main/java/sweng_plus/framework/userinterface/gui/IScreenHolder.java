package sweng_plus.framework.userinterface.gui;

public interface IScreenHolder
{
    /**
     * @return The currently active {@link Screen} which is to be rendered and ticked. May never return null.
     */
    Screen getScreen();
    
    /**
     * @param screen Sets the {@link Screen} returned by {@link #getScreen()}.
     */
    void setScreen(Screen screen);
}
