package sweng_plus.boardgames.ludo.gui;

import org.lwjgl.glfw.GLFW;
import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.widget.TexturedButtonWidget;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Texture;
import sweng_plus.framework.userinterface.gui.util.TextureHelper;
import sweng_plus.framework.userinterface.gui.widget.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;

import java.io.IOException;
import java.util.List;

public class OptionsScreen extends WrappedScreen
{
    @SuppressWarnings("ThisEscapedInObjectConstruction")
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
            
            widgets.add(new TexturedButtonWidget(this, topButton, (mouseX, mouseY, mods) -> {}, buttonActive, buttonInactive));
            
            widgets.add(new TexturedButtonWidget(this, middleButton, (mouseX, mouseY, mods) -> {}, buttonActive, buttonInactive));
            
            widgets.add(new TexturedButtonWidget(this, bottomButton, (mouseX, mouseY, mods) -> Engine.instance().close(), buttonActive, buttonInactive));
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        widgets.add(new TextWidget(this, topButton.clone(), Ludo.instance().fontRenderer32, List.of("Top Button")));
        widgets.add(new TextWidget(this, middleButton.clone(), Ludo.instance().fontRenderer32, List.of("Middle Button")));
        widgets.add(new TextWidget(this, bottomButton.clone(), Ludo.instance().fontRenderer32, List.of("Exit")));
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
