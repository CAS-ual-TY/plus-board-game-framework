package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import java.util.List;

public class TextWidget extends FunctionalTextWidget
{
    public TextWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer, List<String> text, Color4f color)
    {
        super(parent, dimensions, renderer, () -> text, color);
    }
    
    public TextWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer, List<String> text)
    {
        this(parent, dimensions, renderer, text, Color4f.NEUTRAL);
    }
}
