package sweng_plus.framework.userinterface;

public interface IInputListener
{
    void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods);
    
    void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods);
    
    void keyPressed(int key, int mods);
    
    void keyReleased(int key, int mods);
    
    void keyRepeated(int key, int mods);
    
    void charTyped(char character);
}
