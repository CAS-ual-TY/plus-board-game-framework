package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.boardgames.ludo.gui.widget.DiceAnimationWidget;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class DiceAnimationScreen extends WrappedScreen implements ILudoScreen
{
    protected Runnable onEnd;
    protected DiceAnimationWidget animationWidget;
    
    public DiceAnimationScreen(LudoScreen subScreen, Runnable onEnd, int dice)
    {
        super(subScreen);
        
        this.onEnd = onEnd;
        
        widgets.add(animationWidget = new DiceAnimationWidget(screenHolder, new Dimensions(AnchorPoint.M),
                LudoTextures.diceAnim[dice - 1], 16 * 5 /*LudoTextures.diceAnimLast[dice-1]*/, LudoTextures.diceAnimPositions[dice - 1]));
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX, mouseY);
        
        if(animationWidget.hasEnded())
        {
            onEnd();
        }
    }
    
    /*
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        if(mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            onEnd();
        }
    }
    */
    
    public void onEnd()
    {
        returnToSubScreen();
        onEnd.run();
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
