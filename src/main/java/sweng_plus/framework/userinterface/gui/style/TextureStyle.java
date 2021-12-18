package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class TextureStyle extends BaseStyle
{
    protected Texture texture;
    protected Color4f color;
    
    public TextureStyle(Texture texture, Color4f color)
    {
        this.texture = texture;
        this.color = color;
    }
    
    public TextureStyle(Texture texture)
    {
        this.texture = texture;
        color = Color4f.NEUTRAL;
    }
    
    @Override
    public void renderWidget(Widget widget, float deltaTick, int mouseX, int mouseY)
    {
        color.glColor4f();
        
        texture.render(widget.getDimensions().x, widget.getDimensions().y, widget.getDimensions().w, widget.getDimensions().h);
    }
}
