package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.framework.userinterface.gui.style.ColoredQuadStyle;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class WinScreen extends MillExtensionScreen
{
    public WinScreen(MillScreen subScreen, String winner)
    {
        super(subScreen);
        
        Dimensions quadDims = new Dimensions(4000, 200, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, quadDims, new ColoredQuadStyle(new Color4f(232, 225, 218))));
        Dimensions nameTextDims = new Dimensions(800, 0, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, nameTextDims, new TextStyle(Mill.instance().fontRenderer64, winner + " hat Gewonnen!", Color4f.BLACK)));
    }
}
