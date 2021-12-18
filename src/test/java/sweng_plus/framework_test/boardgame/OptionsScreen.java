package sweng_plus.framework_test.boardgame;

import org.lwjgl.glfw.GLFW;
import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.style.IStyle;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
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
            Texture buttonActive = TextureHelper.createTexture("/textures/button_test_active.png");
            Texture buttonInactive = TextureHelper.createTexture("/textures/button_test_inactive.png");
            
            IStyle style = new HoverStyle(new CorneredTextureStyle(buttonInactive), new CorneredTextureStyle(buttonActive));
            
            widgets.add(new FunctionalButtonWidget(screenHolder, topButton, style, (button, mouseX, mouseY, mods) -> {}));
            
            widgets.add(new FunctionalButtonWidget(screenHolder, middleButton, style, (button, mouseX, mouseY, mods) -> {}));
            
            widgets.add(new FunctionalButtonWidget(screenHolder, bottomButton, style, (button, mouseX, mouseY, mods) -> Engine.instance().close()));
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        widgets.add(new SimpleWidget(screenHolder, topButton.clone(), new TextStyle(Ludo.instance().fontRenderer32, "Top Button")));
        widgets.add(new SimpleWidget(screenHolder, middleButton.clone(), new TextStyle(Ludo.instance().fontRenderer32, "Middle Button")));
        widgets.add(new SimpleWidget(screenHolder, bottomButton.clone(), new TextStyle(Ludo.instance().fontRenderer32, "Exit")));
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
