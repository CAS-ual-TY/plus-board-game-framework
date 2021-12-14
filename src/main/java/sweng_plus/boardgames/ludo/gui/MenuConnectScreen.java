package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class MenuConnectScreen extends WrappedScreen
{
    private String portInput;
    private String ipInput;
    private InputWidget inputWidgetPort;
    private InputWidget inputWidgetIP;
    private InputWidget inputWidgetName;
    
    public MenuConnectScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions nameTextDims = new Dimensions(0, 0, AnchorPoint.M, 0, -260);
        widgets.add(new TextWidget(screenHolder, nameTextDims, Ludo.instance().fontRenderer48, "Name:", Color4f.BLACK));
        
        Dimensions nameInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, -180);
        inputWidgetName = new InputWidget(screenHolder, nameInputDims, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetName);
        
        Dimensions ipTextDims = new Dimensions(0, 0, AnchorPoint.M, 0, -80);
        widgets.add(new TextWidget(screenHolder, ipTextDims, Ludo.instance().fontRenderer48, "IP:", Color4f.BLACK));
        
        Dimensions ipInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 0);
        inputWidgetIP = new InputWidget(screenHolder, ipInputDims, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetIP);
        
        Dimensions portTextDims = new Dimensions(0, 0, AnchorPoint.M, 0, 100);
        widgets.add(new TextWidget(screenHolder, portTextDims, Ludo.instance().fontRenderer48, "Port:", Color4f.BLACK));
        
        Dimensions portInputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 180);
        inputWidgetPort = new InputWidget(screenHolder, portInputDims, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetPort);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::cancel));
        widgets.add(new TextWidget(screenHolder, cancelDims, Ludo.instance().fontRenderer32, "Abbruch", Color4f.BLACK));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::accept));
        widgets.add(new TextWidget(screenHolder, acceptDims, Ludo.instance().fontRenderer32, "Fertig", Color4f.BLACK));
    }
    
    private void accept()
    {
        portInput = inputWidgetPort.getText();
        ipInput = inputWidgetIP.getText();
        //TODO
        try
        {
            //Ludo.instance().connect(inputWidgetName.getText(), ipInput, Integer.valueOf(portInput));
            Ludo.instance().connect(inputWidgetName.getText(), "localhost", 25555);
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
