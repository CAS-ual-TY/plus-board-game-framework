package sweng_plus.framework_test.networking.manual_chat;

import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.StackedScreen;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.FunctionalTextStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class NetTestChatScreen extends StackedScreen
{
    public static final int CHAT_WIDTH = 1000;
    
    public InputWidget inputWidget;
    public Widget chatWidget;
    
    public FontRenderer chatFontRenderer;
    
    public List<String> chatMessages;
    
    public NetTestChatScreen(Screen subScreen)
    {
        super(subScreen);
        
        chatFontRenderer = NetTestGame.instance().fontRenderer48;
        
        Dimensions leaveDimensions = new Dimensions(400, 100, AnchorPoint.BL);
        Dimensions sendDimensions = new Dimensions(100, 100, AnchorPoint.BR);
        
        try
        {
            Texture buttonActive = TextureHelper.createTexture("/textures/button_test_active.png");
            Texture buttonInactive = TextureHelper.createTexture("/textures/button_test_inactive.png");
            
            widgets.add(new FunctionalButtonWidget(screenHolder, leaveDimensions, new HoverStyle(new CorneredTextureStyle(buttonInactive), new CorneredTextureStyle(buttonActive)), this::leave));
            widgets.add(new SimpleWidget(screenHolder, leaveDimensions, new TextStyle(chatFontRenderer, "Leave")));
            
            widgets.add(new FunctionalButtonWidget(screenHolder, sendDimensions, new HoverStyle(new CorneredTextureStyle(buttonInactive), new CorneredTextureStyle(buttonActive)), this::sendMessage));
            widgets.add(new SimpleWidget(screenHolder, sendDimensions, new TextStyle(chatFontRenderer, ">")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        chatMessages = new LinkedList<>();
        
        widgets.add(chatWidget = new SimpleWidget(screenHolder, new Dimensions(CHAT_WIDTH, 100, AnchorPoint.BR, 0, -100), new FunctionalTextStyle(chatFontRenderer, this::getChat, AnchorPoint.BL)));
        
        widgets.add(inputWidget = new InputWidget(screenHolder, new Dimensions(CHAT_WIDTH - 100, 100, AnchorPoint.BR, -100, 0), new FunctionalTextStyle(chatFontRenderer, () -> inputWidget.getTextAsList(), AnchorPoint.L), new FunctionalTextStyle(chatFontRenderer, () -> inputWidget.getTextAsList(), AnchorPoint.L)));
    }
    
    @Override
    public void initScreen(int screenW, int screenH)
    {
        chatWidget.getDimensions().h = screenH - 100;
        
        super.initScreen(screenW, screenH);
    }
    
    private void leave(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        NetTestGame.instance().clientManager.close();
        returnToSubScreen();
    }
    
    public void addMessage(String sender, String message, long timestamp)
    {
        chatMessages.addAll(chatFontRenderer.splitStringToWidth(CHAT_WIDTH, sender + ": " + message));
    }
    
    public List<String> getChat()
    {
        return chatMessages;
    }
    
    public void sendMessage(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        NetTestGame.instance().clientManager.sendMessageToServer(new NetTestMessage(inputWidget.getText(), System.currentTimeMillis()));
        inputWidget.clearText();
    }
}
