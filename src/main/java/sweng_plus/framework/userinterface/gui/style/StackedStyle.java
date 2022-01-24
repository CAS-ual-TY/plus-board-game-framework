package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.Rectangle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StackedStyle extends BaseStyle
{
    protected LinkedList<IStyle> styles;
    
    public StackedStyle()
    {
        styles = new LinkedList<>();
    }
    
    public StackedStyle(List<IStyle> styles)
    {
        this();
        this.styles.addAll(styles);
    }
    
    public StackedStyle(IStyle[] styles)
    {
        this();
        this.styles.addAll(Arrays.asList(styles));
    }
    
    public StackedStyle(IStyle style)
    {
        this();
        styles.add(style);
    }
    
    public StackedStyle(IStyle style1, IStyle style2)
    {
        this();
        styles.add(style1);
        styles.add(style2);
    }
    
    public StackedStyle reverse()
    {
        LinkedList<IStyle> reversed = new LinkedList<>();
        styles.forEach(reversed::addFirst);
        styles = reversed;
        return this;
    }
    
    public LinkedList<IStyle> getStyles()
    {
        return styles;
    }
    
    @Override
    public StackedStyle stack(IStyle style)
    {
        if(style instanceof StackedStyle stackedStyle)
        {
            styles.addAll(stackedStyle.getStyles());
        }
        else
        {
            styles.add(style);
        }
        
        return this;
    }
    
    @Override
    public void initStyle(Rectangle parentDimensions)
    {
        super.initStyle(parentDimensions);
        styles.forEach(style -> style.initStyle(dimensions));
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        styles.forEach(style -> style.update(mouseX, mouseY));
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        styles.forEach(style -> style.render(deltaTick, mouseX, mouseY));
    }
}
