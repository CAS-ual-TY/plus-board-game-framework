package sweng_plus.boardgames.mill.gui.util;

import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;

import java.io.IOException;

public class MillTextures
{
    public static Texture figureBlack;
    public static Texture figureWhite;
    public static Texture logo;
    public static Texture background;
    
    public static void load() throws IOException
    {
    
        figureBlack = TextureHelper.createTexture("/textures/mill_figure_black.png");
        figureWhite = TextureHelper.createTexture("/textures/mill_figure_white.png");
        logo = TextureHelper.createTexture("/textures/background/logo2.png");
        background = TextureHelper.createTexture("/textures/background/background.png");
    }
}
