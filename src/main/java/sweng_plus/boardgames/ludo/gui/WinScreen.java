package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.boardgame.I18n;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class WinScreen extends LudoExtensionScreen
{
    public WinScreen(LudoScreen subScreen, String winner)
    {
        super(subScreen);
        
        Dimensions nameTextDims = new Dimensions(800, 0, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, nameTextDims, new TextStyle(Ludo.instance().fontRenderer64, I18n.translate("ludo.win.prefix") + " " + winner + " " + I18n.translate("ludo.win.suffix"), Color4f.WHITE)));
    }
}
