package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
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
        
        Dimensions nameDims = new Dimensions(0, 0, AnchorPoint.M, 0, -260);
        widgets.add(new TextWidget(screenHolder, nameDims, Ludo.instance().fontRenderer48, "Name:"));
        
        Dimensions inputDimsName = new Dimensions(500, 80, AnchorPoint.M, 0, -180);
        inputWidgetName = new InputWidget(screenHolder, inputDimsName, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetName);
        
        Dimensions textDims = new Dimensions(0, 0, AnchorPoint.M, 0, -80);
        widgets.add(new TextWidget(screenHolder, textDims, Ludo.instance().fontRenderer48, "Port:"));
        
        Dimensions inputDimsPort = new Dimensions(500, 80, AnchorPoint.M, 0, 0);
        inputWidgetPort = new InputWidget(screenHolder, inputDimsPort, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetPort);
        
        Dimensions textDimsIP = new Dimensions(0, 0, AnchorPoint.M, 0, 100);
        widgets.add(new TextWidget(screenHolder, textDimsIP, Ludo.instance().fontRenderer48, "IP:"));
        
        Dimensions inputDimsIP = new Dimensions(500, 80, AnchorPoint.M, 0, 180);
        inputWidgetIP = new InputWidget(screenHolder, inputDimsIP, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetIP);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::cancel));
        widgets.add(new TextWidget(screenHolder, cancelDims, Ludo.instance().fontRenderer32, "Abbruch"));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 280);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::accept));
        widgets.add(new TextWidget(screenHolder, acceptDims, Ludo.instance().fontRenderer32, "Fertig"));
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
