package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FunctionalTextStyle extends BaseStyle
{
    protected FontRenderer fontRenderer;
    protected Supplier<List<String>> text;
    protected AnchorPoint anchorPoint;
    protected Color4f color;
    
    public FunctionalTextStyle(FontRenderer fontRenderer, Supplier<List<String>> text, AnchorPoint anchorPoint, Color4f color)
    {
        this.fontRenderer = fontRenderer;
        this.text = text;
        this.anchorPoint = anchorPoint;
        this.color = color;
    }
    
    public FunctionalTextStyle(FontRenderer fontRenderer, Supplier<List<String>> text, AnchorPoint anchorPoint)
    {
        this(fontRenderer, text, anchorPoint, Color4f.NEUTRAL);
    }
    
    public FunctionalTextStyle(FontRenderer fontRenderer, Supplier<List<String>> text, Color4f color)
    {
        this(fontRenderer, text, AnchorPoint.M, color);
    }
    
    public FunctionalTextStyle(FontRenderer fontRenderer, Supplier<List<String>> text)
    {
        this(fontRenderer, text, AnchorPoint.M, Color4f.NEUTRAL);
    }
    
    @Override
    public void renderWidget(Widget widget, float deltaTick, int mouseX, int mouseY)
    {
        Dimensions dims = widget.getDimensions();
        List<String> text = this.text.get().stream().map(line -> fontRenderer.splitStringToWidth(dims.w, line))
                .flatMap(Collection::stream).collect(Collectors.toList());
        
        int textHeight = fontRenderer.getTextHeight(text);
        int textWidth = fontRenderer.getTextWidth(text);
        
        int x = dims.x + anchorPoint.widthToX(dims.w);
        int y = dims.y + anchorPoint.heightToY(dims.h) - anchorPoint.heightToY(textHeight);
        
        color.glColor4f();
        if(anchorPoint.POS_X == 0.0F)
        {
            fontRenderer.renderLeftAligned(x, y, text);
        }
        else if(anchorPoint.POS_X == 1.0F)
        {
            fontRenderer.renderRightAligned(x, y, text);
        }
        else
        {
            fontRenderer.renderMiddleAligned(x, y, text);
        }
    }
}
