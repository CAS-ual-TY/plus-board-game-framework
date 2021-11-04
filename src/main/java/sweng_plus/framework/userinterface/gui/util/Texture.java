package sweng_plus.framework.userinterface.gui.util;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    private int textureID;
    private String texture;
    
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
