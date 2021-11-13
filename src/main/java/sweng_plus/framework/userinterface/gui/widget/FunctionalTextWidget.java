package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import java.util.List;
import java.util.function.Supplier;

public class FunctionalTextWidget extends TextWidget
{
    protected Supplier<List<String>> text;
    
    public FunctionalTextWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer, Supplier<List<String>> text, Color4f color)
    {
        super(parent, dimensions, renderer, null, color);
        this.text = text;
    }
    
    public FunctionalTextWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer, Supplier<List<String>> text)
    {
        this(parent, dimensions, renderer, null, Color4f.NEUTRAL);
        this.text = text;
    }
    
    @Override
    public List<String> getText()
    {
        return text.get();
    }
}
