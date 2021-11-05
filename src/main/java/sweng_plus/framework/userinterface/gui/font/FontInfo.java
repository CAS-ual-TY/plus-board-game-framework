package sweng_plus.framework.userinterface.gui.font;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class FontInfo
{
    private final Font font;
    private final String charSet;
    
    private final String availableCharacters;
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
        this(font, charSet, FontHelper.getAvailableChars(charSet));
    }
    
    public Font getFont()
    {
        return font;
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
        
        // Breite jedes einzelnen characters herausfinden und speichern
        for(char c : availableCharacters.toCharArray())
        {
            charWidth = fontMetrics.charWidth(c);
            
            if(charWidth == 0)
            {
                System.out.println("Font " + font.getName() + ": Char width of char '" + (c >= '!' ? c : "\\0x" + Integer.toHexString(c).toUpperCase()) + "' is 0");
            }
            
            CharInfo charInfo = new CharInfo(width, charWidth);
            charMap.put(c, charInfo);
            width += charWidth;
        }
        g2D.dispose();
        
        // Nun eigentliche Bilddatei erstellen mit allen Characteren von links nach rechts
        // Diese Datei wird nur virtuell im Arbeitsspeicher erstellt, nicht auf dem Festspeicher
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        
        // Alle Charactere nun auf die Bilddatei "malen"
        int x = 0;
        for(char c : availableCharacters.toCharArray())
        {
            CharInfo charInfo = charMap.get(c);
            g2D.drawString(String.valueOf(c), x, fontMetrics.getAscent());
            x += charInfo.getCharW();
        }
        g2D.dispose();
        
        // Bilddatei als Feld abspeichern für spätere Benutzung
        image = img;
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