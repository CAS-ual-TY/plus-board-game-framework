package sweng_plus.framework.userinterface.gui.util;

import org.lwjgl.opengl.GL11;

public class Color4f
{
    public final float rf;
    public final float gf;
    public final float bf;
    public final float af;
    
    public Color4f(int r, int g, int b, int a)
    {
        rf = r / 255f;
        gf = g / 255f;
        bf = b / 255f;
        af = a / 255f;
    }
    
    public Color4f(int r, int g, int b)
    {
        // Byte.MAX_VALUE - Byte.MIN_VALUE = 127 - (-128) = 255 = Unsigned Byte MAX_VALUE
        this(r, g, b, Byte.MAX_VALUE - Byte.MIN_VALUE);
    }
    
    public Color4f(float r, float g, float b, float a)
    {
        rf = r;
        gf = g;
        bf = b;
        af = a;
    }
    
    public Color4f(float r, float g, float b)
    {
        this(r, g, b, 1.0F);
    }
    
    public void glColor3f()
    {
        GL11.glColor3f(rf, gf, bf);
    }
    
    public void glColor4f()
    {
        GL11.glColor4f(rf, gf, bf, af);
    }
}
