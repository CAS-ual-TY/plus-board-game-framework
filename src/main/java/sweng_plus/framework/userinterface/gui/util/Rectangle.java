package sweng_plus.framework.userinterface.gui.util;

public class Rectangle implements Cloneable
{
    public int x;
    public int y;
    
    public int w;
    public int h;
    
    public Rectangle(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        
        this.w = w;
        this.h = h;
    }
    
    public boolean isMouseOver(int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + w
                && mouseY >= y && mouseY < y + h;
    }
    
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Rectangle clone()
    {
        return new Rectangle(x, y, w, h);
    }
}
