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
import java.util.Random;

public class ChatMenuConnectScreen extends StackedScreen
{
    private InputWidget inputWidgetName;
    private InputWidget inputWidgetIP;
    private InputWidget inputWidgetPort;
    
    public ChatMenuConnectScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions nameTextDims = new Dimensions(400, 80, AnchorPoint.M, 0, -360);
        widgets.add(new SimpleWidget(screenHolder, nameTextDims, new TextStyle(ChatGame.instance().fontRenderer, "Name:", Color4f.WHITE)));
        
        Dimensions nameInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -280);
        inputWidgetName = new InputWidget(screenHolder, nameInputDims, ChatGame.activeStyle(() -> inputWidgetName.getTextAsList()), ChatGame.inactiveStyle(() -> inputWidgetName.getTextAsList()))
                .setText(generateRandomPlayer());
        widgets.add(inputWidgetName);
        
        Dimensions ipTextDims = new Dimensions(400, 80, AnchorPoint.M, 0, -180);
        widgets.add(new SimpleWidget(screenHolder, ipTextDims, new TextStyle(ChatGame.instance().fontRenderer, "IP:", Color4f.WHITE)));
        
        Dimensions ipInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -100);
        inputWidgetIP = new InputWidget(screenHolder, ipInputDims, ChatGame.activeStyle(() -> inputWidgetIP.getTextAsList()), ChatGame.inactiveStyle(() -> inputWidgetIP.getTextAsList()))
                .setText("localhost");
        widgets.add(inputWidgetIP);
        
        Dimensions portTextDims = new Dimensions(400, 80, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, portTextDims, new TextStyle(ChatGame.instance().fontRenderer, "Port:", Color4f.WHITE)));
        
        Dimensions portInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 80);
        inputWidgetPort = new InputWidget(screenHolder, portInputDims, ChatGame.activeStyle(() -> inputWidgetPort.getTextAsList()), ChatGame.inactiveStyle(() -> inputWidgetPort.getTextAsList()))
                .setText("25555");
        widgets.add(inputWidgetPort);
        
        Dimensions acceptDims = new Dimensions(500, 80, AnchorPoint.M, 0, 260);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, ChatGame.hoverStyle("Connect"), this::accept));
        
        Dimensions cancelDims = new Dimensions(500, 80, AnchorPoint.M, 0, 360);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, ChatGame.hoverStyle("Cancel"), this::cancel));
    }
    
    private void accept()
    {
        ChatGame.instance().name = inputWidgetName.getText();
        
        String ipInput = inputWidgetIP.getText();
        String portInput = inputWidgetPort.getText();
        
        try
        {
            ChatGame.instance().clientManager = NetworkHelper.advancedConnect(ChatGame.instance().protocol,
                    ChatGame.instance().listener, ipInput, Integer.parseInt(portInput));
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
    
    public static String generateRandomPlayer()
    {
        return "Player" + (new Random().nextInt(999) + 1);
    }
}
