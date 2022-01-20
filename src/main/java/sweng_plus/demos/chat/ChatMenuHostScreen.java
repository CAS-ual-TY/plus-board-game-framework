package sweng_plus.demos.chat;

import sweng_plus.framework.networking.NetworkHelper;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.StackedScreen;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class ChatMenuHostScreen extends StackedScreen
{
    private InputWidget inputWidgetName;
    private InputWidget inputWidgetPort;
    
    public ChatMenuHostScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions nameDims = new Dimensions(400, 80, AnchorPoint.M, 0, -280);
        widgets.add(new SimpleWidget(screenHolder, nameDims, new TextStyle(ChatMain.instance().fontRenderer, "Name:", Color4f.WHITE)));
        
        Dimensions inputDimsName = new Dimensions(500, 80, AnchorPoint.M, 0, -200);
        inputWidgetName = new InputWidget(screenHolder, inputDimsName, ChatMain.activeStyle(() -> inputWidgetName.getTextAsList()), ChatMain.inactiveStyle(() -> inputWidgetName.getTextAsList()))
                .setText("Host");
        widgets.add(inputWidgetName);
        
        Dimensions textDims = new Dimensions(400, 80, AnchorPoint.M, 0, -100);
        widgets.add(new SimpleWidget(screenHolder, textDims, new TextStyle(ChatMain.instance().fontRenderer, "Port:", Color4f.WHITE)));
        
        Dimensions inputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -20);
        inputWidgetPort = new InputWidget(screenHolder, inputDims, ChatMain.activeStyle(() -> inputWidgetPort.getTextAsList()), ChatMain.inactiveStyle(() -> inputWidgetPort.getTextAsList()))
                .setText("25555");
        widgets.add(inputWidgetPort);
        
        Dimensions acceptDims = new Dimensions(500, 80, AnchorPoint.M, 0, 180);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, ChatMain.hoverStyle("Host"), this::accept));
        
        Dimensions cancelDims = new Dimensions(500, 80, AnchorPoint.M, 0, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, ChatMain.hoverStyle("Cancel"), this::cancel));
    }
    
    private void accept()
    {
        String portInput = inputWidgetPort.getText();
        String nameInput = inputWidgetName.getText();
        
        ChatMain.instance().name = nameInput;
        
        try
        {
            ChatMain.instance().hostManager = NetworkHelper.advancedHost(ChatMain.instance().protocol,
                    ChatMain.instance().listener, ChatClient::new, nameInput, Integer.parseInt(portInput));
            ChatMain.instance().clientManager = ChatMain.instance().hostManager;
            ChatMain.instance().setScreen(new ChatScreen(screenHolder));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void cancel()
    {
        returnToSubScreen();
    }
}
