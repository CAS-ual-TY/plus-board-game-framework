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

public class MenuHostScreen extends WrappedScreen
{
    private InputWidget inputWidgetName;
    private InputWidget inputWidgetPort;
    
    public MenuHostScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions nameDims = new Dimensions(400, 0, AnchorPoint.M, 0, -160);
        widgets.add(new SimpleWidget(screenHolder, nameDims, new TextStyle(Mill.instance().fontRenderer48, "Name:", Color4f.WHITE)));
        
        Dimensions inputDimsName = new Dimensions(500, 80, AnchorPoint.M, 0, -80);
        inputWidgetName = new InputWidget(screenHolder, inputDimsName, MillStyles.makeActiveInputStyle(() -> inputWidgetName), MillStyles.makeInactiveInputStyle(() -> inputWidgetName))
                .setText("Host");
        widgets.add(inputWidgetName);
        
        Dimensions textDims = new Dimensions(400, 0, AnchorPoint.M, 0, 20);
        widgets.add(new SimpleWidget(screenHolder, textDims, new TextStyle(Mill.instance().fontRenderer48, "Port:", Color4f.WHITE)));
        
        Dimensions inputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 100);
        inputWidgetPort = new InputWidget(screenHolder, inputDims, MillStyles.makeActiveInputStyle(() -> inputWidgetPort), MillStyles.makeInactiveInputStyle(() -> inputWidgetPort))
                .setText("25555");
        widgets.add(inputWidgetPort);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 200);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, MillStyles.makeButtonStyle("Abbruch"), this::cancel));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 200);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, MillStyles.makeButtonStyle("Fertig"), this::accept));
    }
    
    private void accept()
    {
        String portInput = inputWidgetPort.getText();
        
        try
        {
            Mill.instance().getNetworking().host(inputWidgetName.getText(), Integer.parseInt(portInput));
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
}
