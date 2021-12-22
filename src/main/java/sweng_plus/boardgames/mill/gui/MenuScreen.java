package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.gui.MenuConnectScreen;
import sweng_plus.boardgames.mill.gui.MenuHostScreen;
import sweng_plus.boardgames.mill.gui.util.MillStyles;
import sweng_plus.boardgames.mill.gui.util.MillTextures;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class MenuScreen extends Screen
{
    public MenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions quadDims = new Dimensions(950, 950, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, quadDims, MillStyles.makeButtonStyle("")));
        
        Dimensions hostDims = new Dimensions(700, 80, AnchorPoint.M, 0, -8);
        widgets.add(new FunctionalButtonWidget(screenHolder, hostDims, MillStyles.makeButtonStyle("Neues Spiel"), this::host));
        
        Dimensions connectDims = new Dimensions(700, 80, AnchorPoint.M, 0, 97);
        widgets.add(new FunctionalButtonWidget(screenHolder, connectDims, MillStyles.makeButtonStyle("Spiel beitreten"), this::connect));
        
        Dimensions optionsDims = new Dimensions(700, 80, AnchorPoint.M, 0, 202);
        widgets.add(new FunctionalButtonWidget(screenHolder, optionsDims, MillStyles.makeButtonStyle("Einstellungen"), this::options));
        
        Dimensions exitDims = new Dimensions(700, 80, AnchorPoint.M, 0, 307);
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

}
