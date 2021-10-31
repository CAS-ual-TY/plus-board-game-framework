package sweng_plus.framework.userinterface;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Window
{
    private final String title;
    
    private long windowHandle;
    private InputHandler inputHandler;
    
    public Window(String title)
    {
        this.title = title;
        glfwDefaultWindowHints(); // "Window Hints" = z.B. Fenster skalierbar? Fenster sichtbar? etc.
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
    
    public void init()
    {
        // Fenster erstellen
        windowHandle = glfwCreateWindow(300, 300, title, MemoryUtil.NULL, MemoryUtil.NULL);
        
        if(windowHandle == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        
        // Wir setzen das Fenster hier in die Mitte
        // Beispiel, wie wir uns Daten von GLFW holen
        try(MemoryStack stack = stackPush())
        {
            // Diese brauchen wir, um uns Daten von GLFW zu holen
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            
            // Wir bekommen die Fenstergröße in den Buffern (ist die selbe wie oben, bei glfwCreateWindow)
            glfwGetWindowSize(windowHandle, pWidth, pHeight);
            
            // Auflösung des Hauptmonitors
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            
            // Fenster Zentieren
            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // Oben war Push, Pop passier automatisch dank try und close()
        
        // Das jetzt erstellte Fenster wird nun der Kontext für OpenGL
        glfwMakeContextCurrent(windowHandle);
        
        // Enable v-sync
        glfwSwapInterval(1);
        
        // Fenster sichtbar machen
        glfwShowWindow(windowHandle);
        
        // s. http://forum.lwjgl.org/index.php?topic=6858.0 und http://forum.lwjgl.org/index.php?topic=6459.0
        // OpenGL auf gerade gesetzten Kontext ausrichten
        GL.createCapabilities();
    }
    
    public void preUpdate()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
    
    public void postUpdate()
    {
        glfwSwapBuffers(windowHandle); // swap the color buffers
        
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }
    
    public void destroy()
    {
        glfwDestroyWindow(windowHandle);
    }
    
    public boolean shouldClose()
    {
        return glfwWindowShouldClose(windowHandle);
    }
    
    public InputHandler createInputHandler()
    {
        return this.inputHandler = new InputHandler(this);
    }
}
