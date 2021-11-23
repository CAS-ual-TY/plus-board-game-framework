package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;

import java.util.Arrays;
import java.util.List;

public class NestedWidget extends Widget implements INestedWidget
{
    protected List<IWidget> widgets;
    
    public NestedWidget(IScreenHolder screenHolder, Dimensions dimensions, List<IWidget> widgets)
    {
        super(screenHolder, dimensions);
        this.widgets = widgets;
    }
    
    public NestedWidget(IScreenHolder screenHolder, Dimensions dimensions, IWidget... widgets)
    {
        this(screenHolder, dimensions, Arrays.asList(widgets));
    }
    
    @Override
    public void initWidgetParent(INestedWidget parent)
    {
        dimensions.init(parent.getParentX(), parent.getParentY(), parent.getParentW(), parent.getParentH());
    }
    
    @Override
    public int getParentX()
    {
        return dimensions.x;
    }
    
    @Override
    public int getParentY()
    {
        return dimensions.y;
    }
    
    @Override
    public int getParentW()
    {
        return dimensions.w;
    }
    
    @Override
    public int getParentH()
    {
        return dimensions.h;
    }
    
    @Override
    public List<IWidget> getWidgets()
    {
        return widgets;
    }
}
