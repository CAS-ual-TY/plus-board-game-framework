package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.Rectangle;

public class MatrixStyle extends BaseStyle
{
    protected IStyle[][] subStyles;
    protected float[] horizontalParts;
    protected float[] verticalParts;
    
    protected int[] xs;
    protected int[] ys;
    
    protected int[] ws;
    protected int[] hs;
    
    public MatrixStyle(IStyle[][] subStyles, float[] horizontalParts, float[] verticalParts)
    {
        if(subStyles.length == 0 || subStyles.length != verticalParts.length)
        {
            throw new IllegalArgumentException();
        }
        
        for(IStyle[] row : subStyles)
        {
            if(row.length == 0 || row.length != horizontalParts.length)
            {
                throw new IllegalArgumentException();
            }
        }
        
        this.subStyles = subStyles;
        
        float horizontalTotal = 0;
        float verticalTotal = 0;
        
        for(float f : horizontalParts)
        {
            horizontalTotal += f;
        }
        
        for(float f : verticalParts)
        {
            verticalTotal += f;
        }
        
        for(int i = 0; i < horizontalParts.length; ++i)
        {
            horizontalParts[i] /= horizontalTotal;
        }
        
        for(int i = 0; i < verticalParts.length; ++i)
        {
            verticalParts[i] /= verticalTotal;
        }
        
        this.horizontalParts = horizontalParts;
        this.verticalParts = verticalParts;
        
        xs = new int[horizontalParts.length];
        ys = new int[verticalParts.length];
        
        ws = new int[horizontalParts.length];
        hs = new int[verticalParts.length];
    }
    
    @Override
    public void initStyle(Rectangle parentDimensions)
    {
        super.initStyle(parentDimensions.clone());
        
        xs[0] = dimensions.x;
        ys[0] = dimensions.y;
        
        for(int i = 0; i < horizontalParts.length - 1; ++i)
        {
            ws[i] = Math.round(horizontalParts[i] * dimensions.w);
            xs[i + 1] = xs[i] + ws[i];
        }
        
        for(int i = 0; i < verticalParts.length - 1; ++i)
        {
            hs[i] = Math.round(verticalParts[i] * dimensions.h);
            ys[i + 1] = ys[i] + hs[i];
        }
        
        ws[horizontalParts.length - 1] = dimensions.w - (xs[horizontalParts.length - 1] - dimensions.x);
        hs[verticalParts.length - 1] = dimensions.h - (ys[verticalParts.length - 1] - dimensions.y);
        
        for(int y = 0; y < verticalParts.length; ++y)
        {
            for(int x = 0; x < horizontalParts.length; ++x)
            {
                subStyles[y][x].initStyle(new Rectangle(xs[x], ys[y], ws[x], hs[y]));
            }
        }
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        for(IStyle[] row : subStyles)
        {
            for(IStyle style : row)
            {
                style.update(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        for(IStyle[] styles : subStyles)
        {
            for(IStyle style : styles)
            {
                style.render(deltaTick, mouseX, mouseY);
            }
        }
    }
}
