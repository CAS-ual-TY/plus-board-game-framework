package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.userinterface.IInputListener;

public class Screen implements IInputListener
{
    public static final Screen EMPTY_SCREEN = new Screen();
    
    public int screenW;
    public int screenH;
    
    public void update(int mouseX, int mouseY)
    {
    
    }
    
    public void render(float deltaTick, int mouseX, int mouseY)
    {
    
    }
    
    /** Wird immer gecallt, wenn das Window resized wird, oder der Screen zum ersten mal wieder aktiv ist */
    public void init(int screenW, int screenH)
    {
    
    }
    
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
    
    }
    
    public void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
    
    }
    
    public void keyPressed(int key, int mods)
    {
    
    }
    
    public void keyReleased(int key, int mods)
    {
    
    }
    
    public void charTyped(char character)
    {
    
    }
}
