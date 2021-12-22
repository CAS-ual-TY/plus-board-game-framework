package sweng_plus.demos.chat;

import sweng_plus.framework.networking.Client;
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
        widgets.add(new SimpleWidget(screenHolder, nameDims, new TextStyle(ChatGame.instance().fontRenderer, "Name:", Color4f.WHITE)));
        
        Dimensions inputDimsName = new Dimensions(500, 80, AnchorPoint.M, 0, -200);
        inputWidgetName = new InputWidget(screenHolder, inputDimsName, ChatGame.activeStyle(() -> inputWidgetName.getTextAsList()), ChatGame.inactiveStyle(() -> inputWidgetName.getTextAsList()))
                .setText("Host");
        widgets.add(inputWidgetName);
        
        Dimensions textDims = new Dimensions(400, 80, AnchorPoint.M, 0, -100);
        widgets.add(new SimpleWidget(screenHolder, textDims, new TextStyle(ChatGame.instance().fontRenderer, "Port:", Color4f.WHITE)));
        
        Dimensions inputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -20);
        inputWidgetPort = new InputWidget(screenHolder, inputDims, ChatGame.activeStyle(() -> inputWidgetPort.getTextAsList()), ChatGame.inactiveStyle(() -> inputWidgetPort.getTextAsList()))
                .setText("25555");
        widgets.add(inputWidgetPort);
        
        Dimensions acceptDims = new Dimensions(500, 80, AnchorPoint.M, 0, 180);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, ChatGame.hoverStyle("Host"), this::accept));
        
        Dimensions cancelDims = new Dimensions(500, 80, AnchorPoint.M, 0, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, ChatGame.hoverStyle("Cancel"), this::cancel));
    }
    
    private void accept()
    {
        String portInput = inputWidgetPort.getText();
        
        try
        {
            ChatGame.instance().hostManager = NetworkHelper.advancedHost(ChatGame.instance().protocol,
                    ChatGame.instance().listener, Client.createFactory(), Integer.parseInt(portInput));
            ChatGame.instance().clientManager = ChatGame.instance().hostManager;
            ChatGame.instance().setScreen(new ChatScreen(screenHolder));
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
