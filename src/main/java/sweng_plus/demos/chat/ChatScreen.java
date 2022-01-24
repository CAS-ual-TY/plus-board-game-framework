package sweng_plus.demos.chat;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.style.FunctionalTextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.LinkedList;
import java.util.List;

public class ChatScreen extends Screen
{
    public static final int CHAT_WIDTH = 1000;
    
    public InputWidget inputWidget;
    public Widget chatWidget;
    
    public FontRenderer chatFontRenderer;
    
    public List<String> chatMessages;
    
    public ChatScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        chatFontRenderer = ChatMain.instance().fontRenderer;
        
        Dimensions leaveDimensions = new Dimensions(400, 100, AnchorPoint.BL);
        Dimensions sendDimensions = new Dimensions(100, 100, AnchorPoint.BR);
        
        widgets.add(new FunctionalButtonWidget(screenHolder, leaveDimensions, ChatMain.hoverStyle("Leave"), this::leave));
        widgets.add(new FunctionalButtonWidget(screenHolder, sendDimensions, ChatMain.hoverStyle(">"), this::sendMessage));
        
        chatMessages = new LinkedList<>();
        
        widgets.add(chatWidget = new SimpleWidget(screenHolder, new Dimensions(CHAT_WIDTH, 100, AnchorPoint.BR, 0, -100), new FunctionalTextStyle(chatFontRenderer, this::getChat, AnchorPoint.BL, Color4f.BLACK)));
        widgets.add(inputWidget = new InputWidget(screenHolder, new Dimensions(CHAT_WIDTH - 100, 100, AnchorPoint.BR, -100, 0),
                ChatMain.activeStyle(() -> inputWidget.getTextAsList(), AnchorPoint.L),
                ChatMain.inactiveStyle(() -> inputWidget.getTextAsList(), AnchorPoint.L), this::enter));
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
        ChatMain.instance().clientManager.close();
        screenHolder.setScreen(new ChatDisconnectedScreen(screenHolder,
                ChatMain.instance().hostManager != null ? List.of("Closed the server.") : List.of("Disconnected.")));
    }
    
    public void addMessage(String sender, String message, long timestamp)
    {
        if(sender != null && !sender.isEmpty())
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
        String text = inputWidget.getText().trim();
        
        if(!text.isEmpty())
        {
            ChatMain.instance().clientManager.sendMessageToServer(ChatMessage.clientToServer(text));
        }
        
        inputWidget.clearText();
    }
}
