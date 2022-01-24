package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.gui.util.MillStyles;
import sweng_plus.boardgames.mill.gui.util.MillTextures;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.util.Dimensions;

public class MenuScreen extends Screen
{
    public MenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions hostDims = new Dimensions(700, 80, AnchorPoint.M, 0, 38);
        widgets.add(new FunctionalButtonWidget(screenHolder, hostDims, MillStyles.makeButtonStyle("Neues Spiel"), this::host));
        
        Dimensions connectDims = new Dimensions(700, 80, AnchorPoint.M, 0, 137);
        widgets.add(new FunctionalButtonWidget(screenHolder, connectDims, MillStyles.makeButtonStyle("Spiel beitreten"), this::connect));
        
        Dimensions optionsDims = new Dimensions(700, 80, AnchorPoint.M, 0, 242);
        widgets.add(new FunctionalButtonWidget(screenHolder, optionsDims, MillStyles.makeButtonStyle("Einstellungen"), this::options));
        
        Dimensions exitDims = new Dimensions(700, 80, AnchorPoint.M, 0, 347);
        widgets.add(new FunctionalButtonWidget(screenHolder, exitDims, MillStyles.makeButtonStyle("Beenden"), this::exit));
    }
    
    private void host()
    {
        screenHolder.setScreen(new MenuHostScreen(this));
    }
    
    private void connect()
    {
        screenHolder.setScreen(new MenuConnectScreen(this));
    }
    
    private void options()
    {
    
    }
    
    private void exit()
    {
        Engine.instance().close();
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        Color4f.NEUTRAL.glColor4f();
        int x = screenW / 2 - MillTextures.background.getWidth() / 2;
        int y = screenH / 2 - MillTextures.background.getHeight() / 2;
        MillTextures.background.render(x, y);
        
        Color4f.BLACK.glColor4f();
        x = screenW / 2 - MillTextures.logo.getWidth() / 2;
        y = screenH / 2 - MillTextures.logo.getHeight() / 2;
        MillTextures.logo.render(x, y - 250);
        
        super.render(deltaTick, mouseX, mouseY);
    }
}
