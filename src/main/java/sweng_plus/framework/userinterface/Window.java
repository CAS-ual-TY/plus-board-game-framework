package sweng_plus.framework.userinterface;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import sweng_plus.framework.boardgame.EngineUtil;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.input.InputHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Window
{
    private final String title;
    private final String iconResource;
    private final IScreenHolder screenHolder;
    
    private long windowHandle;
    private final InputHandler inputHandler;
    
    private final WindowScale windowScale;
    
    private int windowW;
    private int windowH;
    
    private float scaleFactor;
    private int screenW;
    private int screenH;
    
    private boolean wasResized;
    
    public Window(String title, String iconResource, IScreenHolder screenHolder, int windowW, int windowH, WindowScale windowScale)
    {
        this.title = title;
        this.iconResource = iconResource;
        this.screenHolder = screenHolder;
        
        windowHandle = -1;
        inputHandler = createInputHandler();
        
        this.windowScale = windowScale;
        
        windowResized(windowW, windowH);
        
        wasResized = true;
        
        glfwDefaultWindowHints(); // "Window Hints" = z.B. Fenster skalierbar? Fenster sichtbar? etc.
    }
    
    public Window(String title, String iconResource, IScreenHolder screenHolder)
    {
        this(title, iconResource, screenHolder, 640, 360, WindowScale.DEFAULT_WINDOW_SCALE_16_9);
    }
    
    public Window hint(int hint, int value)
    {
        glfwWindowHint(hint, value);
        return this;
    }
    
    public long getWindowHandle()
    {
        return windowHandle;
    }
    
    public InputHandler getInputHandler()
    {
        return inputHandler;
    }
    
    public int getWindowW()
    {
        return windowW;
    }
    
    public int getWindowH()
    {
        return windowH;
    }
    
    public float getScaleFactor()
    {
        return scaleFactor;
    }
    
    public int getScreenW()
    {
        return screenW;
    }
    
    public int getScreenH()
    {
        return screenH;
    }
    
    public boolean getWasResized()
    {
        return wasResized;
    }
    
    public void init()
    {
        // Fenster erstellen
        windowHandle = glfwCreateWindow(windowW, windowH, title, MemoryUtil.NULL, MemoryUtil.NULL);
        
        // Aufl??sung des Hauptmonitors
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        
        //noinspection ConstantConditions
        int monitorW = vidmode.width();
        int monitorH = vidmode.height();
        
        glfwSetWindowSizeLimits(windowHandle, 640, 360, GLFW_DONT_CARE, GLFW_DONT_CARE);
        
        if(windowHandle == MemoryUtil.NULL)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        // Wir setzen das Fenster hier in die Mitte
        // Beispiel, wie wir uns Daten von GLFW holen
        try(MemoryStack stack = stackPush())
        {
            // Diese brauchen wir, um uns Daten von GLFW zu holen
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            
            // Wir bekommen die Fenstergr????e in den Buffern (ist die selbe wie oben, bei glfwCreateWindow)
            glfwGetWindowSize(windowHandle, pWidth, pHeight);
            
            // Fenster Zentrieren
            glfwSetWindowPos(
                    windowHandle,
                    (monitorW - pWidth.get(0)) / 2,
                    (monitorH - pHeight.get(0)) / 2
            );
        } // Oben war Push, Pop passier automatisch dank try und close()
        
        // Das jetzt erstellte Fenster wird nun der Kontext f??r OpenGL
        glfwMakeContextCurrent(windowHandle);
        
        // Enable v-sync
        glfwSwapInterval(1);
        
        // Fenster sichtbar machen
        glfwShowWindow(windowHandle);
        
        initIcon();
        
        // s. http://forum.lwjgl.org/index.php?topic=6858.0 und http://forum.lwjgl.org/index.php?topic=6459.0
        // OpenGL auf gerade gesetzten Kontext ausrichten
        GL.createCapabilities();
        
        //glfwSetFramebufferSizeCallback() // F??r Fenster Skalierungen
        glfwSetFramebufferSizeCallback(windowHandle, (window, windowW, windowH) -> windowResized(windowW, windowH));
    }
    
    private void initIcon()
    {
        ByteBuffer buffer;
        int width, height;
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            
            InputStream in = EngineUtil.getResourceInputStream(iconResource);
            byte[] bytes = in.readAllBytes();
            buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            
            buffer = stbi_load_from_memory(buffer, w, h, comp, 4);
            
            if(buffer == null)
            {
                System.err.println(stbi_failure_reason());
                return;
            }
            
            width = w.get();
            height = h.get();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }
        
        GLFWImage image = GLFWImage.malloc();
        GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
        image.set(width, height, buffer);
        imageBuffer.put(0, image);
        glfwSetWindowIcon(windowHandle, imageBuffer);
    }
    
    private void windowResized(int windowW, int windowH)
    {
        this.windowW = windowW;
        this.windowH = windowH;
        
        WindowScale.SingleScale scale = windowScale.getScaleForWindowSize(windowW, windowH);
        scaleFactor = scale.scaleFactor;
        
        screenW = (int) (windowW * scaleFactor);
        screenH = (int) (windowH * scaleFactor);
        wasResized = true;
    }
    
    public void preUpdate()
    {
        // FrameBuffer leeren
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void postUpdate()
    {
        wasResized = false;
        
        // ColorBuffers tauschen
        glfwSwapBuffers(windowHandle);
        
        // Fenster Events ausf??hren, z.B. alle InputCallbacks
        glfwPollEvents();
    }
    
    public void destroy()
    {
        glfwDestroyWindow(windowHandle);
    }
    
    public boolean shouldClose() // Threadsafe
    {
        return glfwWindowShouldClose(windowHandle);
    }
    
    public void close() // synchronized?
    {
        glfwSetWindowShouldClose(windowHandle, true);
    }
    
    protected InputHandler createInputHandler()
    {
        return new InputHandler(this);
    }
}
