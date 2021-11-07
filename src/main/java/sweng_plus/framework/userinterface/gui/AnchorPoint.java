package sweng_plus.framework.userinterface.gui;

/**
 * {@link #TL},
 * {@link #T},
 * {@link #TR},
 * {@link #L},
 * {@link #M},
 * {@link #R},
 * {@link #BL},
 * {@link #B},
 * {@link #BR}
 */
public enum AnchorPoint
{
    /** "Top-Left" */
    TL(0.0F, 0.0F),
    
    /** "Top" */
    T(0.5F, 0.0F),
    
    /** "Top-Right" */
    TR(1.0F, 0.0F),
    
    /** "Left" */
    L(0.0F, 0.5F),
    
    /** "Middle" */
    M(0.5F, 0.5F),
    
    /** "Right" */
    R(1.0F, 0.5F),
    
    /** "Bottom-Left" */
    BL(0.0F, 1.0F),
    
    /** "Bottom" */
    B(0.5F, 1.0F),
    
    /** "Bottom-Right" */
    BR(1.0F, 1.0F);
    
    public final float POS_X;
    public final float POS_Y;
    
    AnchorPoint(float x, float y)
    {
        POS_X = x;
        POS_Y = y;
    }
    
    public float getX()
    {
        return POS_X;
    }
    
    public float getY()
    {
        return POS_Y;
    }
    
    public int widthToX(int width)
    {
        return floatMultToIntShift(width, POS_X);
    }
    
    public int heightToY(int height)
    {
        return floatMultToIntShift(height, POS_Y);
    }
    
    public void widthHeightToXY(int width, int height, PositionConsumer positionConsumer)
    {
        positionConsumer.accept(widthToX(width), heightToY(height));
    }
    
    private int floatMultToIntShift(int integer, float factor)
    {
        if(factor == 0.0F)
            // integer * 0.0F = 0
            return 0;
        
        else if(factor == 0.5F)
            // integer * 0.5F = integer / 2 = integer >> 1 (= 1x rechts shift)
            return integer >> 1;
        
        else if(factor == 1.0F)
            // integer * 1.0F = integer
            return integer;
        
        throw new IllegalArgumentException("factor must be 0.0F, 0.5F or 1.0F");
    }
    
    public interface PositionConsumer
    {
        void accept(int posX, int posY);
    }
}
