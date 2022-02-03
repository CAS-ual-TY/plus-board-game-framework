package sweng_plus.framework.boardgame;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.input.IInputListener;

import java.util.Collections;
import java.util.List;

public interface IGame extends IScreenHolder
{
    /**
     * Auxiliary setup method. Called before anything is initialized and before the window is created.
     * This is the first thing that is ever called.
     */
    default void preInit() {}
    
    /**
     * Main setup method. Called after GLFW is initialized, the window is created
     * and after the input handler and OpenGL are initialized.
     */
    void init();
    
    /**
     * Main cleanup method. Called before the input handler is cleaned up, before the window is destroyed
     * and before GLFW is cleaned up.
     */
    void cleanup();
    
    /**
     * Auxiliary cleanup method. Called after everything is cleaned up and after the window is destroyed.
     * This is the last thing that is ever called.
     */
    default void postCleanup() {}
    
    /**
     * @return The title of the window shown in the operating system.
     */
    String getWindowTitle();
    
    /**
     * @return The path to the icon of the window.
     */
    String getWindowIconResource();
    
    // Same doc as IRenderable#update
    
    /**
     * Called every game tick with stable interval. The amount of ticks is set by overriding {@link #getTicksPerSecond()}.
     */
    void update();
    
    // Same doc as IRenderable#render
    
    /**
     * Called every frame. Can be called multiple times in between game ticks.
     *
     * @param deltaTick The amount of time (in game-tick-%) until the next game tick is approximately executed.
     *                  E.g. if this value os 0.5F, then the current frame is exactly halfway between the last
     *                  {@link #update()} call and the next call.
     */
    void render(float deltaTick);
    
    /**
     * This method allows any additional {@link IInputListener} to listen to input.
     * All methods of {@link IInputListener} are called right before {@link #update()} is called.
     *
     * @return A list with all additional {@link IInputListener}s, which listen to current input.
     * Shall not include the screen returned by {@link #getScreen()}, this is done separately.
     */
    default List<IInputListener> getInputListeners()
    {
        return Collections.emptyList();
    }
    
    /**
     * @return The amount of times {@link #update()} is called per second (game tick interval).
     */
    @SuppressWarnings("SameReturnValue")
    default int getTicksPerSecond()
    {
        return 20;
    }
}
