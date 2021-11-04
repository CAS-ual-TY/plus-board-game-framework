package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.AnchorPoint;

public class Dimensions
{
    public int x;
    public int y;
    
    public int w;
    public int h;
    
    public int offX;
    public int offY;
    
    public AnchorPoint headAnchor;
    public AnchorPoint innerAnchor;
    
    public Dimensions(int w, int h, AnchorPoint headAnchor, AnchorPoint innerAnchor, int offX, int offY)
    {
        this.w = w;
        this.h = h;
        
        this.offX = offX;
        this.offY = offY;
        
        this.headAnchor = headAnchor;
        this.innerAnchor = innerAnchor;
    }
    
    public Dimensions(int w, int h, AnchorPoint headAnchor, AnchorPoint innerAnchor)
    {
        this(w, h, headAnchor, innerAnchor, 0, 0);
    }
    
    public Dimensions(int w, int h, AnchorPoint anchor, int offX, int offY)
    {
        this(w, h, anchor, anchor, offX, offY);
    }
    
    public Dimensions(int w, int h, AnchorPoint anchor)
    {
        this(w, h, anchor, 0, 0);
    }
    
    public Dimensions(int x, int y, int w, int h)
    {
        this(w, h, AnchorPoint.TL, x, y);
    }
    
    public void init(int screenW, int screenH)
    {
        x = headAnchor.widthToX(screenW);
        y = headAnchor.heightToY(screenH);
        
        x += innerAnchor.widthToX(w) - w;
        y += innerAnchor.heightToY(h) - h;
        
        x += offX;
        y += offY;
    }
    
    public boolean isMouseOver(int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + w
                && mouseY >= y && mouseY < y + h;
    }
}
