package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class MarginStyle extends BaseStyle
{
    protected IStyle subStyle;
    protected int margin;
    protected AnchorPoint marginSides;
    protected boolean inside;
    
    public MarginStyle(IStyle subStyle, int margin, AnchorPoint marginSides, boolean inside)
    {
        this.subStyle = subStyle;
        this.margin = margin;
        this.marginSides = marginSides;
        this.inside = inside;
    }
    
    public MarginStyle(IStyle subStyle, int margin, AnchorPoint marginSides)
    {
        this(subStyle, margin, marginSides, true);
    }
    
    public MarginStyle(IStyle subStyle, int margin)
    {
        this(subStyle, margin, AnchorPoint.M, true);
    }
    
    @Override
    public void initStyle(Dimensions parentDimensions)
    {
        super.initStyle(parentDimensions.clone());
        
        int margin = inside ? this.margin : -this.margin;
        
        if(marginSides == AnchorPoint.M || marginSides.POS_X == 0.0F)
        {
            dimensions.x += margin;
            dimensions.w -= margin;
        }
        
        if(marginSides == AnchorPoint.M || marginSides.POS_X == 1.0F)
        {
            dimensions.w -= margin;
        }
        
        if(marginSides == AnchorPoint.M || marginSides.POS_Y == 0.0F)
        {
            dimensions.y += margin;
            dimensions.h -= margin;
        }
        
        if(marginSides == AnchorPoint.M || marginSides.POS_Y == 1.0F)
        {
            dimensions.h -= margin;
        }
        
        subStyle.initStyle(dimensions);
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        subStyle.update(mouseX, mouseY);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        subStyle.render(deltaTick, mouseX, mouseY);
    }
}
