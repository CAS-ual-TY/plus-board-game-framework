package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.util.Color4f;

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
    public void renderStyle(float deltaTick, int mouseX, int mouseY)
    {
        color.glColor4f();
        
        texture.render(dimensions.x, dimensions.y, dimensions.w, dimensions.h);
    }
}
