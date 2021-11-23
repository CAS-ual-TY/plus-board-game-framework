package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.Window;
import sweng_plus.framework.userinterface.input.IInputListener;

public interface IWidget extends IInputListener
{
    IWidgetParent getParent();
    
    void render(float deltaTick, int mouseX, int mouseY);
    
    void update(int mouseX, int mouseY);
    
    void init();
}
