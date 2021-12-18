package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.boardgame.Engine;
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

public class MenuScreen extends Screen
{
    public MenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions hostDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -415);
        widgets.add(new FunctionalButtonWidget(screenHolder, hostDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::host));
        widgets.add(new SimpleWidget(screenHolder, hostDims, new TextStyle(Ludo.instance().fontRenderer32, "Neues Spiel", Color4f.BLACK)));
        
        Dimensions connectDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -310);
        widgets.add(new FunctionalButtonWidget(screenHolder, connectDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::connect));
        widgets.add(new SimpleWidget(screenHolder, connectDims, new TextStyle(Ludo.instance().fontRenderer32, "Spiel beitreten", Color4f.BLACK)));
        
        Dimensions optionsDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -205);
        widgets.add(new FunctionalButtonWidget(screenHolder, optionsDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::options));
        widgets.add(new SimpleWidget(screenHolder, optionsDims, new TextStyle(Ludo.instance().fontRenderer32, "Einstellungen", Color4f.BLACK)));
        
        Dimensions exitDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -100);
        widgets.add(new FunctionalButtonWidget(screenHolder, exitDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::exit));
        widgets.add(new SimpleWidget(screenHolder, exitDims, new TextStyle(Ludo.instance().fontRenderer32, "Beenden", Color4f.BLACK)));
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
        Color4f.BLUE.glColor4f();
        LudoTextures.figureHuge.render(325, (screenH - 700 - 150) / 2, 700, 700, 0, 0, LudoTextures.figureHuge.getWidth(), LudoTextures.figureHuge.getHeight());
        
        Color4f.YELLOW.glColor4f();
        LudoTextures.figureHuge.render(-25, (screenH - 700 - 150) / 2, 700, 700, 0, 0, LudoTextures.figureHuge.getWidth(), LudoTextures.figureHuge.getHeight());
        
        Color4f.RED.glColor4f();
        LudoTextures.figureHuge.render(450, (screenH - 700 - 150) / 2 + 150, 700, 700, 0, 0, LudoTextures.figureHuge.getWidth(), LudoTextures.figureHuge.getHeight());
        
        Color4f.GREEN.glColor4f();
        LudoTextures.figureHuge.render(100, (screenH - 700 - 150) / 2 + 150, 700, 700, 0, 0, LudoTextures.figureHuge.getWidth(), LudoTextures.figureHuge.getHeight());
        
        Color4f.BLACK.glColor4f();
        LudoTextures.logo.render(900, 50, LudoTextures.logo.getWidth(), LudoTextures.logo.getHeight(), 0, 0, LudoTextures.logo.getWidth(), LudoTextures.logo.getHeight());
        
        super.render(deltaTick, mouseX, mouseY);
    }
}
