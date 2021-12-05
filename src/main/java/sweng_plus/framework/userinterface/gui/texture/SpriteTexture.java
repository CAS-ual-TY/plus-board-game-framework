package sweng_plus.framework.userinterface.gui.texture;

import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class SpriteTexture extends Texture
{
    protected final int spriteX;
    protected final int spriteY;
    protected final int spriteW;
    protected final int spriteH;
    
    public SpriteTexture(Texture baseTexture, int spriteX, int spriteY, int spriteW, int spriteH)
    {
        super(baseTexture.getTextureID(), baseTexture.getTextureName(), baseTexture.getWidth(), baseTexture.getHeight());
        this.spriteX = spriteX;
        this.spriteY = spriteY;
        this.spriteW = spriteW;
        this.spriteH = spriteH;
    }
    
    @Override
    protected void doQuad(int x, int y, int w, int h, int texX, int texY, int texW, int texH)
    {
        super.doQuad(x, y, w, h, texX + spriteX, texY + spriteY, texW, texH);
    }
    
    @Override
    public SpriteTexture[] makeSprites(int spritesWidth, int spritesHeight)
    {
        throw new IllegalStateException();
    }
}
