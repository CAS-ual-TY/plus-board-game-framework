package sweng_plus.framework_test.networking.manual_chat;

import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.StackedScreen;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Texture;
import sweng_plus.framework.userinterface.gui.util.TextureHelper;
import sweng_plus.framework.userinterface.gui.widget.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NetTestChatScreen extends StackedScreen
{
    public static final int CHAT_WIDTH = 500;
    
    public FunctionalTextWidget textWidget;
    public InputWidget inputWidget;
    
    public FontRenderer chatFontRenderer;
    
    public List<String> chatMessages;
    
    public NetTestChatScreen(Screen subScreen)
    {
        super(subScreen);
        
        chatFontRenderer = NetTestGame.instance().fontRenderer48;
        
        Dimensions leaveDimensions = new Dimensions(400, 100, AnchorPoint.TR);
        Dimensions sendDimensions = new Dimensions(100, 100, AnchorPoint.BR);
        
        try
        {
            Texture buttonActive = TextureHelper.createTexture("src/test/resources/textures/button_test_active.png");
            Texture buttonInactive = TextureHelper.createTexture("src/test/resources/textures/button_test_inactive.png");
            
            widgets.add(new FunctionalButtonWidget(this, leaveDimensions, buttonActive, buttonInactive, this::leave));
            widgets.add(new TextWidget(this, leaveDimensions, chatFontRenderer, Arrays.asList("Leave")));
            
            widgets.add(new FunctionalButtonWidget(this, sendDimensions, buttonActive, buttonInactive, this::sendMessage));
            widgets.add(new TextWidget(this, sendDimensions, chatFontRenderer, Arrays.asList(">")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        chatMessages = new LinkedList<>();
        
        textWidget = new FunctionalTextWidget(this, new Dimensions(AnchorPoint.BR, 0, -100), chatFontRenderer, this::getChat);
        widgets.add(textWidget);
        
        widgets.add(inputWidget = new InputWidget(this, new Dimensions(CHAT_WIDTH - 100, 100, AnchorPoint.BR, -100, 0), chatFontRenderer));
    }
    
    private void leave(int mouseX, int mouseY, int mods)
    {
        NetTestGame.instance().clientManager.close();
        returnToSubScreen();
    }
    
    public void addMessage(String sender, String message, long timestamp)
    {
        chatMessages.addAll(chatFontRenderer.splitStringToWidth(CHAT_WIDTH, sender + ": " + message));
        textWidget.adjustSizeToText();
        textWidget.init(getParentX(), getParentY(), getParentW(), getParentH());
    }
    
    public List<String> getChat()
    {
        return chatMessages;
    }
    
    public void sendMessage(int mouseX, int mouseY, int mods)
    {
        try
        {
            NetTestGame.instance().clientManager.sendMessageToServer(new NetTestMessage(inputWidget.getText(), System.currentTimeMillis()));
            inputWidget.clearText();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
