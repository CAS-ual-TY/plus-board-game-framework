package sweng_plus.framework_test.networking.manual_chat;

import sweng_plus.boardgames.ludo.gui.widget.TexturedButtonWidget;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.NetworkManager;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Texture;
import sweng_plus.framework.userinterface.gui.util.TextureHelper;
import sweng_plus.framework.userinterface.gui.widget.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;

import java.io.IOException;
import java.util.Arrays;

public class NetTestMenuScreen extends Screen
{
    public NetTestMenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions topButton = new Dimensions(400, 100, AnchorPoint.M, 0, -150);
        Dimensions middleButton = new Dimensions(400, 100, AnchorPoint.M, 0, 0);
        Dimensions bottomButton = new Dimensions(400, 100, AnchorPoint.M, 0, 150);
        
        try
        {
            Texture buttonActive = TextureHelper.createTexture("src/test/resources/textures/button_test_active.png");
            Texture buttonInactive = TextureHelper.createTexture("src/test/resources/textures/button_test_inactive.png");
            
            widgets.add(new TexturedButtonWidget(this, topButton, this::host, buttonActive, buttonInactive));
            widgets.add(new TextWidget(this, topButton, NetTestGame.instance().fontRenderer48, Arrays.asList("Host")));
            
            widgets.add(new TexturedButtonWidget(this, middleButton, this::connect, buttonActive, buttonInactive));
            widgets.add(new TextWidget(this, middleButton, NetTestGame.instance().fontRenderer48, Arrays.asList("Connect")));
            
            widgets.add(new TexturedButtonWidget(this, bottomButton, this::exit, buttonActive, buttonInactive));
            widgets.add(new TextWidget(this, bottomButton, NetTestGame.instance().fontRenderer48, Arrays.asList("Exit")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void host(int mouseX, int mouseY, int mods)
    {
        try
        {
            NetTestGame.instance().hostManager = NetworkManager.host(NetTestGame.instance().protocol, Client.createFactory(), 100);
            NetTestGame.instance().clientManager = NetTestGame.instance().hostManager;
            NetTestGame.instance().setScreen(new NetTestChatScreen(this));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void connect(int mouseX, int mouseY, int mods)
    {
        try
        {
            NetTestGame.instance().clientManager = NetworkManager.connect(NetTestGame.instance().protocol, "localhost", 100);
            NetTestGame.instance().setScreen(new NetTestChatScreen(this));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void exit(int mouseX, int mouseY, int mods)
    {
        Engine.instance().close();
    }
}
