package sweng_plus.boardgames.ludo.gui;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;

public class OptionsScreen extends WrappedScreen
{
    public OptionsScreen(Screen subScreen)
    {
        super(subScreen);
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        super.keyPressed(key, mods);
        
        if(key == GLFW.GLFW_KEY_SPACE)
        {
            returnToSubScreen();
        }
    }
}
