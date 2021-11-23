package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.userinterface.gui.widget.IWidget;
import sweng_plus.framework.userinterface.gui.widget.IWidgetParent;
import sweng_plus.framework.userinterface.input.IInputListener;
import sweng_plus.framework.userinterface.input.INestedInputListener;

import java.util.LinkedList;
import java.util.List;

public class Screen implements IWidgetParent, INestedRenderable, INestedInputListener
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
    
    /** Wird immer gecallt, wenn das Window resized wird, oder der Screen zum ersten mal wieder aktiv ist */
    public void initScreen(int screenW, int screenH)
    {
        this.screenW = screenW;
        this.screenH = screenH;
        
        initSubWidgets(this);
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        INestedRenderable.super.update(mouseX, mouseY);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        INestedRenderable.super.render(deltaTick, mouseX, mouseY);
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        INestedInputListener.super.mouseButtonPressed(mouseX, mouseY, mouseButton, mods);
    }
    
    @Override
    public void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
        INestedInputListener.super.mouseButtonReleased(mouseX, mouseY, mouseButton, mods);
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        INestedInputListener.super.keyPressed(key, mods);
    }
    
    @Override
    public void keyRepeated(int key, int mods)
    {
        INestedInputListener.super.keyRepeated(key, mods);
    }
    
    @Override
    public void keyReleased(int key, int mods)
    {
        INestedInputListener.super.keyReleased(key, mods);
    }
    
    @Override
    public void charTyped(char character)
    {
        INestedInputListener.super.charTyped(character);
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
    
    @Override
    public List<? extends IRenderable> getRenderables()
    {
        return getWidgets();
    }
    
    @Override
    public List<? extends IInputListener> getListeners()
    {
        return getWidgets();
    }
}
