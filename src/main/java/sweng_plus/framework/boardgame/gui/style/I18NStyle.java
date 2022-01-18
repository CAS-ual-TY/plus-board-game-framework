package sweng_plus.framework.boardgame.gui.style;

import sweng_plus.framework.boardgame.I18N;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.style.FunctionalTextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import java.util.List;
import java.util.stream.Collectors;

public class I18NStyle extends FunctionalTextStyle
{
    public I18NStyle(FontRenderer fontRenderer, List<String> text, AnchorPoint anchorPoint, Color4f color)
    {
        super(fontRenderer, () -> text.stream().map(I18N::translate).collect(Collectors.toList()), anchorPoint, color);
    }
    
    public I18NStyle(FontRenderer fontRenderer, List<String> text, AnchorPoint anchorPoint)
    {
        this(fontRenderer, text, anchorPoint, Color4f.NEUTRAL);
    }
    
    public I18NStyle(FontRenderer fontRenderer, List<String> text, Color4f color)
    {
        this(fontRenderer, text, AnchorPoint.M, color);
    }
    
    public I18NStyle(FontRenderer fontRenderer, List<String> text)
    {
        this(fontRenderer, text, Color4f.NEUTRAL);
    }
    
    public I18NStyle(FontRenderer fontRenderer, String text, AnchorPoint anchorPoint, Color4f color)
    {
        this(fontRenderer, List.of(text), anchorPoint, color);
    }
    
    public I18NStyle(FontRenderer fontRenderer, String text, AnchorPoint anchorPoint)
    {
        this(fontRenderer, List.of(text), anchorPoint, Color4f.NEUTRAL);
    }
    
    public I18NStyle(FontRenderer fontRenderer, String text, Color4f color)
    {
        this(fontRenderer, List.of(text), AnchorPoint.M, color);
    }
    
    public I18NStyle(FontRenderer fontRenderer, String text)
    {
        this(fontRenderer, List.of(text), Color4f.NEUTRAL);
    }
}
