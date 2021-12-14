package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;
import sweng_plus.boardgames.ludo.gamelogic.networking.RollMessage;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class DiceScreen extends WrappedScreen implements ILudoScreen
{
    public DiceScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions rollDims = new Dimensions(350, 80, AnchorPoint.M, 0, 0);
        widgets.add(new FunctionalButtonWidget(screenHolder, rollDims, new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::roll));
        widgets.add(new TextWidget(screenHolder, rollDims, Ludo.instance().fontRenderer32, "würfeln"));
    }
    
    private void roll()
    {
        returnToSubScreen();
        
        try
        {
            Ludo.instance().getClientManager().sendMessageToServer(new RollMessage());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void diceResult(int dice)
    {
        returnToSubScreen();
        ((LudoScreen) subScreen).diceResult(dice);
    }
    
    @Override
    public void figureSelected(int figure)
    {
        returnToSubScreen();
        ((LudoScreen) subScreen).figureSelected(figure);
    }
    
    @Override
    public void chat(ChatMessage message)
    {
        ((LudoScreen) subScreen).chat(message);
    }
}
