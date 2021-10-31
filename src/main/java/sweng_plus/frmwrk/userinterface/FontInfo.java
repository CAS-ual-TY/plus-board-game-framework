package sweng_plus.frmwrk.userinterface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

public class FontInfo
{
    private final Font font;
    private final String charSet;
    
    private String availableCharacters;
    private final Map<Character, CharInfo> charMap;
    
    private int width;
    private int height;
    
    private BufferedImage image;
    
    public FontInfo(Font font, String charSet, String availableCharacters)
    {
        this.font = font;
        this.charSet = charSet;
        charMap = new HashMap<>();
        this.availableCharacters = availableCharacters;
        
        setup();
    }
    
    public FontInfo(Font font, String charSet)
    {
        this(font, charSet, getAvailableChars(charSet));
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public BufferedImage getImage()
    {
        return image;
    }
    
    public CharInfo getCharInfo(char c)
    {
        return charMap.get(c);
    }
    
    private void setup()
    {
        // Graphics2D erstellen, um FontMetrics zu erhalten und dadurch Charactere auszumessen
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();
        
        width = 0;
        height = fontMetrics.getHeight();
        
        int charWidth;
        
        for(char c : availableCharacters.toCharArray())
        {
            charWidth = fontMetrics.charWidth(c);
            
            if(charWidth == 0)
            {
                System.out.println("Font " + font.getName() + ": Char width of char '" + (c >= '!' ? c : "\\0x" + Integer.toHexString((int)c).toUpperCase()) + "' is 0");
            }
            
            // Get the size for each character and update global image size
            CharInfo charInfo = new CharInfo(width, charWidth);
            charMap.put(c, charInfo);
            width += charWidth;
        }
        
        g2D.dispose();
        
        // Create the image associated to the charset
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        int startX = 0;
        for(char c : availableCharacters.toCharArray())
        {
            CharInfo charInfo = charMap.get(c);
            g2D.drawString(String.valueOf(c), startX, fontMetrics.getAscent());
            startX += charInfo.getCharW();
        }
        g2D.dispose();
        
        image = img;
    }
    
    public static Font createFont(File file, float size) throws IllegalArgumentException
    {
        if(!file.getName().endsWith(".ttf"))
        {
            throw new IllegalArgumentException("Font must be truetype and file must end with \".ttf\"");
        }
    
        try
        {
            return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(size);
        }
        catch(IOException | NullPointerException | SecurityException e)
        {
            throw new IllegalArgumentException("Exception with font file", e);
        }
        catch(FontFormatException | IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Exception with font format", e);
        }
    }
    
    public static String getAvailableChars(String charSet)
    {
        CharsetEncoder ce = Charset.forName(charSet).newEncoder();
        StringBuilder result = new StringBuilder();
        
        for(char c = 0; c < Character.MAX_VALUE; c++)
        {
            if(ce.canEncode(c))
            {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    public static String getAvailableChars(char lastChar)
    {
        StringBuilder result = new StringBuilder();
        
        for(char c = 0; c <= lastChar; c++)
        {
            result.append(c);
        }
        
        return result.toString();
    }
    
    public static class CharInfo
    {
        private final int charX;
        private final int charW;
        
        public CharInfo(int charX, int charW)
        {
            this.charX = charX;
            this.charW = charW;
        }
        
        public int getCharX()
        {
            return charX;
        }
        
        public int getCharW()
        {
            return charW;
        }
    }
}