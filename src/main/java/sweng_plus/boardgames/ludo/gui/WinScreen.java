package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class WinScreen extends LudoExtensionScreen
{
    public WinScreen(LudoScreen subScreen, String winner)
    {
        super(subScreen);
        
        Dimensions nameTextDims = new Dimensions(0, 0, AnchorPoint.M, 0, 0);
        widgets.add(new TextWidget(screenHolder, nameTextDims, Ludo.instance().fontRenderer64, winner + " hat Gewonnen!", Color4f.WHITE));
    }
}
