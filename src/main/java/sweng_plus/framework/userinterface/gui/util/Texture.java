package sweng_plus.framework.userinterface.gui.util;

import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    public static final Texture NULL_TEXTURE = new Texture(0, "null", 1, 1);
    
    private final int textureID;
    private final String texture;
    
    private final int width;
    private final int height;
    
    public Texture(int textureID, String textureName, int width, int height)
    {
        this.textureID = textureID;
        this.texture = textureName;
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
    
    public void render(int x, int y, int w, int h, int texX, int texY, int texW, int texH)
    {
        bind();
        
        glBegin(GL_QUADS);
        
        glTexCoord2f(texX / (float) width, texY / (float) height);
        glVertex3f(x, y, 0);
        
        glTexCoord2f(texX / (float) width, (texY + texH) / (float) height);
        glVertex3f(x, y + h, 0);
        
        glTexCoord2f((texX + texW) / (float) width, (texY + texH) / (float) height);
        glVertex3f(x + w, y + h, 0);
        
        glTexCoord2f((texX + texW) / (float) width, texY / (float) height);
        glVertex3f(x + w, y, 0);
        
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
}
