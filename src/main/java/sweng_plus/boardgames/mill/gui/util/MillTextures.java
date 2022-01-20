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
        
        figureBlack = TextureHelper.createTexture("/mill/textures/mill_figure_black.png");
        figureWhite = TextureHelper.createTexture("/mill/textures/mill_figure_white.png");
        logo = TextureHelper.createTexture("/mill/textures/logo.png");
        background = TextureHelper.createTexture("/mill/textures/background.png");
    }
}
