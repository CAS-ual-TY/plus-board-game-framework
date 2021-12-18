package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class NameScreen extends Screen
{
    public NameScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions textDims = new Dimensions(0, 0, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, textDims, new TextStyle(Ludo.instance().fontRenderer64, Ludo.instance().names, Color4f.BLACK)));
        
        Dimensions cancelDims = new Dimensions(350, 80, AnchorPoint.L, 100, 400);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::cancel));
        widgets.add(new SimpleWidget(screenHolder, cancelDims, new TextStyle(Ludo.instance().fontRenderer32, "Zurück zum Menü", Color4f.BLACK)));
    }
    
    private void cancel()
    {
        screenHolder.setScreen(new MenuScreen(screenHolder));
    }
}
