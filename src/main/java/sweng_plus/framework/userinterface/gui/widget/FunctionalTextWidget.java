package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import java.util.List;
import java.util.function.Supplier;

public class FunctionalTextWidget extends Widget
{
    protected FontRenderer renderer;
    protected Supplier<List<String>> text;
    protected Color4f color;
    
    public FunctionalTextWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer, Supplier<List<String>> text, Color4f color)
    {
        super(parent, dimensions);
        this.renderer = renderer;
        this.text = text;
        this.color = color;
    }
    
    public FunctionalTextWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer, Supplier<List<String>> text)
    {
        this(parent, dimensions, renderer, null, Color4f.NEUTRAL);
        this.text = text;
    }
    
    public FunctionalTextWidget adjustSizeToText()
    {
        dimensions.w = renderer.getTextWidth(getText());
        dimensions.h = renderer.getTextHeight(getText());
        
        return this;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        color.glColor4f();
        renderer.renderCentered(dimensions.x + dimensions.w / 2, dimensions.y + dimensions.h / 2, getText());
    }
    
    public List<String> getText()
    {
        return text.get();
    }
}
