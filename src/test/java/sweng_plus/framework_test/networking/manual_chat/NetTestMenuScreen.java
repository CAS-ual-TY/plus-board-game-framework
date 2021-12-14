package sweng_plus.framework_test.networking.manual_chat;

import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.NetworkHelper;
import sweng_plus.framework.networking.interfaces.IClientEventsListener;
import sweng_plus.framework.networking.interfaces.IHostEventsListener;
import sweng_plus.framework.networking.util.NetworkRole;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

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
    
            widgets.add(new FunctionalButtonWidget(screenHolder, topButton, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::host));
            widgets.add(new TextWidget(screenHolder, topButton, NetTestGame.instance().fontRenderer48, "Host"));
    
            widgets.add(new FunctionalButtonWidget(screenHolder, middleButton, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::connect));
            widgets.add(new TextWidget(screenHolder, middleButton, NetTestGame.instance().fontRenderer48, "Connect"));
    
            widgets.add(new FunctionalButtonWidget(screenHolder, bottomButton, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::exit));
            widgets.add(new TextWidget(screenHolder, bottomButton, NetTestGame.instance().fontRenderer48, "Exit"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void host(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        try
        {
            NetTestGame.instance().hostManager = NetworkHelper.host(NetTestGame.instance().protocol,
                    new IHostEventsListener<Client>()
                    {
                        @Override
                        public void clientConnected(Client client)
                        {
                            if(screenHolder.getScreen() instanceof NetTestChatScreen chatScreen)
                            {
                                chatScreen.addMessage("Server",
                                        (client.getRole() == NetworkRole.HOST
                                                ? "Host" : "Client") + " connected!",
                                        System.currentTimeMillis());
                            }
                        }
                        
                        @Override
                        public void clientDisconnected(Client client)
                        {
                            if(screenHolder.getScreen() instanceof NetTestChatScreen chatScreen)
                            {
                                chatScreen.addMessage("Server",
                                        (client.getRole() == NetworkRole.HOST
                                                ? "Host" : "Client") + " disconnected!",
                                        System.currentTimeMillis());
                            }
                        }
                    }, Client.createFactory(), 100);
            NetTestGame.instance().clientManager = NetTestGame.instance().hostManager;
            NetTestGame.instance().setScreen(new NetTestChatScreen(this));
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
            NetTestGame.instance().clientManager = NetworkHelper.connect(NetTestGame.instance().protocol,
                    new IClientEventsListener()
                    {
                        @Override
                        public void disconnected()
                        {
                            NetTestGame.instance().setScreen(NetTestMenuScreen.this);
                        }
                        
                        @Override
                        public void disconnectedWithException(Exception e)
                        {
                            e.printStackTrace();
                            disconnected();
                        }
                    }, "localhost", 100);
            NetTestGame.instance().setScreen(new NetTestChatScreen(this));
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
