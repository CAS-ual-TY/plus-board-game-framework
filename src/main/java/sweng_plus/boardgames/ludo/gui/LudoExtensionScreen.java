package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.LinkedList;
import java.util.List;

public class LudoExtensionScreen extends WrappedScreen implements ILudoScreen
{
    public List<Widget> universalWidgets;
    
    public LudoExtensionScreen(LudoScreen subScreen)
    {
        super(subScreen);
        universalWidgets = new LinkedList<>();
        subScreen.removeUniversalWidgets(universalWidgets::add);
        widgets.addAll(universalWidgets);
    }
    
    @Override
    public void initScreen(int screenW, int screenH)
    {
        // move widgets back to LudoScreen for init
        // then get them back
        universalWidgets.forEach(widgets::remove);
        ((LudoScreen) subScreen).reAddUniversalWidgets();
        
        super.initScreen(screenW, screenH);
        
        universalWidgets.clear();
        ((LudoScreen) subScreen).removeUniversalWidgets(universalWidgets::add);
        widgets.addAll(universalWidgets);
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
    
    @Override
    public void returnToSubScreen()
    {
        ((LudoScreen) subScreen).reAddUniversalWidgets();
        super.returnToSubScreen();
    }
}
