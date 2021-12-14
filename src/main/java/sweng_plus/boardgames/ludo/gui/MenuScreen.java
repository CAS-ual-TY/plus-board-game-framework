package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class MenuScreen extends Screen
{
    public MenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions hostDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -415);
        widgets.add(new FunctionalButtonWidget(screenHolder, hostDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::host));
        widgets.add(new TextWidget(screenHolder, hostDims, Ludo.instance().fontRenderer32, "Neues Spiel", Color4f.BLACK));
        
        Dimensions connectDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -310);
        widgets.add(new FunctionalButtonWidget(screenHolder, connectDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::connect));
        widgets.add(new TextWidget(screenHolder, connectDims, Ludo.instance().fontRenderer32, "Spiel beitreten", Color4f.BLACK));
        
        Dimensions optionsDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -205);
        widgets.add(new FunctionalButtonWidget(screenHolder, optionsDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::options));
        widgets.add(new TextWidget(screenHolder, optionsDims, Ludo.instance().fontRenderer32, "Einstellungen", Color4f.BLACK));
        
        Dimensions exitDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -100);
        widgets.add(new FunctionalButtonWidget(screenHolder, exitDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::exit));
        widgets.add(new TextWidget(screenHolder, exitDims, Ludo.instance().fontRenderer32, "Beenden", Color4f.BLACK));
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
