package sweng_plus.boardgames.ludo.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Texture;
import sweng_plus.framework.userinterface.gui.widget.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;

public class TexturedButtonWidget extends FunctionalButtonWidget
{
    protected ButtonAction buttonAction;
    protected Texture active;
    protected Texture inactive;
    
    public TexturedButtonWidget(IScreenHolder screenHolder, Dimensions dimensions, ButtonAction buttonAction, Texture active, Texture inactive)
    {
        super(screenHolder, dimensions, buttonAction);
        this.active = active;
        this.inactive = inactive;
    }
    
    @Override
    public void renderWidget(float deltaTick, int mouseX, int mouseY)
    {
        Color4f.NEUTRAL.glColor4f();
        
        Texture texture = updateMouseOver(deltaTick, mouseX, mouseY) ? active : inactive;
        texture.renderCornered(dimensions.x, dimensions.y, dimensions.w, dimensions.h);
    }
}
