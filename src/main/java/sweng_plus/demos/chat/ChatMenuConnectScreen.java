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
        widgets.add(new SimpleWidget(screenHolder, nameTextDims, new TextStyle(ChatMain.instance().fontRenderer, "Name:", Color4f.WHITE)));
        
        Dimensions nameInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -280);
        inputWidgetName = new InputWidget(screenHolder, nameInputDims, ChatMain.activeStyle(() -> inputWidgetName.getTextAsList()), ChatMain.inactiveStyle(() -> inputWidgetName.getTextAsList()))
                .setText(generateRandomPlayer());
        widgets.add(inputWidgetName);
        
        Dimensions ipTextDims = new Dimensions(400, 80, AnchorPoint.M, 0, -180);
        widgets.add(new SimpleWidget(screenHolder, ipTextDims, new TextStyle(ChatMain.instance().fontRenderer, "IP:", Color4f.WHITE)));
        
        Dimensions ipInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -100);
        inputWidgetIP = new InputWidget(screenHolder, ipInputDims, ChatMain.activeStyle(() -> inputWidgetIP.getTextAsList()), ChatMain.inactiveStyle(() -> inputWidgetIP.getTextAsList()))
                .setText("localhost");
        widgets.add(inputWidgetIP);
        
        Dimensions portTextDims = new Dimensions(400, 80, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, portTextDims, new TextStyle(ChatMain.instance().fontRenderer, "Port:", Color4f.WHITE)));
        
        Dimensions portInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 80);
        inputWidgetPort = new InputWidget(screenHolder, portInputDims, ChatMain.activeStyle(() -> inputWidgetPort.getTextAsList()), ChatMain.inactiveStyle(() -> inputWidgetPort.getTextAsList()))
                .setText("25555");
        widgets.add(inputWidgetPort);
        
        Dimensions acceptDims = new Dimensions(500, 80, AnchorPoint.M, 0, 260);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, ChatMain.hoverStyle("Connect"), this::accept));
        
        Dimensions cancelDims = new Dimensions(500, 80, AnchorPoint.M, 0, 360);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, ChatMain.hoverStyle("Cancel"), this::cancel));
    }
    
    private void accept()
    {
        String nameInput = inputWidgetName.getText();
        String ipInput = inputWidgetIP.getText();
        String portInput = inputWidgetPort.getText();
        
        ChatMain.instance().name = nameInput;
        
        try
        {
            ChatMain.instance().clientManager = NetworkHelper.advancedConnect(ChatMain.instance().protocol,
                    ChatMain.instance().listener, nameInput, ipInput, Integer.parseInt(portInput));
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
    
    public static String generateRandomPlayer()
    {
        return "Player" + (new Random().nextInt(999) + 1);
    }
}
