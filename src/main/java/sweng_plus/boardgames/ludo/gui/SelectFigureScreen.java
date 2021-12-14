package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.networking.FigureSelectMessage;
import sweng_plus.boardgames.ludo.gui.widget.SelectableFigureWidget;
import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.io.IOException;

public class SelectFigureScreen extends LudoExtensionScreen
{
    public SelectFigureScreen(LudoScreen subScreen)
    {
        super(subScreen);
        
        subScreen.logic.movableFigures.keySet().stream().map(NodeFigure::getCurrentNode)
                .map(subScreen.nodeWidgetMap::get)
                .forEach((widget) -> widgets.add(
                        new SelectableFigureWidget(subScreen.screenHolder, this::select, widget,
                                subScreen.isTurnPlayer())));
        widgets.sort(Widget.TOP_TO_BOTTOM_SORTER);
    }
    
    private void select(ButtonWidget button)
    {
        if(screenHolder.getScreen() == this)
        {
            returnToSubScreen();
            
            if(((LudoScreen) subScreen).isTurnPlayer())
            {
                try
                {
                    Ludo.instance().getClientManager().sendMessageToServer(
                            new FigureSelectMessage(((SelectableFigureWidget) button).ludoFigure.getIndex()));
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
