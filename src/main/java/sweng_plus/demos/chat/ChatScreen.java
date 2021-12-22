package sweng_plus.demos.chat;

import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.StackedScreen;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.style.FunctionalTextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.LinkedList;
import java.util.List;

public class ChatScreen extends StackedScreen
{
    public static final int CHAT_WIDTH = 1000;
    
    public InputWidget inputWidget;
    public Widget chatWidget;
    
    public FontRenderer chatFontRenderer;
    
    public List<String> chatMessages;
    
    public ChatScreen(Screen subScreen)
    {
        super(subScreen);
        
        chatFontRenderer = ChatGame.instance().fontRenderer48;
        
        Dimensions leaveDimensions = new Dimensions(400, 100, AnchorPoint.BL);
        Dimensions sendDimensions = new Dimensions(100, 100, AnchorPoint.BR);
        
        widgets.add(new FunctionalButtonWidget(screenHolder, leaveDimensions, ChatGame.hoverStyle("Leave"), this::leave));
        widgets.add(new FunctionalButtonWidget(screenHolder, sendDimensions, ChatGame.hoverStyle(">"), this::sendMessage));
        
        chatMessages = new LinkedList<>();
        
        widgets.add(chatWidget = new SimpleWidget(screenHolder, new Dimensions(CHAT_WIDTH, 100, AnchorPoint.BR, 0, -100), new FunctionalTextStyle(chatFontRenderer, this::getChat, AnchorPoint.BL, Color4f.BLACK)));
        widgets.add(inputWidget = new InputWidget(screenHolder, new Dimensions(CHAT_WIDTH - 100, 100, AnchorPoint.BR, -100, 0),
                ChatGame.activeStyle(() -> inputWidget.getTextAsList(), AnchorPoint.L),
                ChatGame.inactiveStyle(() -> inputWidget.getTextAsList(), AnchorPoint.L), this::enter));
    }
    
    @Override
    public void initScreen(int screenW, int screenH)
    {
        chatWidget.getDimensions().w = screenW - 400;
        chatWidget.getDimensions().h = screenH - 100;
        
        inputWidget.getDimensions().w = screenW - 400 - 100;
        
        super.initScreen(screenW, screenH);
    }
    
    private void enter(InputWidget inputWidget)
    {
        sendMessage();
    }
    
    private void leave(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods)
    {
        ChatGame.instance().clientManager.close();
        returnToSubScreen();
    }
    
    public void addMessage(String sender, String message, long timestamp)
    {
        if(!sender.isEmpty())
        {
            message = sender + ": " + message;
        }
        
        chatMessages.addAll(chatFontRenderer.splitStringToWidth(CHAT_WIDTH, message));
    }
    
    public List<String> getChat()
    {
        return chatMessages;
    }
    
    public void sendMessage()
    {
        if(!inputWidget.getText().trim().isEmpty())
        {
            ChatGame.instance().clientManager.sendMessageToServer(new ChatMessage(inputWidget.getText().trim(), System.currentTimeMillis()));
        }
        
        inputWidget.clearText();
    }
}