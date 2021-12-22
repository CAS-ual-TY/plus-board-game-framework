package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gui.MillExtensionScreen;
import sweng_plus.boardgames.mill.gui.MillScreen;
import sweng_plus.boardgames.mill.gui.util.MillTextures;
import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.userinterface.gui.style.TextureStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class SelectFigureScreen extends MillExtensionScreen
{
    public SelectFigureScreen(MillScreen subScreen)
    {
        super(subScreen);
        /*
        subScreen.logic.movableFigures.keySet().stream().map(NodeFigure::getCurrentNode)
                .map(subScreen.nodeWidgetMap::get)
                .forEach((widget) -> widgets.add(
                        new SelectableFigureWidget(subScreen.screenHolder, this::select, widget,
                                subScreen.isTurnPlayer())));
        widgets.sort(Widget.TOP_TO_BOTTOM_SORTER);
        widgets.add(new SimpleWidget(subScreen.screenHolder, new Dimensions(64, 64, AnchorPoint.M),
                new TextureStyle(MillTextures.dices[subScreen.logic.latestRoll - 1])));
         */
    }
    
    private void select(ButtonWidget button)
    {
        /*
        if(screenHolder.getScreen() == this)
        {
            returnToSubScreen();
            
            if(((MillScreen) subScreen).isTurnPlayer())
            {
                Mill.instance().getNetworking().getClientManager().sendMessageToServer(
                        new FigureSelectMessage(((SelectableFigureWidget) button).millFigure.getIndex()));
            }
        }
         */
    }
}
