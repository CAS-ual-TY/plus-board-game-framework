package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.util.List;

public class TextWidget extends FunctionalTextWidget
{
    public TextWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer renderer, List<String> text, Color4f color)
    {
        super(screenHolder, dimensions, renderer, () -> text, color);
    }
    
    public TextWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer renderer, List<String> text)
    {
        this(screenHolder, dimensions, renderer, text, Color4f.NEUTRAL);
    }
    
    public TextWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer renderer, String text, Color4f color)
    {
        this(screenHolder, dimensions, renderer, List.of(text), color);
    }
    
    public TextWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer renderer, String text)
    {
        this(screenHolder, dimensions, renderer, List.of(text));
    }
}
