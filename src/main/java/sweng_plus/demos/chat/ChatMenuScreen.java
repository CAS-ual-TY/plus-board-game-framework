package sweng_plus.demos.chat;

import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.util.Dimensions;

public class ChatMenuScreen extends Screen
{
    public ChatMenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions topButton = new Dimensions(400, 100, AnchorPoint.M, 0, -150);
        Dimensions middleButton = new Dimensions(400, 100, AnchorPoint.M, 0, 0);
        Dimensions bottomButton = new Dimensions(400, 100, AnchorPoint.M, 0, 150);
        
        widgets.add(new FunctionalButtonWidget(screenHolder, topButton, ChatMain.hoverStyle("Host"), this::host));
        widgets.add(new FunctionalButtonWidget(screenHolder, middleButton, ChatMain.hoverStyle("Connect"), this::connect));
        widgets.add(new FunctionalButtonWidget(screenHolder, bottomButton, ChatMain.hoverStyle("Exit"), this::exit));
    }
    
    private void host()
    {
        ChatMain.instance().setScreen(new ChatMenuHostScreen(this));
    }
    
    private void connect(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        ChatMain.instance().setScreen(new ChatMenuConnectScreen(this));
    }
    
    private void exit(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        Engine.instance().close();
    }
}
