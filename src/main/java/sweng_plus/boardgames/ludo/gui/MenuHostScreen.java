package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class MenuHostScreen extends WrappedScreen
{
    private InputWidget inputWidgetName;
    private InputWidget inputWidgetPort;
    
    public MenuHostScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions nameDims = new Dimensions(0, 0, AnchorPoint.M, 0, -160);
        widgets.add(new SimpleWidget(screenHolder, nameDims, new TextStyle(Ludo.instance().fontRenderer48, "Name:", Color4f.WHITE)));
        
        Dimensions inputDimsName = new Dimensions(500, 80, AnchorPoint.M, 0, -80);
        inputWidgetName = new InputWidget(screenHolder, inputDimsName, Ludo.instance().fontRenderer48)
                .setText("Host");
        widgets.add(inputWidgetName);
        
        Dimensions textDims = new Dimensions(0, 0, AnchorPoint.M, 0, 20);
        widgets.add(new SimpleWidget(screenHolder, textDims, new TextStyle(Ludo.instance().fontRenderer48, "Port:", Color4f.WHITE)));
        
        Dimensions inputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 100);
        inputWidgetPort = new InputWidget(screenHolder, inputDims, Ludo.instance().fontRenderer48)
                .setText("25555");
        widgets.add(inputWidgetPort);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 200);
        
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::cancel));
        widgets.add(new SimpleWidget(screenHolder, cancelDims, new TextStyle(Ludo.instance().fontRenderer32, "Abbruch", Color4f.BLACK)));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 200);
        widgets.add(new FunctionalButtonWidget(screenHolder, acceptDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::accept));
        widgets.add(new SimpleWidget(screenHolder, acceptDims, new TextStyle(Ludo.instance().fontRenderer32, "Fertig", Color4f.BLACK)));
    }
    
    private void accept()
    {
        String portInput = inputWidgetPort.getText();
        
        try
        {
            Ludo.instance().host(inputWidgetName.getText(), Integer.parseInt(portInput));
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
