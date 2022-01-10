package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.gamelogic.MillNode;
import sweng_plus.framework.userinterface.gui.style.BaseStyle;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class MillNodeStyle extends BaseStyle
{
    protected MillNode node;
    
    public MillNodeStyle(MillNode node)
    {
        this.node = node;
    }
    
    @Override
    public void renderWidget(Widget widget, float deltaTick, int mouseX, int mouseY)
    {
    
    }
}
