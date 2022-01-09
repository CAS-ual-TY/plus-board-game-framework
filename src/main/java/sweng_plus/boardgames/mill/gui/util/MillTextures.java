package sweng_plus.boardgames.mill.gui.util;

import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;

import java.io.IOException;

public class MillTextures
{
    public static Texture activeButton;
    public static Texture inactiveButton;
    public static Texture node;
    public static Texture logo;
    public static Texture background;
    
    public static void load() throws IOException
    {
        activeButton = TextureHelper.createTexture("/textures/button_active.png");
        inactiveButton = TextureHelper.createTexture("/textures/button_inactive.png");
        node = TextureHelper.createTexture("/textures/node.png");
        logo = TextureHelper.createTexture("/textures/background/logo2.png");
        background = TextureHelper.createTexture("/textures/background/background.png");
    }
}
