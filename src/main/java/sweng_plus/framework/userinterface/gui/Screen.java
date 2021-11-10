package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.userinterface.IInputListener;
import sweng_plus.framework.userinterface.INestedInputListener;
import sweng_plus.framework.userinterface.gui.widget.IWidget;

import java.util.LinkedList;
import java.util.List;

public class Screen implements INestedInputListener
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
    
    /** Wird immer gecallt, wenn das Window resized wird, oder der Screen zum ersten mal wieder aktiv ist */
    public void init(int screenW, int screenH)
    {
        this.screenW = screenW;
        this.screenH = screenH;
        
        for(IWidget w : widgets)
        {
            w.init(screenW, screenH);
        }
    }
    
    @Override
    public List<? extends IInputListener> getListeners()
    {
        return widgets;
    }
    
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        for(IWidget w : widgets)
        {
            w.render(deltaTick, mouseX, mouseY);
        }
    }
    
    public void update(int mouseX, int mouseY)
    {
        for(IWidget w : widgets)
        {
            w.update(mouseX, mouseY);
        }
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
    public void keyReleased(int key, int mods)
    {
        INestedInputListener.super.keyReleased(key, mods);
    }
    
    @Override
    public void charTyped(char character)
    {
        INestedInputListener.super.charTyped(character);
    }
}
