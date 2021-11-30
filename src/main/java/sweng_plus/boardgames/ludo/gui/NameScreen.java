package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.widget.TexturedButtonWidget;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class NameScreen extends Screen
{
    public NameScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions textDims = new Dimensions(0, 0, AnchorPoint.M, 0, 0);
        widgets.add(new TextWidget(screenHolder, textDims, Ludo.instance().fontRenderer64, Ludo.instance().names));
        
        Dimensions cancelDims = new Dimensions(350, 80, AnchorPoint.L, 100, 400);
        widgets.add(new TexturedButtonWidget(screenHolder, cancelDims, this::cancel, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, cancelDims, Ludo.instance().fontRenderer32, "Zurück zum Menü"));
    }
    
    private void cancel()
    {
        screenHolder.setScreen(new MenuScreen(screenHolder));
    }
}
