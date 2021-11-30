package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.networking.RollMessage;
import sweng_plus.boardgames.ludo.gui.widget.TexturedButtonWidget;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.TextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class DiceScreen extends WrappedScreen
{
    public DiceScreen(Screen subScreen)
    {
        super(subScreen);
        
        Dimensions rollDims = new Dimensions(350, 80, AnchorPoint.M, 0, 0);
        widgets.add(new TexturedButtonWidget(screenHolder, rollDims, this::roll, LudoTextures.activeButton, LudoTextures.inactiveButton));
        widgets.add(new TextWidget(screenHolder, rollDims, Ludo.instance().fontRenderer32, "würfeln"));
    }
    
    private void roll()
    {
        try
        {
            Ludo.instance().getClientManager().sendMessageToServer(new RollMessage());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        returnToSubScreen();
    }
}
