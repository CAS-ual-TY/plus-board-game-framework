package sweng_plus.boardgames.ludo.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Texture;
import sweng_plus.framework.userinterface.gui.widget.*;

public class TexturedButtonWidget extends FunctionalButtonWidget
{
    protected ButtonAction buttonAction;
    protected Texture active;
    protected Texture inactive;
    
    public TexturedButtonWidget(IWidgetParent parent, Dimensions dimensions, ButtonAction buttonAction, Texture active, Texture inactive)
    {
        super(parent, dimensions, buttonAction);
        this.active = active;
        this.inactive = inactive;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        Color4f.NEUTRAL.glColor4f();
        
        Texture texture = updateMouseOver(deltaTick, mouseX, mouseY) ? active : inactive;
        texture.renderCornered(dimensions.x, dimensions.y, dimensions.w, dimensions.h);
    }
}
