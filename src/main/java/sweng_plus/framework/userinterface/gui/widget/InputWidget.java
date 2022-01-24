package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.style.IStyle;
import sweng_plus.framework.userinterface.gui.util.Dimensions;

import java.util.List;
import java.util.function.Consumer;

public class InputWidget extends SelectableWidget
{
    protected StringBuilder stringBuilder;
    protected Consumer<InputWidget> consumer;
    
    public InputWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle activeStyle, IStyle inactiveStyle, Consumer<InputWidget> consumer)
    {
        super(screenHolder, dimensions, activeStyle, inactiveStyle);
        this.consumer = consumer;
        
        stringBuilder = new StringBuilder();
    }
    
    public InputWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle activeStyle, IStyle inactiveStyle)
    {
        this(screenHolder, dimensions, activeStyle, inactiveStyle, (w) -> {});
    }
    
    public InputWidget setText(String text)
    {
        clearText();
        stringBuilder.append(text);
        return this;
    }
    
    public boolean tryDelete()
    {
        if(!isSelected || stringBuilder.isEmpty())
        {
            return false;
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return true;
    }
    
    public String getText()
    {
        return stringBuilder.toString();
    }
    
    public List<String> getTextAsList()
    {
        return List.of(getText());
    }
    
    public void clearText()
    {
        stringBuilder.setLength(0);
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        if(key == GLFW.GLFW_KEY_BACKSPACE)
        {
            tryDelete();
        }
        
        if(key == GLFW.GLFW_KEY_ENTER)
        {
            consumer.accept(this);
        }
    }
    
    @Override
    public void keyRepeated(int key, int mods)
    {
        if(key == GLFW.GLFW_KEY_BACKSPACE)
        {
            tryDelete();
        }
    }
    
    @Override
    public void charTyped(char character)
    {
        if(isSelected)
        {
            stringBuilder.append(character);
        }
    }
}
