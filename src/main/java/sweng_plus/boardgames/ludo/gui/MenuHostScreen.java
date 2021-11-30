package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.widget.TexturedButtonWidget;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class MenuHostScreen extends WrappedScreen
{
    private String portInput;
    private InputWidget inputWidget;
    private InputWidget inputWidgetName;
    
    public MenuHostScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions nameDims = new Dimensions(0, 0, AnchorPoint.M, 0, -160);
        widgets.add(new TextWidget(screenHolder, nameDims, Ludo.instance().fontRenderer48, "Name:"));
        
        Dimensions inputDimsName = new Dimensions(500, 80, AnchorPoint.M, 0, -80);
        inputWidgetName = new InputWidget(screenHolder, inputDimsName, Ludo.instance().fontRenderer48);
        widgets.add(inputWidgetName);
        
        Dimensions textDims = new Dimensions(0, 0, AnchorPoint.M, 0, 20);
        widgets.add(new TextWidget(screenHolder, textDims, Ludo.instance().fontRenderer48, "Port:"));
        
        Dimensions inputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 100);
        inputWidget = new InputWidget(screenHolder, inputDims, Ludo.instance().fontRenderer48);
        widgets.add(inputWidget);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 200);
        widgets.add(new TexturedButtonWidget(screenHolder, cancelDims, this::cancel, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, cancelDims, Ludo.instance().fontRenderer32, "Abbruch"));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 200);
        widgets.add(new TexturedButtonWidget(screenHolder, acceptDims, this::accept, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, acceptDims, Ludo.instance().fontRenderer32, "Fertig"));
    }
    
    private void accept()
    {
        portInput = inputWidget.getText();
        //TODO
        try
        {
            //Ludo.instance().host(inputWidgetName.getText(), Integer.valueOf(portInput));
            Ludo.instance().host("Host", (int) 25555);
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
