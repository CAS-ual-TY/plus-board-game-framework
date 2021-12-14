package sweng_plus.boardgames.ludo.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalTextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.util.List;
import java.util.function.Supplier;

public class ChatWidget extends FunctionalTextWidget
{
    public ChatWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer fontRenderer, Supplier<List<String>> text, Color4f color)
    {
        super(screenHolder, dimensions, fontRenderer, text, color);
    }
    
    public ChatWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer fontRenderer, Supplier<List<String>> text)
    {
        super(screenHolder, dimensions, fontRenderer, text);
    }
    
    @Override
    public FunctionalTextWidget adjustSizeToText()
    {
        dimensions.h = fontRenderer.getTextHeight(getText());
        return this;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        updateMouseOver(deltaTick, mouseX, mouseY);
        
        color.glColor4f();
        fontRenderer.render(dimensions.x, dimensions.y, getText());
    }
}
