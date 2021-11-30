package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.widget.TexturedButtonWidget;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class MenuScreen extends Screen
{
    public MenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions hostDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -415);
        widgets.add(new TexturedButtonWidget(screenHolder, hostDims, this::host, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, hostDims, Ludo.instance().fontRenderer32, "Neues Spiel"));
        
        Dimensions connectDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -310);
        widgets.add(new TexturedButtonWidget(screenHolder, connectDims, this::connect, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, connectDims, Ludo.instance().fontRenderer32, "Spiel beitreten"));
        
        Dimensions optionsDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -205);
        widgets.add(new TexturedButtonWidget(screenHolder, optionsDims, this::options, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, optionsDims, Ludo.instance().fontRenderer32, "Einstellungen"));
        
        Dimensions exitDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -100);
        widgets.add(new TexturedButtonWidget(screenHolder, exitDims, this::exit, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, exitDims, Ludo.instance().fontRenderer32, "Beenden"));
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
