package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gamelogic.LudoNodeType;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.boardgames.ludo.gui.widget.DiceAnimationWidget;
import sweng_plus.boardgames.ludo.gui.widget.FigureAnimationWidget;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.function.Consumer;

public class FigureAnimationScreen extends LudoExtensionScreen
{
    protected Runnable onEnd;
    protected FigureAnimationWidget animationWidget;
    
    public FigureAnimationScreen(LudoScreen subScreen, Runnable onEnd, int figure, LudoFigure selectedFigure,
                                 LudoNode startNode, LudoNode endNode)
    {
        super(subScreen);
        
        this.onEnd = onEnd;
    
        int timer;
    
        if(startNode.getNodeType() == LudoNodeType.OUTSIDE)
        {
            timer = 20;
        }
        else
        {
            timer = subScreen.logic.latestRoll * 10;
        }
    
        widgets.add(animationWidget = new FigureAnimationWidget(screenHolder, new Dimensions(AnchorPoint.M), selectedFigure,
                LudoTextures.figure, subScreen.nodeWidgetMap.get(startNode), subScreen.nodeWidgetMap.get(endNode),
                timer));
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
    public void renderBackground(float deltaTick)
    {
        subScreen.render(deltaTick, -1, -1);
    }
}
