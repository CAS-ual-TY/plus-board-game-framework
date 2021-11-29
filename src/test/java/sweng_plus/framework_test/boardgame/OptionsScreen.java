package sweng_plus.framework_test.boardgame;

import org.lwjgl.glfw.GLFW;
import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.widget.TexturedButtonWidget;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

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
            Texture buttonActive = TextureHelper.createTexture("src/test/resources/textures/button_test_active.png");
            Texture buttonInactive = TextureHelper.createTexture("src/test/resources/textures/button_test_inactive.png");
            
            widgets.add(new TexturedButtonWidget(screenHolder, topButton, (button, mouseX, mouseY, mods) -> {}, buttonActive, buttonInactive));
            
            widgets.add(new TexturedButtonWidget(screenHolder, middleButton, (button, mouseX, mouseY, mods) -> {}, buttonActive, buttonInactive));
            
            widgets.add(new TexturedButtonWidget(screenHolder, bottomButton, (button, mouseX, mouseY, mods) -> Engine.instance().close(), buttonActive, buttonInactive));
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        widgets.add(new TextWidget(screenHolder, topButton.clone(), Ludo.instance().fontRenderer32, "Top Button"));
        widgets.add(new TextWidget(screenHolder, middleButton.clone(), Ludo.instance().fontRenderer32, "Middle Button"));
        widgets.add(new TextWidget(screenHolder, bottomButton.clone(), Ludo.instance().fontRenderer32, "Exit"));
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        super.keyPressed(key, mods);
        
        if(key == GLFW.GLFW_KEY_ESCAPE)
        {
            returnToSubScreen();
        }
    }
}
