package sweng_plus.boardgames.ludo.gui.widget;

import org.joml.RoundingMode;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.texture.SpriteTexture;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class DiceAnimationWidget extends Widget
{
    public static final int ANIMATION_SPEED = 2;
    
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
            
            Vector2d pos = new Vector2d(spritePositions[timer]);
            Vector2d mot = new Vector2d(spritePositions[timer + ANIMATION_SPEED]).sub(pos).mul(deltaTick);
            Vector2i posI = pos.add(mot).get(RoundingMode.HALF_UP, new Vector2i(0, 0));
            
            GL11.glPushMatrix();
            
            GL11.glScalef(2F, 2F, 2F);
            
            sprites[timer].render(posI.x() / 2, posI.y() / 2);
            
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        timer += ANIMATION_SPEED;
    }
    
    public boolean hasEnded()
    {
        return timer >= animationLength;
    }
}
