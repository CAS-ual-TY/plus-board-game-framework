package sweng_plus.framework.userinterface.gui.util;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class TextureHelper
{
    private static final HashMap<String, Texture> TEXTURE_MAP = new HashMap<>();
    
    public static Texture createTexture(String path) throws IOException
    {
        File file = new File(path);
        
        if(!file.exists() || !file.getName().toLowerCase().endsWith(".png"))
        {
            throw new IllegalArgumentException("Invalid file");
        }
        
        return createTexture(path, file);
    }
    
    public static Texture createTexture(String name, File file) throws IOException
    {
        if(TEXTURE_MAP.containsKey(name))
        {
            return TEXTURE_MAP.get(name);
        }
        
        return createTexture(name, ImageIO.read(file));
    }
    
    public static Texture createTexture(String name, BufferedImage image)
    {
        if(TEXTURE_MAP.containsKey(name))
        {
            return TEXTURE_MAP.get(name);
        }
        
        final int w = image.getWidth();
        final int h = image.getHeight();
        
        int[] pixels = new int[w * h];
        image.getRGB(0, 0, w, h, pixels, 0, w);
        ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4); //4 für RGBA, also 4 Bytes pro Farbe (RGB wäre 3 Bytes)
        
        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                int rgba = pixels[y * h + x];
                buffer.put((byte) ((rgba >> 0x10) & 0xFF)); // Rot
                buffer.put((byte) ((rgba >> 0x08) & 0xFF)); // Grün
                buffer.put((byte) ((rgba >> 0x00) & 0xFF)); // Blau
                buffer.put((byte) ((rgba >> 0x18) & 0xFF)); // Alpha
            }
        }
        
        buffer.flip();
        
        final int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        
        // Texture Skalierungs Filter, es gibt auch z.B. GL_LINEAR
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        // Wrap Mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        // Daten Binden
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        
        Texture texture = new Texture(textureID, name, w, h);
        
        TEXTURE_MAP.put(name, texture);
        
        return texture;
    }
}
