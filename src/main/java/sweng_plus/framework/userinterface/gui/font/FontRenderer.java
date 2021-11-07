package sweng_plus.framework.userinterface.gui.font;

import sweng_plus.framework.userinterface.gui.util.Texture;
import sweng_plus.framework.userinterface.gui.util.TextureHelper;

public class FontRenderer
{
    private final FontInfo font;
    private Texture texture;
    
    public FontRenderer(FontInfo font)
    {
        this.font = font;
        texture = TextureHelper.createTexture(font.getFont().getName() + "_" + font.getFont().getSize(), font.getImage());
    }
    
    public int getTextWidth(String text)
    {
        int width = 0;
        
        for(char c : text.toCharArray())
        {
            width += font.getCharInfo(c).getCharW();
        }
        
        return width;
    }
    
    public void render(int x, int y, String text)
    {
        int x0 = x;
        
        for(char c : text.toCharArray())
        {
            FontInfo.CharInfo info = font.getCharInfo(c);
            texture.render(x0, y, info.getCharW(), font.getHeight(), info.getCharX(), 0);
            x0 += info.getCharW();
        }
    }
}
