package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.widget.TexturedButtonWidget;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class MenuConnectScreen extends WrappedScreen
{
    private String portInput;
    private String ipInput;
    private InputWidget inputWidgetPort;
    private InputWidget inputWidgetIP;
    
    public MenuConnectScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions textDims = new Dimensions(0, 0, AnchorPoint.M, 0, -160);
        widgets.add(new TextWidget(screenHolder, textDims, Ludo.instance().fontRenderer48, "Port eingeben:"));
        
        Dimensions inputDimsPort = new Dimensions(500, 80, AnchorPoint.M, 0, -80);
        inputWidgetPort = new InputWidget(screenHolder, inputDimsPort, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetPort);
        
        Dimensions textDimsIP = new Dimensions(0, 0, AnchorPoint.M, 0, 20);
        widgets.add(new TextWidget(screenHolder, textDimsIP, Ludo.instance().fontRenderer48, "IP eingeben:"));
        
        Dimensions inputDimsIP = new Dimensions(500, 80, AnchorPoint.M, 0, 100);
        inputWidgetIP = new InputWidget(screenHolder, inputDimsIP, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetIP);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 200);
        widgets.add(new TexturedButtonWidget(screenHolder, cancelDims, this::cancel, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, cancelDims, Ludo.instance().fontRenderer32, "Abbruch"));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 200);
        widgets.add(new TexturedButtonWidget(screenHolder, acceptDims, this::accept, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, acceptDims, Ludo.instance().fontRenderer32, "Fertig"));
    }
    
    private void accept()
    {
        portInput = inputWidgetPort.getText();
        ipInput = inputWidgetIP.getText();
        //TODO
    }
    
    private void cancel()
    {
        returnToSubScreen();
    }
}
