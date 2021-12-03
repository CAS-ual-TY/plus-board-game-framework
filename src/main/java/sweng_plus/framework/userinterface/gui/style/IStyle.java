package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public interface IStyle
{
    void renderWidget(Widget widget, float deltaTick, int mouseX, int mouseY);
}
