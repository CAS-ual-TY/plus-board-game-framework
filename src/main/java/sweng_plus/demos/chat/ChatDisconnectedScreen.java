package sweng_plus.demos.chat;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.util.Dimensions;

import java.util.List;

public class ChatDisconnectedScreen extends Screen
{
    public ChatDisconnectedScreen(IScreenHolder screenHolder, List<String> message)
    {
        super(screenHolder);
        
        Dimensions topButton = new Dimensions(1200, 100, AnchorPoint.M, 0, -75);
        Dimensions bottomButton = new Dimensions(400, 100, AnchorPoint.M, 0, 75);
        
        widgets.add(new SimpleWidget(screenHolder, topButton, new TextStyle(ChatMain.instance().fontRenderer, message, Color4f.BLACK)));
        widgets.add(new FunctionalButtonWidget(screenHolder, bottomButton, ChatMain.hoverStyle("Main Menu"), this::toMainMenu));
    }
    
    private void toMainMenu()
    {
        ChatMain.instance().clientManager = null;
        ChatMain.instance().hostManager = null;
        screenHolder.setScreen(new ChatMenuScreen(screenHolder));
    }
}
