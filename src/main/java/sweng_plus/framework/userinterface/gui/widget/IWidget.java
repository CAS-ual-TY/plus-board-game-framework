package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.IInputListener;

public interface IWidget extends IInputListener
{
    void render(float deltaTick, int mouseX, int mouseY);
    
    void update(int mouseX, int mouseY);
    
    void init(int screenW, int screenH);
}
