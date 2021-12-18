package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class EmptyStyle extends BaseStyle
{
    public static final EmptyStyle EMPTY_STYLE = new EmptyStyle();
    
    private EmptyStyle() {}
    
    @Override
    public void renderWidget(Widget widget, float deltaTick, int mouseX, int mouseY)
    {
    
    }
}
