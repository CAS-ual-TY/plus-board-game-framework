package sweng_plus.framework.userinterface.gui.font;

import sweng_plus.framework.userinterface.gui.util.Texture;
import sweng_plus.framework.userinterface.gui.util.TextureHelper;

import java.util.List;

public class FontRenderer
{
    private final FontInfo fontInfo;
    private Texture texture;
    
    public FontRenderer(FontInfo fontInfo)
    {
        this.fontInfo = fontInfo;
        texture = TextureHelper.createTexture(fontInfo.getFont().getName() + "_" + fontInfo.getFont().getSize(), fontInfo.getImage());
    }
    
    public int getTextWidth(String text)
    {
        int width = 0;
        
        for(char c : text.toCharArray())
        {
            width += fontInfo.getCharInfo(c).getCharW();
        }
        
        return width;
    }
    
    public int getHeight()
    {
        return fontInfo.getHeight();
    }
    
    public FontInfo getFontInfo()
    {
        return fontInfo;
    }
    
    public void render(int x, int y, String text)
    {
        int x0 = x;
        
        for(char c : text.toCharArray())
        {
            FontInfo.CharInfo info = fontInfo.getCharInfo(c);
            texture.render(x0, y, info.getCharW(), fontInfo.getHeight(), info.getCharX(), 0);
            x0 += info.getCharW();
        }
    }
    
    public void render(int x, int y, List<String> text)
    {
        int y0 = y;
        
        for(String line : text)
        {
            render(x, y0, line);
            y0 += getHeight();
        }
    }
    
    public void renderCentered(int x, int y, String text)
    {
        int w = getTextWidth(text);
        int h = getHeight();
        
        render(x - w / 2, y - h / 2, text);
    }
}
