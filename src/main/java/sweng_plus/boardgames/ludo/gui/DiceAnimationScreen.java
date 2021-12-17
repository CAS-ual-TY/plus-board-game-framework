package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.boardgames.ludo.gui.widget.DiceAnimationWidget;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class DiceAnimationScreen extends LudoExtensionScreen
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
    }
    
    @Override
    public void returnToSubScreen()
    {
        super.returnToSubScreen();
        onEnd.run();
    }
}
