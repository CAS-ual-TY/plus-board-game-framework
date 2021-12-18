package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class CorneredTextureStyle extends BaseStyle
{
    protected Texture texture;
    protected Color4f color;
    
    public CorneredTextureStyle(Texture texture, Color4f color)
    {
        this.texture = texture;
        this.color = color;
    }
    
    public CorneredTextureStyle(Texture texture)
    {
        this.texture = texture;
        color = Color4f.NEUTRAL;
    }
    
    @Override
    public void renderWidget(Widget widget, float deltaTick, int mouseX, int mouseY)
    {
        color.glColor4f();
        
        texture.renderCornered(widget.getDimensions().x, widget.getDimensions().y, widget.getDimensions().w, widget.getDimensions().h);
    }
}
