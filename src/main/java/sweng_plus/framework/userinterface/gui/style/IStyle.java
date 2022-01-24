package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public interface IStyle
{
    void initStyle(Dimensions parentDimensions);
    
    void renderStyle(float deltaTick, int mouseX, int mouseY);
}
