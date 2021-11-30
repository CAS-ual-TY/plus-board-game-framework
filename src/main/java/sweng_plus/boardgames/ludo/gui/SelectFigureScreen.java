package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.networking.FigureSelectMessage;
import sweng_plus.boardgames.ludo.gui.widget.SelectableFigureWidget;
import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;
import java.util.List;

public class SelectFigureScreen extends WrappedScreen
{
    public SelectFigureScreen(LudoScreen subScreen, List<NodeFigure> list)
    {
        super(subScreen);
        
        subScreen.logic.movableFigures.keySet().stream().map((figure) -> figure.getCurrentNode()).map((node) -> subScreen.nodeWidgetMap.get(node))
                .forEach((widget) -> widgets.add(new SelectableFigureWidget(subScreen.screenHolder, new Dimensions(AnchorPoint.M), this::select, widget)));
    }
    
    private void select(ButtonWidget button)
    {
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
