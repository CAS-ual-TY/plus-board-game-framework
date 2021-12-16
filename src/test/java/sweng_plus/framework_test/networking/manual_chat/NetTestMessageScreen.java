package sweng_plus.framework_test.networking.manual_chat;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class NetTestMessageScreen extends Screen
{
    public NetTestMessageScreen(IScreenHolder screenHolder, String message)
    {
        super(screenHolder);
        
        Dimensions topButton = new Dimensions(400, 100, AnchorPoint.M, 0, -75);
        Dimensions bottomButton = new Dimensions(400, 100, AnchorPoint.M, 0, 75);
        
        try
        {
            Texture buttonActive = TextureHelper.createTexture("src/test/resources/textures/button_test_active.png");
            Texture buttonInactive = TextureHelper.createTexture("src/test/resources/textures/button_test_inactive.png");
            
            widgets.add(new TextWidget(screenHolder, topButton, NetTestGame.instance().fontRenderer48, message).adjustSizeToText());
            
            widgets.add(new FunctionalButtonWidget(screenHolder, bottomButton, new HoverStyle(new CorneredTextureStyle(buttonInactive), new CorneredTextureStyle(buttonActive)), this::toMainMenu));
            widgets.add(new TextWidget(screenHolder, bottomButton, NetTestGame.instance().fontRenderer48, "Main Menu"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void toMainMenu()
    {
        screenHolder.setScreen(new NetTestMenuScreen(screenHolder));
    }
}
