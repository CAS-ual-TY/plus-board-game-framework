package sweng_plus.boardgames.mill.gui;

import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;

public class SelectTakeableFigureScreen extends MillExtensionScreen
{
    public SelectTakeableFigureScreen(MillScreen subScreen)
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
