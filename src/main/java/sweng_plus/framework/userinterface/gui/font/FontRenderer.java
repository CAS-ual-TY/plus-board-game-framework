package sweng_plus.framework.userinterface.gui.font;

import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("Convert2MethodRef")
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
    
    public int getTextHeight(String text) {return getHeight();}
    
    public int getTextWidth(List<String> text)
    {
        return text.stream().mapToInt(line -> getTextWidth(line)).max().orElse(0);
    }
    
    public int getTextHeight(List<String> text)
    {
        return text.size() * getHeight();
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
    
    public void renderCentered(int x, int y, List<String> text)
    {
        int h = getTextHeight(text);
        
        int x0;
        int y0 = y - h / 2;
        
        for(String line : text)
        {
            x0 = x - getTextWidth(line) / 2;
            render(x0, y0, line);
            y0 += getHeight();
        }
    }
    
    public String trimStringToWidth(int w, String text)
    {
        for(int i = 1; i < text.length(); ++i)
        {
            if(getTextWidth(text.substring(0, i)) > w)
            {
                return text.substring(0, i - 1);
            }
        }
        
        return text;
    }
    
    public List<String> splitStringToWidth(int w, String text)
    {
        List<String> list = new LinkedList<>();
        
        String[] words = text.split(" ");
        
        int w0 = w + 1;
        int wordW;
        StringBuilder line = new StringBuilder();
        
        final int spaceW = getTextWidth(" ");
        
        for(String word : words)
        {
            wordW = getTextWidth(word);
            
            if((w0 += wordW + spaceW) <= w)
            {
                line.append(" ").append(word);
            }
            else
            {
                list.add(line.toString());
                line.setLength(0);
                w0 = wordW;
                
                while(w0 > w)
                {
                    String word1 = trimStringToWidth(w, word);
                    list.add(word1);
                    
                    word = word.substring(word1.length());
                    w0 -= getTextWidth(word1);
                }
                
                line.append(word);
            }
        }
        
        if(!line.isEmpty())
        {
            list.add(line.toString());
        }
        
        return list;
    }
}
