package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.IInputListener;
import sweng_plus.framework.userinterface.INestedInputListener;
import sweng_plus.framework.userinterface.gui.IScreenHolder;

import java.util.List;

public interface IWidgetParent extends INestedInputListener
{
    int getScreenX();
    
    int getScreenY();
    
    int getScreenW();
    
    int getScreenH();
    
    IScreenHolder getScreenHolder();
    
    List<IWidget> getWidgets();
    
    default void render(float deltaTick, int mouseX, int mouseY)
    {
        for(IWidget w : getWidgets())
        {
            w.render(deltaTick, mouseX, mouseY);
        }
    }
    
    default void update(int mouseX, int mouseY)
    {
        for(IWidget w : getWidgets())
        {
            w.update(mouseX, mouseY);
        }
    }
    
    default void init()
    {
        for(IWidget w : getWidgets())
        {
            w.init(getScreenW(), getScreenH());
        }
    }
    
    @Override
    default List<? extends IInputListener> getListeners()
    {
        return getWidgets();
    }
}
