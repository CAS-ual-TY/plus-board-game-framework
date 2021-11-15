package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Texture;

public class TextureWidget extends Widget
{
    protected Texture texture;
    
    public TextureWidget(IWidgetParent parent, Dimensions dimensions, Texture texture)
    {
        super(parent, dimensions);
        this.texture = texture;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        Color4f.NEUTRAL.glColor4f();
        texture.render(dimensions.x, dimensions.y, dimensions.w, dimensions.h);
    }
}
