package sweng_plus.framework.userinterface.gui.util;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Texture
{
    private final int textureID;
    private final String texture;
    
    public Texture(int textureID, String texture)
    {
        this.textureID = textureID;
        this.texture = texture;
    }
    
    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }
}
