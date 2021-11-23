package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.userinterface.gui.widget.base.INestedWidget;
import sweng_plus.framework.userinterface.gui.widget.base.IWidget;
import sweng_plus.framework.userinterface.input.IInputListener;
import sweng_plus.framework.userinterface.input.INestedInputListener;

import java.util.List;

public interface IWidgetParent
{
    /**
     * @return All sub-{@link IInputListener}s of this {@link INestedInputListener}.
     */
    List<IWidget> getWidgets();
    
    /**
     * Used by sub-widgets returned by {@link #getWidgets()} to calculate their {@link Screen} position.
     *
     * @return X-coordinate of this {@link INestedWidget}'s {@link Screen} position.
     */
    int getParentX();
    
    /**
     * Used by sub-widgets returned by {@link #getWidgets()} to calculate their {@link Screen} position.
     *
     * @return Y-coordinate of this {@link INestedWidget}'s {@link Screen} position.
     */
    int getParentY();
    
    /**
     * Used by sub-widgets returned by {@link #getWidgets()} to calculate their {@link Screen} position.
     *
     * @return Width of this {@link INestedWidget} in {@link Screen}-coordinates.
     */
    int getParentW();
    
    /**
     * Used by sub-widgets returned by {@link #getWidgets()} to calculate their {@link Screen} position.
     *
     * @return Height of this {@link INestedWidget} in {@link Screen}-coordinates.
     */
    int getParentH();
    
    /**
     * Calls {@link IWidget#initWidget(IWidgetParent)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    default void initSubWidgets(IWidgetParent parent)
    {
        for(IWidget w : getWidgets())
        {
            w.initWidget(this);
        }
    }
}
