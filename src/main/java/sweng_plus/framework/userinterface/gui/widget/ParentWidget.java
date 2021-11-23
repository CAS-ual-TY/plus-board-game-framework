package sweng_plus.framework.userinterface.gui.widget;

import java.util.Arrays;
import java.util.List;

public class ParentWidget extends Widget implements IWidgetParent
{
    protected List<IWidget> widgets;
    
    public ParentWidget(IWidgetParent parent, Dimensions dimensions, List<IWidget> widgets)
    {
        super(parent, dimensions);
        this.widgets = widgets;
    }
    
    public ParentWidget(IWidgetParent parent, Dimensions dimensions, IWidget... widgets)
    {
        this(parent, dimensions, Arrays.asList(widgets));
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        IWidgetParent.super.update(mouseX, mouseY);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        IWidgetParent.super.render(deltaTick, mouseX, mouseY);
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        IWidgetParent.super.mouseButtonPressed(mouseX, mouseY, mouseButton, mods);
    }
    
    @Override
    public void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
        IWidgetParent.super.mouseButtonReleased(mouseX, mouseY, mouseButton, mods);
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        IWidgetParent.super.keyPressed(key, mods);
    }
    
    @Override
    public void keyRepeated(int key, int mods)
    {
        IWidgetParent.super.keyRepeated(key, mods);
    }
    
    @Override
    public void keyReleased(int key, int mods)
    {
        IWidgetParent.super.keyReleased(key, mods);
    }
    
    @Override
    public void charTyped(char character)
    {
        IWidgetParent.super.charTyped(character);
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
