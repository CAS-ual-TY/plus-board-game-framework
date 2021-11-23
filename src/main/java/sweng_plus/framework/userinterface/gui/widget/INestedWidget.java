package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.input.IInputListener;
import sweng_plus.framework.userinterface.input.INestedInputListener;

import java.util.List;

public interface INestedWidget extends IWidget, INestedInputListener
{
    /**
     * @return All sub-{@link IInputListener}s of this {@link INestedInputListener}.
     */
    List<IWidget> getWidgets();
    
    // Same doc as IWidget#initWidget
    
    /**
     * <p>Called every time the active {@link Screen} ({@link IGame#getScreen()}/{@link IGame#setScreen(Screen)})
     * is changed (with this widget being a part of the new {@link Screen}) or when the window is resized.
     * Primarily this should be used to update widget dimensions (position, maybe also the size) based on these events.</p>
     *
     * <p>This method is called by {@link #initWidget(INestedWidget)} which then
     * calls {@link IWidget#initWidget(INestedWidget)} for all sub-widgets returned by {@link #getWidgets()}</p>
     *
     * @param parent The {@link INestedWidget} to give access to its screen coordinates and size
     *               (for proper aligning with anchor points etc.).
     */
    void initWidgetParent(INestedWidget parent);
    
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
    
    // Same doc as IWidget#update
    
    /**
     * Calls {@link IWidget#update(int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void update(int mouseX, int mouseY)
    {
        for(IWidget w : getWidgets())
        {
            w.update(mouseX, mouseY);
        }
    }
    
    // Same doc as IWidget#render
    
    /**
     * Calls {@link IWidget#render(float, int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void render(float deltaTick, int mouseX, int mouseY)
    {
        for(IWidget w : getWidgets())
        {
            w.render(deltaTick, mouseX, mouseY);
        }
    }
    
    /**
     * First calls {@link #initWidgetParent(INestedWidget)} to initialize this {@link INestedWidget}.
     * <p>
     * Then calls {@link IWidget#initWidget(INestedWidget)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void initWidget(INestedWidget parent)
    {
        initWidgetParent(parent);
        
        for(IWidget w : getWidgets())
        {
            w.initWidget(this);
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
