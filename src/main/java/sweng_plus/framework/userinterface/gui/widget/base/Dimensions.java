package sweng_plus.framework.userinterface.gui.widget.base;

import sweng_plus.framework.userinterface.gui.util.AnchorPoint;

import java.util.Objects;

public class Dimensions implements Cloneable
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
    
    public Dimensions(AnchorPoint headAnchor, AnchorPoint innerAnchor)
    {
        this(0, 0, headAnchor, innerAnchor, 0, 0);
    }
    
    public Dimensions(int w, int h, AnchorPoint anchor, int offX, int offY)
    {
        this(w, h, anchor, anchor, offX, offY);
    }
    
    public Dimensions(int w, int h, AnchorPoint anchor)
    {
        this(w, h, anchor, 0, 0);
    }
    
    public Dimensions(AnchorPoint anchor, int offX, int offY)
    {
        this(0, 0, anchor, offX, offY);
    }
    
    public Dimensions(AnchorPoint anchor)
    {
        this(anchor, 0, 0);
    }
    
    public Dimensions(int x, int y, int w, int h)
    {
        this(w, h, AnchorPoint.TL, x, y);
    }
    
    public void init(int screenX, int screenY, int screenW, int screenH)
    {
        x = screenX + headAnchor.widthToX(screenW) - innerAnchor.widthToX(w) + offX;
        y = screenY + headAnchor.heightToY(screenH) - innerAnchor.heightToY(h) + offY;
    }
    
    public boolean isMouseOver(int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + w
                && mouseY >= y && mouseY < y + h;
    }
    
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Dimensions clone()
    {
        return new Dimensions(w, h, headAnchor, innerAnchor, offX, offY);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        
        Dimensions that = (Dimensions) o;
        return x == that.x && y == that.y &&
                w == that.w && h == that.h &&
                offX == that.offX && offY == that.offY &&
                headAnchor == that.headAnchor && innerAnchor == that.innerAnchor;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(x, y, w, h, offX, offY, headAnchor, innerAnchor);
    }
    
    @Override
    public String toString()
    {
        return "Dimensions{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                ", offX=" + offX +
                ", offY=" + offY +
                ", headAnchor=" + headAnchor +
                ", innerAnchor=" + innerAnchor +
                '}';
    }
}
