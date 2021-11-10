package sweng_plus.boardgames.ludo.gui;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.gui.AnchorPoint;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.Texture;
import sweng_plus.framework.userinterface.gui.util.TextureHelper;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.Dimensions;

import java.io.IOException;

public class OptionsScreen extends WrappedScreen
{
    public OptionsScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions topButton = new Dimensions(400, 100, AnchorPoint.M, 0, -150);
        Dimensions middleButton = new Dimensions(400, 100, AnchorPoint.M, 0, 0);
        Dimensions bottomButton = new Dimensions(400, 100, AnchorPoint.M, 0, 150);
        
        try
        {
            Texture buttonActive = TextureHelper.createTexture("src/main/resources/textures/button_test_active.png");
            Texture buttonInactive = TextureHelper.createTexture("src/main/resources/textures/button_test_inactive.png");
            
            widgets.add(new ButtonWidget(this, topButton, buttonActive, buttonInactive)
            {
                @Override
                protected void clicked(int mouseX, int mouseY, int mods)
                {
                
                }
            });
            
            widgets.add(new ButtonWidget(this, middleButton, buttonActive, buttonInactive)
            {
                @Override
                protected void clicked(int mouseX, int mouseY, int mods)
                {
                
                }
            });
            
            widgets.add(new ButtonWidget(this, bottomButton, buttonActive, buttonInactive)
            {
                @Override
                protected void clicked(int mouseX, int mouseY, int mods)
                {
                
                }
            });
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
