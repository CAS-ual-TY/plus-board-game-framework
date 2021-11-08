package sweng_plus.framework_test.userinterface;

import org.junit.jupiter.api.Test;
import sweng_plus.framework.userinterface.gui.font.FontHelper;
import sweng_plus.framework.userinterface.gui.font.FontInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FontTests
{
    //TODO properly setup JUnit 5 with maven
    
    /**
     * Erstelle PNG Datei für den ChicagoFLF Font, um zu prüfen, ob das Einlesen und Character-Parsing korrekt geklappt hat.
     */
    @Test
    public void createFontPNG() throws IOException
    {
        Font font = FontHelper.createFont(new File("src/main/resources/fonts/chicagoFLF.ttf"));
        String chars = FontHelper.getAvailableChars((char) 0xFF);
    
        assertTrue(createFontFile(font, 64, chars).exists());
        assertTrue(createFontFile(font, 48, chars).exists());
        assertTrue(createFontFile(font, 32, chars).exists());
        assertTrue(createFontFile(font, 24, chars).exists());
        assertTrue(createFontFile(font, 16, chars).exists());
    }
    
    private File createFontFile(Font font, int size, String chars) throws IOException
    {
        File file = new File("src/test/resources/fonts/chicagoFLF_" + size + ".png");
        if(file.exists())
        {
            file.delete();
        }
        ImageIO.write(new FontInfo(font.deriveFont((float)size), StandardCharsets.UTF_8.name(), chars).getImage(), "png", file);
        return file;
    }
}
