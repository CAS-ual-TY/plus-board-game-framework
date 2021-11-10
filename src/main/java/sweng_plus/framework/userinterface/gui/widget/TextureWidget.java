package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Texture;

public class TextureWidget extends Widget
{
    public Texture texture;
    
    public TextureWidget(Screen screen, Dimensions dimensions, Texture texture)
    {
        super(screen, dimensions);
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
