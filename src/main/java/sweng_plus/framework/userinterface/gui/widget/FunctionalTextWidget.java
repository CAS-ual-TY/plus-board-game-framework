package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import java.util.List;
import java.util.function.Supplier;

public class FunctionalTextWidget extends Widget
{
    protected FontRenderer fontRenderer;
    protected Supplier<List<String>> text;
    protected Color4f color;
    
    public FunctionalTextWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer fontRenderer, Supplier<List<String>> text, Color4f color)
    {
        super(screenHolder, dimensions);
        this.fontRenderer = fontRenderer;
        this.text = text;
        this.color = color;
    }
    
    public FunctionalTextWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer fontRenderer, Supplier<List<String>> text)
    {
        this(screenHolder, dimensions, fontRenderer, null, Color4f.NEUTRAL);
        this.text = text;
    }
    
    public FunctionalTextWidget adjustSizeToText()
    {
        dimensions.w = fontRenderer.getTextWidth(getText());
        dimensions.h = fontRenderer.getTextHeight(getText());
        
        return this;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        updateMouseOver(deltaTick, mouseX, mouseY);
        
        color.glColor4f();
        fontRenderer.renderCentered(dimensions.x + dimensions.w / 2, dimensions.y + dimensions.h / 2, getText());
    }
    
    public List<String> getText()
    {
        return text.get();
    }
}
