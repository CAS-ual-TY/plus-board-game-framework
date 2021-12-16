package sweng_plus.boardgames.ludo.gui.widget;

import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.style.EmptyStyle;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;

public class SelectableFigureWidget extends FunctionalButtonWidget
{
    public LudoNodeWidget nodeWidget;
    public LudoFigure ludoFigure;
    public boolean active;
    
    public SelectableFigureWidget(IScreenHolder screenHolder, SimpleButtonAction buttonAction,
                                  LudoNodeWidget nodeWidget, boolean active)
    {
        super(screenHolder, nodeWidget.getDimensions(), EmptyStyle.EMPTY_STYLE, buttonAction);
        
        this.nodeWidget = nodeWidget;
        ludoFigure = ((LudoFigure) nodeWidget.getNode().getFigures().get(0));
        this.active = active;
    }
    
    @Override
    public boolean updateMouseOver(float deltaTick, int mouseX, int mouseY)
    {
        int x = dimensions.x + dimensions.w / 2;
        int y = dimensions.y + dimensions.h / 2;
        return (x - mouseX) * (x - mouseX) + (y - mouseY) * (y - mouseY) <= LudoNodeWidget.NODE_TEXTURE_RADIUS_SQUARED;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        nodeWidget.render(deltaTick, mouseX, mouseY);
        
        if(active && updateMouseOver(deltaTick, mouseX, mouseY))
        {
            Color4f.HALF_VISIBLE.glColor4f();
            nodeWidget.renderFigure();
        }
    }
    
    @Override
    protected void clicked(int mouseX, int mouseY, int mods)
    {
        if(active)
        {
            super.clicked(mouseX, mouseY, mods);
        }
    }
}
