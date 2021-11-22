package sweng_plus.framework.userinterface.input;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.Window;

public interface IInputListener
{
    /**
     * Called whenever a mouse button is pressed that was not pressed before.
     * This method can not be called again before {@link #mouseButtonReleased(int, int, int, int)} has not been called.
     *
     * @param mouseX      The X screen coordinate of the mouse position. It holds that:
     *                    0 <= mouseX < {@link Window#getScreenW()}
     * @param mouseY      The Y screen coordinate of the mouse position. It holds that:
     *                    0 <= mouseY < {@link Window#getScreenH()}
     * @param mouseButton The mouse button that was pressed. Compare to GLFW mouse button values.
     *                    See {@link GLFW#GLFW_MOUSE_BUTTON_LEFT} and similar fields.
     * @param mods        The active mod-flags when the mouse button was pressed.
     *                    Use bitwise operators with GLFW mod flags.
     *                    See {@link GLFW#GLFW_MOD_CONTROL} and similar fields.
     */
    void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods);
    
    /**
     * Called whenever a mouse button is released that was pressed before.
     * This method can not be called again before {@link #mouseButtonPressed(int, int, int, int)} has not been called.
     *
     * @param mouseX      The X screen coordinate of the mouse position. It holds that:
     *                    0 <= mouseX < {@link Window#getScreenW()}
     * @param mouseY      The Y screen coordinate of the mouse position. It holds that:
     *                    0 <= mouseY < {@link Window#getScreenH()}
     * @param mouseButton The mouse button that was released. Compare to GLFW mouse button values.
     *                    See {@link GLFW#GLFW_MOUSE_BUTTON_LEFT} and similar fields.
     * @param mods        The active mod-flags when this mouse button was released.
     *                    Use bitwise operators with GLFW mod flags.
     *                    See {@link GLFW#GLFW_MOD_CONTROL} and similar fields.
     */
    void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods);
    
    /**
     * Called whenever a key is pressed that was not pressed before.
     * This method can not be called again before {@link #keyReleased(int, int)} has not been called.
     *
     * @param key  The key that was pressed. Compare to GLFW key values.
     *             See {@link GLFW#GLFW_KEY_W} and similar fields.
     * @param mods The active mod-flags when this key was pressed. Use bitwise operators with GLFW mod flags.
     *             See {@link GLFW#GLFW_MOD_CONTROL} and similar fields.
     */
    void keyPressed(int key, int mods);
    
    /**
     * Called whenever a key is pressed and held down.
     * This method is called repeatedly after {@link #keyPressed(int, int)} is called and a short delay has passed
     * and until {@link #keyReleased(int, int)} is called.
     *
     * @param key  The key that is being held down. Compare to GLFW key values.
     *             See {@link GLFW#GLFW_KEY_W} and similar fields.
     * @param mods The active mod-flags while this key is being held down. Use bitwise operators with GLFW mod flags.
     *             See {@link GLFW#GLFW_MOD_CONTROL} and similar fields.
     */
    void keyRepeated(int key, int mods);
    
    /**
     * Called whenever a key is released that was pressed before.
     * This method can not be called again before {@link #keyPressed(int, int)} has not been called.
     *
     * @param key  The key that was released. Compare to GLFW key values.
     *             See {@link GLFW#GLFW_KEY_W} and similar fields.
     * @param mods The active mod-flags when this key was released. Use bitwise operators with GLFW mod flags.
     *             See {@link GLFW#GLFW_MOD_CONTROL} and similar fields.
     */
    void keyReleased(int key, int mods);
    
    /**
     * <p>Called whenever a valid character is typed by pressing or holding a key or a combination of keys.
     * Adjusts for different keyboards and language settings. {@link #keyPressed(int, int)} is also called,
     * this method is specifically made to easily get characters from pressed key combinations.</p>
     *
     * <p>E.g. pressing Left-Shift followed by L will result in the character 'L'.
     * Left-Shift followed by 1 will result in '!' (german keyboard). And so on...</p>
     *
     * <p>Backspace or DEL is not included.</p>
     *
     * <p>In case a button (or combination) is pressed and held, this method will be called once immediately
     * and then after a short delay it will be called repeatedly.</p>
     *
     * @param character The character the currently pressed keys represent.
     */
    void charTyped(char character);
}
