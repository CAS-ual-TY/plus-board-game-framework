package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gui.widget.DiceAnimationWidget;
import sweng_plus.framework.userinterface.gui.WrappedScreen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class DiceAnimationScreen extends WrappedScreen implements ILudoScreen
{
    protected Runnable onEnd;
    protected DiceAnimationWidget dice;
    
    public DiceAnimationScreen(LudoScreen subScreen, Runnable onEnd)
    {
        super(subScreen);
        
        this.onEnd = onEnd;
        
        widgets.add(dice = new DiceAnimationWidget(screenHolder, new Dimensions(AnchorPoint.M),
                LudoTextures.diceAnim1, 16 * 5 /*LudoTextures.diceAnim1Last*/, LudoTextures.diceAnim1Positions));
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX, mouseY);
        
        if(dice.hasEnded())
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
}
