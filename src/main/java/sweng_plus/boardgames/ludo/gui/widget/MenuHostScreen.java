package sweng_plus.boardgames.ludo.gui.widget;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.LudoTextures;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class MenuHostScreen extends WrappedScreen
{
    private String portInput;
    private InputWidget inputWidget;
    
    public MenuHostScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions textDims = new Dimensions(0, 0, AnchorPoint.M, 0, -80);
        widgets.add(new TextWidget(screenHolder, textDims, Ludo.instance().fontRenderer48, "Port eingeben:"));
        
        Dimensions inputDims = new Dimensions(500, 80, AnchorPoint.M, 0, 0);
        inputWidget = new InputWidget(screenHolder, inputDims, Ludo.instance().fontRenderer48);
        widgets.add(inputWidget);
        
        Dimensions cancelDims = new Dimensions(200, 50, AnchorPoint.M, -150, 150);
        widgets.add(new TexturedButtonWidget(screenHolder, cancelDims, this::cancel, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, cancelDims, Ludo.instance().fontRenderer32, "Abbruch"));
        
        Dimensions acceptDims = new Dimensions(200, 50, AnchorPoint.M, 150, 150);
        widgets.add(new TexturedButtonWidget(screenHolder, acceptDims, this::accept, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, acceptDims, Ludo.instance().fontRenderer32, "Fertig"));
    }
    
    private void accept()
    {
        portInput = inputWidget.getText();
        //TODO
    }
    
    private void cancel()
    {
        returnToSubScreen();
    }
}
