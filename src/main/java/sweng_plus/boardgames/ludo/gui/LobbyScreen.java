package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoStyles;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.style.TextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class LobbyScreen extends Screen
{
    public LobbyScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions textDims = new Dimensions(800, 0, AnchorPoint.M, 0, 0);
        widgets.add(new SimpleWidget(screenHolder, textDims, new TextStyle(Ludo.instance().fontRenderer64, Ludo.instance().names, Color4f.BLACK)));
        
        Dimensions cancelDims = new Dimensions(350, 80, AnchorPoint.L, 100, 400);
        widgets.add(new FunctionalButtonWidget(screenHolder, cancelDims, LudoStyles.makeButtonStyle("menu.lobby.back"), this::cancel));
        
        if(Ludo.instance().getNetworking().isHost())
        {
            Dimensions startDims = new Dimensions(350, 80, AnchorPoint.R, -100, 400);
            widgets.add(new FunctionalButtonWidget(screenHolder, startDims, LudoStyles.makeButtonStyle("ludo.start_game"), this::start));
        }
    }
    
    private void cancel()
    {
        screenHolder.setScreen(new MenuScreen(screenHolder));
    }
    
    private void start()
    {
        if(Ludo.instance().getNetworking().getHostManager().getAllClients().size() >= 2)
        {
            Ludo.instance().startGame(true, Ludo.instance().getNetworking().getHostManager().getAllClients().size());
        }
    }
}
