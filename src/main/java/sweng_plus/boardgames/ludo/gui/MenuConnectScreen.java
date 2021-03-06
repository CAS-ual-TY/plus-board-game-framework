package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoStyles;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.boardgame.gui.style.I18NStyle;
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
        widgets.add(new SimpleWidget(screenHolder, nameTextDims, new I18NStyle(Ludo.instance().fontRenderer48, "menu.connect.name", Color4f.WHITE)));
        
        Dimensions nameInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -180);
        inputWidgetName = new InputWidget(screenHolder, nameInputDims, LudoStyles.makeActiveInputStyle(() -> inputWidgetName), LudoStyles.makeInactiveInputStyle(() -> inputWidgetName))
                .setText(generateRandomPlayer());
        widgets.add(inputWidgetName);
        
        Dimensions ipTextDims = new Dimensions(400, 0, AnchorPoint.M, 0, -80);
        widgets.add(new SimpleWidget(screenHolder, ipTextDims, new I18NStyle(Ludo.instance().fontRenderer48, "menu.connect.ip", Color4f.WHITE)));
        
        Dimensions ipInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 0);
        inputWidgetIP = new InputWidget(screenHolder, ipInputDims, LudoStyles.makeActiveInputStyle(() -> inputWidgetIP), LudoStyles.makeInactiveInputStyle(() -> inputWidgetIP))
                .setText("localhost");
        widgets.add(inputWidgetIP);
        
        Dimensions portTextDims = new Dimensions(400, 0, AnchorPoint.M, 0, 100);
        widgets.add(new SimpleWidget(screenHolder, portTextDims, new I18NStyle(Ludo.instance().fontRenderer48, "menu.connect.port", Color4f.WHITE)));
        
        Dimensions portInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 180);
        inputWidgetPort = new InputWidget(screenHolder, portInputDims, LudoStyles.makeActiveInputStyle(() -> inputWidgetPort), LudoStyles.makeInactiveInputStyle(() -> inputWidgetPort))
                .setText("25555");
        widgets.add(inputWidgetPort);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, LudoStyles.makeButtonStyle("menu.cancel"), this::cancel));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, LudoStyles.makeButtonStyle("menu.ready"), this::accept));
    }
    
    private void accept()
    {
        String ipInput = inputWidgetIP.getText();
        String portInput = inputWidgetPort.getText();
        
        try
        {
            Ludo.instance().getNetworking().connect(inputWidgetName.getText(), ipInput, Integer.parseInt(portInput));
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
