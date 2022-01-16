package sweng_plus.boardgames.mill.gui;

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
        widgets.addAll(universalWidgets);
    }
    
    @Override
    public void initScreen(int screenW, int screenH)
    {
        // move widgets back to MillScreen for init
        // then get them back
        universalWidgets.forEach(widgets::remove);
        
        super.initScreen(screenW, screenH);
        
        universalWidgets.clear();
        widgets.addAll(universalWidgets);
    }
    
    @Override
    public void figureNodeSelected(int figure, int node)
    {
        returnToSubScreen();
        ((MillScreen) subScreen).figureNodeSelected(figure, node);
    }
    
    @Override
    public void figureTaken(int figure)
    {
        returnToSubScreen();
        ((MillScreen) subScreen).figureTaken(figure);
    }
    
    @Override
    public void gameWon(String winnerName)
    {
        returnToSubScreen();
        ((MillScreen) subScreen).gameWon(winnerName);
    }
    
    @Override
    public void returnToSubScreen()
    {
        super.returnToSubScreen();
    }
    
    @Override
    public void renderBackground(float deltaTick)
    {
        subScreen.render(deltaTick, -1, -1);
    }
}
