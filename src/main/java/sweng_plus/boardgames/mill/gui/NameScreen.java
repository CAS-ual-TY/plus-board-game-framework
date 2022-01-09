package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gui.util.MillStyles;
import sweng_plus.boardgames.mill.gui.util.MillTextures;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
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
        
        Dimensions textDims = new Dimensions(800, 0, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, textDims, new TextStyle(Mill.instance().fontRenderer64, Mill.instance().names, Color4f.BLACK)));
        
        Dimensions cancelDims = new Dimensions(350, 80, AnchorPoint.L, 100, 400);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, MillStyles.makeButtonStyle("Zurück zum Menü"), this::cancel));
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        Color4f.NEUTRAL.glColor4f();
        int x = screenW / 2 - MillTextures.background.getWidth() / 2;
        int y = screenH / 2 - MillTextures.background.getHeight() / 2;
        MillTextures.background.render(x, y);
        
        super.render(deltaTick, mouseX, mouseY);
    }
    
    private void cancel()
    {
        screenHolder.setScreen(new MenuScreen(screenHolder));
    }
}
