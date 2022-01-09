package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.gui.IMillScreen;
import sweng_plus.boardgames.mill.gui.MillScreen;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.LinkedList;
import java.util.List;

public class MillExtensionScreen extends WrappedScreen implements IMillScreen
{
    public List<Widget> universalWidgets;
    
    public MillExtensionScreen(MillScreen subScreen)
    {
        super(subScreen);
        universalWidgets = new LinkedList<>();
        subScreen.removeUniversalWidgets(universalWidgets::add);
        widgets.addAll(universalWidgets);
    }
    
    @Override
    public void initScreen(int screenW, int screenH)
    {
        // move widgets back to MillScreen for init
        // then get them back
        universalWidgets.forEach(widgets::remove);
        ((MillScreen) subScreen).reAddUniversalWidgets();
        
        super.initScreen(screenW, screenH);
        
        universalWidgets.clear();
        ((MillScreen) subScreen).removeUniversalWidgets(universalWidgets::add);
        widgets.addAll(universalWidgets);
    }

    @Override
    public void figureNodeSelected(int figure, int node)
    {
        returnToSubScreen();
        ((MillScreen) subScreen).figureNodeSelected(figure, node);
    }
    
    @Override
    public void gameWon(int winningTeamIndex)
    {
        returnToSubScreen();
        ((MillScreen) subScreen).gameWon(winningTeamIndex);
    }
    
    /*
    @Override
    public void chat(ChatMessage message)
    {
        ((MillScreen) subScreen).chat(message);
    }
    */
    
    @Override
    public void returnToSubScreen()
    {
        ((MillScreen) subScreen).reAddUniversalWidgets();
        super.returnToSubScreen();
    }
}
