package sweng_plus.framework.userinterface.gui.font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class FontHelper
{
    /**
     * Erstellt eine {@link Font} Instanz von der angegebenen Datei @file in der angegebenen Größe @size
     *
     * @param file Font Datei, muss auf ".ttf" enden und ein True Type Font sein
     * @return {@link Font} entsprechend nach Parametern
     * @throws IllegalArgumentException Wenn etwas mit der angegebenen Font Datei oder deren Format nicht stimmt
     */
    public static Font createFont(File file) throws IllegalArgumentException
    {
        if(!file.getName().endsWith(".ttf"))
        {
            throw new IllegalArgumentException("Font must be truetype and file must end with \".ttf\"");
        }
        
        try
        {
            return Font.createFont(Font.TRUETYPE_FONT, file);
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
