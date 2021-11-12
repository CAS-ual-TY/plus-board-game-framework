package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import java.util.List;

public class TextWidget extends Widget
{
    protected FontRenderer renderer;
    protected List<String> text;
    protected Color4f color;
    
    public TextWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer, List<String> text, Color4f color)
    {
        super(parent, dimensions);
        this.renderer = renderer;
        this.text = text;
        this.color = color;
        
        this.dimensions.w = renderer.getTextWidth(text);
        this.dimensions.h = renderer.getTextHeight(text);
    }
    
    public TextWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer, List<String> text)
    {
        this(parent, dimensions, renderer, text, Color4f.NEUTRAL);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        color.glColor4f();
        renderer.renderCentered(dimensions.x + dimensions.w / 2, dimensions.y + dimensions.h / 2, text);
    }
}
