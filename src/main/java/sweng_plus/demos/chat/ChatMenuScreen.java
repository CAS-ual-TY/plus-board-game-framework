package sweng_plus.demos.chat;

import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.NetworkHelper;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class ChatMenuScreen extends Screen
{
    public ChatMenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions topButton = new Dimensions(400, 100, AnchorPoint.M, 0, -150);
        Dimensions middleButton = new Dimensions(400, 100, AnchorPoint.M, 0, 0);
        Dimensions bottomButton = new Dimensions(400, 100, AnchorPoint.M, 0, 150);
        
        widgets.add(new FunctionalButtonWidget(screenHolder, topButton, ChatGame.hoverStyle("Host"), this::host));
        widgets.add(new FunctionalButtonWidget(screenHolder, middleButton, ChatGame.hoverStyle("Connect"), this::connect));
        widgets.add(new FunctionalButtonWidget(screenHolder, bottomButton, ChatGame.hoverStyle("Exit"), this::exit));
    }
    
    private void host(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        try
        {
            ChatGame.instance().hostManager = NetworkHelper.advancedHost(ChatGame.instance().protocol,
                    ChatGame.instance().listener, Client.createFactory(), 100);
            ChatGame.instance().clientManager = ChatGame.instance().hostManager;
            ChatGame.instance().setScreen(new ChatScreen(this));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void connect(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        try
        {
            ChatGame.instance().clientManager = NetworkHelper.advancedConnect(ChatGame.instance().protocol,
                    ChatGame.instance().listener, "localhost", 100);
            ChatGame.instance().setScreen(new ChatScreen(this));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void exit(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        Engine.instance().close();
    }
}