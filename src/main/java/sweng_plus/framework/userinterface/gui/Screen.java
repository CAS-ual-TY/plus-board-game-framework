package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.userinterface.gui.widget.IWidget;
import sweng_plus.framework.userinterface.gui.widget.IWidgetParent;

import java.util.LinkedList;
import java.util.List;

public class Screen implements IWidgetParent
{
    public final IScreenHolder screenHolder;
    
    public int screenW;
    public int screenH;
    
    public List<IWidget> widgets;
    
    public Screen(IScreenHolder screenHolder)
    {
        this.screenHolder = screenHolder;
        widgets = new LinkedList<>();
    }
    
    public IScreenHolder getScreenHolder()
    {
        return screenHolder;
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
    public void initWidgetParent(IWidgetParent parent)
    {
    
    }
    
    /** Wird immer gecallt, wenn das Window resized wird, oder der Screen zum ersten mal wieder aktiv ist */
    public void initScreen(int screenW, int screenH)
    {
        this.screenW = screenW;
        this.screenH = screenH;
        
        IWidgetParent.super.initWidget(this);
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
        return 0;
    }
    
    @Override
    public int getParentY()
    {
        return 0;
    }
    
    @Override
    public int getParentW()
    {
        return screenW;
    }
    
    @Override
    public int getParentH()
    {
        return screenH;
    }
    
    @Override
    public List<IWidget> getWidgets()
    {
        return widgets;
    }
}
