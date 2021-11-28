package sweng_plus.framework.userinterface.gui.util;

import org.lwjgl.opengl.GL11;

public class Color4f
{
    public static final Color4f NEUTRAL = new Color4f(1F, 1F, 1F, 1F);
    public static final Color4f HALF_VISIBLE = new Color4f(1F, 1F, 1F, 0.5F);
    public static final Color4f BACKGROUND = new Color4f(0F, 0F, 0F, 0.5F);
    
    public static final Color4f WHITE = NEUTRAL;
    public static final Color4f BLACK = new Color4f(0F, 0F, 0F, 1F);
    public static final Color4f GREY = new Color4f(0.5F, 0.5F, 0.5F, 1F);
    public static final Color4f LIGHT_GREY = new Color4f(0.33F, 0.33F, 0.33F, 1F);
    public static final Color4f DARK_GREY = new Color4f(0.66F, 0.66F, 0.66F, 1F);
    
    public static final Color4f RED = new Color4f(1F, 0F, 0F, 1F);
    public static final Color4f GREEN = new Color4f(0F, 1F, 0F, 1F);
    public static final Color4f BLUE = new Color4f(0F, 0F, 1F, 1F);
    
    public static final Color4f YELLOW = new Color4f(1F, 1F, 0F, 1F);
    public static final Color4f PURPLE = new Color4f(1F, 0F, 1F, 1F);
    public static final Color4f CYAN = new Color4f(0F, 1F, 1F, 1F);
    
    public final float r;
    public final float g;
    public final float b;
    public final float a;
    
    public Color4f(int r, int g, int b, int a)
    {
        this.r = r / 255f;
        this.g = g / 255f;
        this.b = b / 255f;
        this.a = a / 255f;
    }
    
    public Color4f(int r, int g, int b)
    {
        // Byte.MAX_VALUE - Byte.MIN_VALUE = 127 - (-128) = 255 = Unsigned Byte MAX_VALUE
        this(r, g, b, (int) Byte.MAX_VALUE - (int) Byte.MIN_VALUE);
    }
    
    public Color4f(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public Color4f(float r, float g, float b)
    {
        this(r, g, b, 1.0F);
    }
    
    public void glColor3fAlpha(float a)
    {
        GL11.glColor4f(r, g, b, a);
    }
    
    public void glColor4f()
    {
        glColor3fAlpha(a);
    }
    
    public void glColor3fStrength(float s)
    {
        GL11.glColor4f(r * s + (1F - s), g * s + (1F - s), b * s + (1F - s), a);
    }
    
    public void glColor4fApply(java.util.function.DoubleUnaryOperator function)
    {
        GL11.glColor4f(
                (float) function.applyAsDouble(r),
                (float) function.applyAsDouble(g),
                (float) function.applyAsDouble(b),
                a);
    }
    
    @Override
    public String toString()
    {
        return "Color4f{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
