package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.input.IInputListener;
import sweng_plus.framework.userinterface.input.INestedInputListener;

import java.util.List;

public interface IWidgetParent extends IWidget, INestedInputListener
{
    /**
     * Used by sub-widgets returned by {@link #getWidgets()} to calculate their {@link Screen} position.
     *
     * @return X-coordinate of this {@link IWidgetParent}'s {@link Screen} position.
     */
    int getParentX();
    
    /**
     * Used by sub-widgets returned by {@link #getWidgets()} to calculate their {@link Screen} position.
     *
     * @return Y-coordinate of this {@link IWidgetParent}'s {@link Screen} position.
     */
    int getParentY();
    
    /**
     * Used by sub-widgets returned by {@link #getWidgets()} to calculate their {@link Screen} position.
     *
     * @return Width of this {@link IWidgetParent} in {@link Screen}-coordinates.
     */
    int getParentW();
    
    /**
     * Used by sub-widgets returned by {@link #getWidgets()} to calculate their {@link Screen} position.
     *
     * @return Height of this {@link IWidgetParent} in {@link Screen}-coordinates.
     */
    int getParentH();
    
    /**
     * @return All sub-{@link IInputListener}s of this {@link INestedInputListener}.
     */
    List<IWidget> getWidgets();
    
    // Same doc as IWidget#update
    
    /**
     * Calls {@link IWidget#updateWidget(int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void updateWidget(int mouseX, int mouseY)
    {
        for(IWidget w : getWidgets())
        {
            w.updateWidget(mouseX, mouseY);
        }
    }
    
    // Same doc as IWidget#render
    
    /**
     * Calls {@link IWidget#renderWidget(float, int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void renderWidget(float deltaTick, int mouseX, int mouseY)
    {
        for(IWidget w : getWidgets())
        {
            w.renderWidget(deltaTick, mouseX, mouseY);
        }
    }
    
    /**
     * <p>Calls {@link IWidget#init(IWidgetParent)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.</p>
     *
     * <p>This method should be overridden and update this {@link IWidgetParent}'s position and size and only then
     * call {@link IWidget#init(IWidgetParent)} of all sub-widgets returned by {@link #getWidgets()}</p>
     */
    @Override
    default void init(IWidgetParent parent)
    {
        for(IWidget w : getWidgets())
        {
            w.init(this);
        }
    }
    
    @Override
    default List<? extends IInputListener> getListeners()
    {
        return getWidgets();
    }
    
    /**
     * Calls {@link Widget#mouseButtonPressed(int, int, int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        INestedInputListener.super.mouseButtonPressed(mouseX, mouseY, mouseButton, mods);
    }
    
    /**
     * Calls {@link Widget#mouseButtonReleased(int, int, int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
        INestedInputListener.super.mouseButtonReleased(mouseX, mouseY, mouseButton, mods);
    }
    
    /**
     * Calls {@link Widget#keyPressed(int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void keyPressed(int key, int mods)
    {
        INestedInputListener.super.keyPressed(key, mods);
    }
    
    /**
     * Calls {@link Widget#keyRepeated(int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void keyRepeated(int key, int mods)
    {
        INestedInputListener.super.keyRepeated(key, mods);
    }
    
    /**
     * Calls {@link Widget#keyReleased(int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void keyReleased(int key, int mods)
    {
        INestedInputListener.super.keyReleased(key, mods);
    }
    
    /**
     * Calls {@link Widget#charTyped(char)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void charTyped(char character)
    {
        INestedInputListener.super.charTyped(character);
    }
}
