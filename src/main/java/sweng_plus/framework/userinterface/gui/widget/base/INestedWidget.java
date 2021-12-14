package sweng_plus.framework.userinterface.gui.widget.base;

import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.gui.INestedRenderable;
import sweng_plus.framework.userinterface.gui.IRenderable;
import sweng_plus.framework.userinterface.gui.IWidgetParent;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.input.IInputListener;
import sweng_plus.framework.userinterface.input.INestedInputListener;

import java.util.List;

public interface INestedWidget extends IWidget, IWidgetParent, INestedRenderable, INestedInputListener
{
    // Same doc as IWidget#initWidget
    
    /**
     * <p>Called every time the active {@link Screen} ({@link IGame#getScreen()}/{@link IGame#setScreen(Screen)})
     * is changed (with this widget being a part of the new {@link Screen}) or when the window is resized.
     * Primarily this should be used to update widget dimensions (position, maybe also the size) based on these events.</p>
     *
     * <p>This method is called by {@link #initWidget(IWidgetParent)} which then
     * calls {@link IWidget#initWidget(IWidgetParent)} for all sub-widgets returned by {@link #getWidgets()}</p>
     *
     * @param parent The {@link INestedWidget} to give access to its screen coordinates and size
     *               (for proper aligning with anchor points etc.).
     */
    void initNestedWidget(IWidgetParent parent);
    
    /**
     * First calls {@link #initNestedWidget(IWidgetParent)} to initialize this {@link INestedWidget}.
     * Then calls {@link IWidget#initWidget(IWidgetParent)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void initWidget(IWidgetParent parent)
    {
        initNestedWidget(parent);
        initSubWidgets(parent);
    }
    
    /**
     * Calls {@link IWidget#update(int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void update(int mouseX, int mouseY)
    {
        INestedRenderable.super.update(mouseX, mouseY);
    }
    
    /**
     * Calls {@link IWidget#render(float, int, int)} for all {@link IWidget}s
     * returned by {@link #getWidgets()}.
     */
    @Override
    default void render(float deltaTick, int mouseX, int mouseY)
    {
        INestedRenderable.super.render(deltaTick, mouseX, mouseY);
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
    
    @Override
    default List<? extends IInputListener> getInputListeners()
    {
        return getWidgets();
    }
    
    @Override
    default List<? extends IRenderable> getRenderables()
    {
        return getWidgets();
    }
}
