package sweng_plus.framework.userinterface.gui;

public interface IScreenHolder
{
    /** Returnt den {@link sweng_plus.framework.userinterface.gui.Screen} der aktuell aktiv ist. Darf niemals null sein. */
    Screen getScreen();
    
    void setScreen(Screen screen);
}
