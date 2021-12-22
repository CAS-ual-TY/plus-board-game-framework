package sweng_plus.framework.userinterface.gui.font;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class FontHelper
{
    public static Font createFont(InputStream in) throws IllegalArgumentException
    {
        try
        {
            return Font.createFont(Font.TRUETYPE_FONT, in);
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
    
    /**
     * Gibt einen String mit allen Characteren aus, die im angegebenen Char Set enthalten sind.
     */
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
    
    /**
     * Gibt einen String mit allen Characteren aus, für die gilt: 0 <= c <= @lastChar.
     */
    public static String getAvailableChars(char lastChar)
    {
        return getAvailableChars((char) 1, lastChar);
    }
    
    public static String getAvailableChars(char firstChar, char lastChar)
    {
        StringBuilder result = new StringBuilder();
        
        for(char c = firstChar; c <= lastChar; c++)
        {
            result.append(c);
        }
        
        return result.toString();
    }
}
