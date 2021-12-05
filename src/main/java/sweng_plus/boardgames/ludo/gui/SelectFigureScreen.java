package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.networking.FigureSelectMessage;
import sweng_plus.boardgames.ludo.gui.widget.DiceAnimationWidget;
import sweng_plus.boardgames.ludo.gui.widget.SelectableFigureWidget;
import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;

public class SelectFigureScreen extends WrappedScreen
{
    public SelectFigureScreen(LudoScreen subScreen)
    {
        super(subScreen);
        
        DiceAnimationWidget dice = new DiceAnimationWidget(screenHolder, new Dimensions(AnchorPoint.M), LudoTextures.diceAnim1, LudoTextures.diceAnim1Last, LudoTextures.diceAnim1Positions, (w) -> {
            widgets.remove(w);
            subScreen.logic.movableFigures.keySet().stream().map(NodeFigure::getCurrentNode).map(subScreen.nodeWidgetMap::get)
                    .forEach((widget) -> widgets.add(new SelectableFigureWidget(subScreen.screenHolder, this::select, widget)));
        });
        widgets.add(dice);
    }
    
    private void select(ButtonWidget button)
    {
        if(screenHolder.getScreen() == this)
        {
            returnToSubScreen();
            
            try
            {
                Ludo.instance().getClientManager().sendMessageToServer(new FigureSelectMessage(((SelectableFigureWidget) button).ludoFigure.getIndex()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
