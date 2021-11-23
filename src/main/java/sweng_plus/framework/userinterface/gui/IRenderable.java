package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.Window;

public interface IRenderable
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
}
