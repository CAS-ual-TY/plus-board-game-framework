package sweng_plus.framework.userinterface.gui.texture;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    public static final Texture NULL_TEXTURE = new Texture(0, "null", 1, 1);
    
    protected final int textureID;
    protected final String texture;

    protected final int width;
    protected final int height;
    
    public Texture(int textureID, String textureName, int width, int height)
    {
        this.textureID = textureID;
        texture = textureName;
        this.width = width;
        this.height = height;
    }
    
    public int getTextureID()
    {
        return textureID;
    }
    
    public String getTextureName()
    {
        return texture;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }
    
    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    protected void doQuad(int x, int y, int w, int h, int texX, int texY, int texW, int texH)
    {
        glTexCoord2f(texX / (float) width, texY / (float) height);
        glVertex3f(x, y, 0);
        
        glTexCoord2f(texX / (float) width, (texY + texH) / (float) height);
        glVertex3f(x, y + h, 0);
        
        glTexCoord2f((texX + texW) / (float) width, (texY + texH) / (float) height);
        glVertex3f(x + w, y + h, 0);
        
        glTexCoord2f((texX + texW) / (float) width, texY / (float) height);
        glVertex3f(x + w, y, 0);
    }
    
    public void render(int x, int y, int w, int h, int texX, int texY, int texW, int texH)
    {
        bind();
        glBegin(GL_QUADS);
        doQuad(x, y, w, h, texX, texY, texW, texH);
        glEnd();
        unbind();
    }
    
    public void render(int x, int y, int w, int h, int u, int v)
    {
        render(x, y, w, h, u, v, w, h);
    }
    
    public void render(int x, int y, int w, int h)
    {
        render(x, y, w, h, 0, 0);
    }
    
    public void render(int x, int y)
    {
        render(x, y, width, height, 0, 0, width, height);
    }
    
    public void renderCornered(int x, int y, int w, int h)
    {
        int w1 = w / 2;
        int w2 = w - w1;
        int h1 = h / 2;
        int h2 = h - h1;
        
        bind();
        glBegin(GL_QUADS);
        
        doQuad(x, y, w1, h1, 0, 0, w1, h1);
        doQuad(x + w1, y, w2, h1, getWidth() - w2, 0, w2, h1);
        doQuad(x, y + h1, w1, h2, 0, getHeight() - h2, w1, h2);
        doQuad(x + w1, y + h1, w2, h2, getWidth() - w2, getHeight() - h2, w2, h2);
        
        glEnd();
        unbind();
    }
    
    public SpriteTexture[] makeSprites(int spritesWidth, int spritesHeight)
    {
        int spritesAmtX = width/spritesWidth;
        int spritesAmtY = height/spritesHeight;
        
        SpriteTexture[] sprites = new SpriteTexture[spritesAmtX * spritesAmtY];
        
        for(int y = 0; y < spritesAmtY; y++)
        {
            for(int x = 0; x < spritesAmtX; x++)
            {
                sprites[x + y*spritesAmtX] = new SpriteTexture(this, x*spritesWidth, y*spritesHeight, spritesWidth, spritesHeight);
            }
        }
        
        return sprites;
    }
}
