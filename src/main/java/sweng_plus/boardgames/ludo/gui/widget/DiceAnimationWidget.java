package sweng_plus.boardgames.ludo.gui.widget;

import org.joml.Vector2i;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.texture.SpriteTexture;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class DiceAnimationWidget extends Widget
{
    protected SpriteTexture[] sprites;
    protected int animationLength;
    protected Vector2i[] spritePositions;
    
    protected int timer;
    
    public DiceAnimationWidget(IScreenHolder screenHolder, Dimensions dimensions, SpriteTexture[] sprites, int animationLength, Vector2i[] spritePositions)
    {
        super(screenHolder, dimensions);
        this.sprites = sprites;
        this.animationLength = animationLength;
        this.spritePositions = spritePositions;
        timer = 0;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        if(timer < animationLength)
        {
            Color4f.NEUTRAL.glColor4f();
            sprites[timer].render(0, 0);
        }
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        timer++;
    }
    
    public boolean hasEnded()
    {
        return timer >= animationLength;
    }
}
