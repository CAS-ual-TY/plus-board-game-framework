package sweng_plus.framework.userinterface;

public interface IInputListener
{
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods);
    
    public void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods);
    
    public void keyPressed(int key, int mods);
    
    public void keyReleased(int key, int mods);
    
    public void charTyped(char character);
}
