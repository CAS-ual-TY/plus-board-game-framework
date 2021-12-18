package sweng_plus.framework.userinterface.gui.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import sweng_plus.framework.boardgame.EngineUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class TextureHelper
{
    private static final HashMap<String, Texture> TEXTURE_MAP = new HashMap<>();
    
    public static Texture createTexture(String path) throws IOException
    {
        URL url = EngineUtil.getResourceURL(path);
        
        if(!url.getFile().toLowerCase().endsWith(".png"))
        {
            throw new IllegalArgumentException("Invalid path: " + path + " (" + url.getFile() + ")");
        }
        
        return createTexture(path, url);
    }
    
    public static Texture createTexture(String name, URL path) throws IOException
    {
        if(TEXTURE_MAP.containsKey(name))
        {
            return TEXTURE_MAP.get(name);
        }
        
        return createTexture(name, ImageIO.read(path));
    }
    
    @SuppressWarnings("PointlessBitwiseExpression")
    public static Texture createTexture(String name, BufferedImage image)
    {
        if(TEXTURE_MAP.containsKey(name))
        {
            return TEXTURE_MAP.get(name);
        }
        
        final int w = image.getWidth();
        final int h = image.getHeight();
    
        Texture texture = imageToByteBuffer(image, buffer ->
        {
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
    
            return new Texture(textureID, name, w, h);
        });
    
        TEXTURE_MAP.put(name, texture);
    
        return texture;
    }
    
    public static Texture imageToByteBuffer(BufferedImage image, Function<ByteBuffer, Texture> consumer)
    {
        final int w = image.getWidth();
        final int h = image.getHeight();
    
        int[] pixels = new int[w * h];
        image.getRGB(0, 0, w, h, pixels, 0, w);
    
        ByteBuffer buffer = ByteBuffer.allocateDirect(w * h * 4);
    
        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                int rgba = pixels[y * w + x];
                buffer.put((byte) ((rgba >> 0x10) & 0xFF)); // Rot
                buffer.put((byte) ((rgba >> 0x08) & 0xFF)); // Grün
                buffer.put((byte) ((rgba >> 0x00) & 0xFF)); // Blau
                buffer.put((byte) ((rgba >> 0x18) & 0xFF)); // Alpha
            }
        }
    
        buffer.flip();
            
        return consumer.apply(buffer);
    }
}
