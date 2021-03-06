package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gui.util.MillStyles;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.util.Dimensions;

import java.io.IOException;
import java.util.Random;

public class MenuConnectScreen extends WrappedScreen
{
    private InputWidget inputWidgetName;
    private InputWidget inputWidgetIP;
    private InputWidget inputWidgetPort;
    
    public MenuConnectScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions nameTextDims = new Dimensions(400, 0, AnchorPoint.M, 0, -260);
        widgets.add(new SimpleWidget(screenHolder, nameTextDims, new TextStyle(Mill.instance().fontRenderer48, "Name:", Color4f.WHITE)));
        
        Dimensions nameInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -180);
        inputWidgetName = new InputWidget(screenHolder, nameInputDims, MillStyles.makeActiveInputStyle(() -> inputWidgetName), MillStyles.makeInactiveInputStyle(() -> inputWidgetName))
                .setText(generateRandomPlayer());
        widgets.add(inputWidgetName);
        
        Dimensions ipTextDims = new Dimensions(400, 0, AnchorPoint.M, 0, -80);
        widgets.add(new SimpleWidget(screenHolder, ipTextDims, new TextStyle(Mill.instance().fontRenderer48, "IP:", Color4f.WHITE)));
        
        Dimensions ipInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 0);
        inputWidgetIP = new InputWidget(screenHolder, ipInputDims, MillStyles.makeActiveInputStyle(() -> inputWidgetIP), MillStyles.makeInactiveInputStyle(() -> inputWidgetIP))
                .setText("localhost");
        widgets.add(inputWidgetIP);
        
        Dimensions portTextDims = new Dimensions(400, 0, AnchorPoint.M, 0, 100);
        widgets.add(new SimpleWidget(screenHolder, portTextDims, new TextStyle(Mill.instance().fontRenderer48, "Port:", Color4f.WHITE)));
        
        Dimensions portInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 180);
        inputWidgetPort = new InputWidget(screenHolder, portInputDims, MillStyles.makeActiveInputStyle(() -> inputWidgetPort), MillStyles.makeInactiveInputStyle(() -> inputWidgetPort))
                .setText("25555");
        widgets.add(inputWidgetPort);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, MillStyles.makeButtonStyle("Abbruch"), this::cancel));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, MillStyles.makeButtonStyle("Fertig"), this::accept));
    }
    
    private void accept()
    {
        String ipInput = inputWidgetIP.getText();
        String portInput = inputWidgetPort.getText();
        
        try
        {
            Mill.instance().getNetworking().connect(inputWidgetName.getText(), ipInput, Integer.parseInt(portInput));
        }
        catch(IOException | NumberFormatException e)
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
