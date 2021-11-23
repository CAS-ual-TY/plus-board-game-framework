package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.gui.IRenderable;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.input.IInputListener;

public interface IWidget extends IInputListener, IRenderable
{
    /**
     * Called every time the active {@link Screen} ({@link IGame#getScreen()}/{@link IGame#setScreen(Screen)})
     * is changed (with this widget being a part of the new {@link Screen}) or when the window is resized.
     * Primarily this should be used to update widget dimensions (position, maybe also the size) based on these events.
     *
     * @param parent The {@link IWidgetParent} to give access to its screen coordinates and size
     *               (for proper aligning with anchor points etc.).
     */
    void initWidget(IWidgetParent parent);
}
