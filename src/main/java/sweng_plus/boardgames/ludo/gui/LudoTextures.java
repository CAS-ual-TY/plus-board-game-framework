package sweng_plus.boardgames.ludo.gui;

import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;

import java.io.IOException;

public class LudoTextures
{
    public static Texture activeButton;
    public static Texture inactiveButton;
    public static Texture node;
    
    public static void load() throws IOException
    {
        activeButton = TextureHelper.createTexture("src/test/resources/textures/button_test_active.png");
        inactiveButton = TextureHelper.createTexture("src/test/resources/textures/button_test_inactive.png");
        node = TextureHelper.createTexture("src/main/resources/textures/node.png");
    }
}
