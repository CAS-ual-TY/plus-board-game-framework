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
    
    @Test
    /**
     * Erstelle PNG Datei für den ChicagoFLF Font, um zu prüfen, ob das Einlesen und Character-Parsing korrekt geklappt hat.
     */
    public void createFontPNG() throws IOException
    {
        File fileChicagoFLF = new File("src/main/resources/fonts/chicagoFLF.ttf");
        File pngChicagoFLF = new File("src/test/resources/fonts/chicagoFLF.png");
        
        if(pngChicagoFLF.exists())
            pngChicagoFLF.delete();
        
        Font fontChicagoFLF = FontHelper.createFont(fileChicagoFLF, 64F);
        FontInfo fontInfoChicagoFLF = new FontInfo(fontChicagoFLF, StandardCharsets.UTF_8.name(), FontHelper.getAvailableChars((char) 0xFF));
        ImageIO.write(fontInfoChicagoFLF.getImage(), "png", pngChicagoFLF);
        
        assertTrue(pngChicagoFLF::exists);
    }
}
