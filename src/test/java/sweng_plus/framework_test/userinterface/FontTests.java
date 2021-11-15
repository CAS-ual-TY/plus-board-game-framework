package sweng_plus.framework_test.userinterface;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.userinterface.gui.font.FontHelper;
import sweng_plus.framework.userinterface.gui.font.FontInfo;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework_test.boardgame.EngineTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FontTests
{
    //TODO properly setup JUnit 5 with maven
    
    private static EngineTest engine;
    
    public static Font font;
    public static String chars;
    public static FontInfo info;
    public static FontRenderer fontRenderer;
    
    @BeforeAll
    public static void setup()
    {
        engine = new EngineTest(new Ludo());
        engine.pre();
        
        font = FontHelper.createFont(new File("src/main/resources/fonts/chicagoFLF.ttf"));
        chars = FontHelper.getAvailableChars((char) 0xFF);
        info = new FontInfo(font.deriveFont(32F), StandardCharsets.UTF_8.name(), chars);
        fontRenderer = new FontRenderer(info);
    }
    
    @AfterAll
    public static void destroy()
    {
        engine.post();
    }
    
    /**
     * Erstelle PNG Datei für den ChicagoFLF Font, um zu prüfen, ob das Einlesen und Character-Parsing korrekt geklappt hat.
     */
    //@Test
    public static void createFontPNG() throws IOException
    {
        assertTrue(createFontFile(font, 64, chars).exists());
        assertTrue(createFontFile(font, 48, chars).exists());
        assertTrue(createFontFile(font, 32, chars).exists());
        assertTrue(createFontFile(font, 24, chars).exists());
        assertTrue(createFontFile(font, 16, chars).exists());
    }
    
    private static File createFontFile(Font font, int size, String chars) throws IOException
    {
        File file = new File("src/test/resources/fonts/chicagoFLF_" + size + ".png");
        if(file.exists())
        {
            file.delete();
        }
        ImageIO.write(new FontInfo(font.deriveFont((float) size), StandardCharsets.UTF_8.name(), chars).getImage(), "png", file);
        return file;
    }
    
    @Test
    public void testTextSplitting()
    {
        for(String s : fontRenderer.splitStringToWidth(200, "Das ist ein RIEEEEESEN Test! Noch einer! NOOOOOOOOOOOOOOOOOCH einer! Heh!"))
        {
            System.out.println(s);
        }
        
        System.out.println("--------");
    }
}
