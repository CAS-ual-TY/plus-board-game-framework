package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.input.IInputListener;
import sweng_plus.framework.userinterface.input.INestedInputListener;

import java.util.List;

public interface IWidgetParent extends IWidget, INestedInputListener
{
    int getParentX();
    
    int getParentY();
    
    int getParentW();
    
    int getParentH();
    
    IScreenHolder getScreenHolder();
    
    List<IWidget> getWidgets();
    
    @Override
    default void update(int mouseX, int mouseY)
    {
        for(IWidget w : getWidgets())
        {
            w.update(mouseX, mouseY);
        }
    }
    
    @Override
    default void render(float deltaTick, int mouseX, int mouseY)
    {
        for(IWidget w : getWidgets())
        {
            w.render(deltaTick, mouseX, mouseY);
        }
    }
    
    @Override
    default void init()
    {
        for(IWidget w : getWidgets())
        {
            w.init();
        }
    }
    
    @Override
    default List<? extends IInputListener> getListeners()
    {
        return getWidgets();
    }
}
