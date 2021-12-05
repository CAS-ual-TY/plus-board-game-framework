package sweng_plus.boardgames.ludo.gui.widget;

import org.joml.Vector2i;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.texture.SpriteTexture;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.function.Consumer;

public class DiceAnimationWidget extends Widget
{
    protected SpriteTexture[] sprites;
    protected int animationLength;
    protected Vector2i[] spritePositions;
    protected Consumer<DiceAnimationWidget> onEnd;
    
    protected int timer;
    
    public DiceAnimationWidget(IScreenHolder screenHolder, Dimensions dimensions, SpriteTexture[] sprites, int animationLength, Vector2i[] spritePositions, Consumer<DiceAnimationWidget> onEnd)
    {
        super(screenHolder, dimensions);
        this.sprites = sprites;
        this.animationLength = animationLength;
        this.spritePositions = spritePositions;
        this.onEnd = onEnd;
        timer = 0;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        sprites[timer].render(0, 0);
        if(timer == animationLength)
        {
            onEnd.accept(this);
        }
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        timer++;
    }
}
