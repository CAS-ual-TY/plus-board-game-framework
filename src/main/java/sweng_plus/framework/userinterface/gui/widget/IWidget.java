package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.Window;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.input.IInputListener;

public interface IWidget extends IInputListener
{
    // Same doc as IGame#update, mouseX/Y: IInputListener#mousePressed
    
    /**
     * Called every game tick with stable interval. The amount of ticks is set by {@link IGame#getTicksPerSecond()}.
     *
     * @param mouseX The X screen coordinate of the mouse position. It holds that:
     *               0 <= mouseX < {@link Window#getScreenW()}
     * @param mouseY The Y screen coordinate of the mouse position. It holds that:
     *               0 <= mouseY < {@link Window#getScreenH()}
     */
    void update(int mouseX, int mouseY);
    
    // Same doc as IGame#render, mouseX/Y: IInputListener#mousePressed
    
    /**
     * Called every frame. Can be called multiple times in between game ticks.
     *
     * @param mouseX The X screen coordinate of the mouse position. It holds that:
     *               0 <= mouseX < {@link Window#getScreenW()}
     * @param mouseY The Y screen coordinate of the mouse position. It holds that:
     *               0 <= mouseY < {@link Window#getScreenH()}
     */
    void render(float deltaTick, int mouseX, int mouseY);
    
    /**
     * Called every time the active {@link Screen} ({@link IGame#getScreen()}/{@link IGame#setScreen(Screen)})
     * is changed (with this widget being a part of the new {@link Screen}) or when the window is resized.
     * Primarily this should be used to update widget dimensions (position, maybe also the size) based on these events.
     *
     * @param parent The {@link IWidgetParent} to give access to its screen coordinates and size
     *               (for proper aligning with anchor points etc.).
     */
    void init(IWidgetParent parent);
}
