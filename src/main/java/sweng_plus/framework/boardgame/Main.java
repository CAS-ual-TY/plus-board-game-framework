package sweng_plus.framework.boardgame;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import sweng_plus.boardgames.ludo.Ludo;

import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Main implements Runnable
{
    private static Main instance;
    
    private final IGame game;
    
    private long window;
    
    public Main(IGame game)
    {
        this.game = game;
        
        instance = this;
    }
    
    public Main instance()
    {
        return instance;
    }
    
    public IGame getGame()
    {
        return game;
    }
    
    public long getGLFWWindow()
    {
        return window;
    }
    
    @Override
    public void run()
    {
        initGLFW();
        game.preInit();
        
        initWindow();
        game.init();
        
        setupInputCallbacks();
        game.postInit();
        
        loop();
        
        game.cleanup();
        freeInputCallbacks();
        destroyWindow();
        cleanupGLFW();
    }
    
    private void loop()
    {
        final int ticksPerSecond = game.getTicksPerSecond(); // eg. 20
        final long millisPerSecond = TimeUnit.SECONDS.toMillis(1); // 1000
        final long millisPerTick = millisPerSecond / ticksPerSecond; // 50
        final float ticksPerMillis = 1F / millisPerTick; // 1/50
        
        long currentMillis = millisPerTick;
        long lastMillis = System.currentTimeMillis();
        float deltaTick;
        
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while(!glfwWindowShouldClose(window))
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            
            // --------- LOOP ---------
            
            currentMillis += System.currentTimeMillis() - lastMillis;
            lastMillis = System.currentTimeMillis();
            deltaTick = (float) currentMillis / ticksPerMillis;
            
            if(currentMillis >= millisPerTick)
            {
                game.update();
                currentMillis -= millisPerTick;
            }
            
            // vielleicht zu HZ von Monitor limitieren? s. GLFWVidMode
            game.render(deltaTick);
            
            // --------- LOOP ---------
            
            glfwSwapBuffers(window); // swap the color buffers
            
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
    
    private void initGLFW()
    {
        // Fehlernachrichten in System.err ausgeben
        GLFWErrorCallback.createPrint(System.err).set();
        
        // GLFW initialisieren, das meiste von GLFW funktioniert sonst nicht
        // Returnt false wenns nicht geklappt hat
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
    }
    
    private void initWindow()
    {
        // Konfiguration
        glfwDefaultWindowHints(); // "Window Hints" = z.B. Fenster skalierbar? Fenster sichtbar? etc.
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Setzt das Fenster unsichtbar
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Setzt das Fenster skalierbar
        
        // Fenster erstellen
        window = glfwCreateWindow(300, 300, game.getWindowTitle(), MemoryUtil.NULL, MemoryUtil.NULL);
        
        if(window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        
        // Wir setzen das Fenster hier in die Mitte
        // Beispiel, wie wir uns Daten von GLFW holen
        try(MemoryStack stack = stackPush())
        {
            // Diese brauchen wir, um uns Daten von GLFW zu holen
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            
            // Wir bekommen die Fenstergröße in den Buffern (ist die selbe wie oben, bei glfwCreateWindow)
            glfwGetWindowSize(window, pWidth, pHeight);
            
            // Auflösung des Hauptmonitors
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            
            // Fenster Zentieren
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // Oben war Push, Pop passier automatisch dank try und close()
        
        // Das jetzt erstellte Fenster wird nun der Kontext für OpenGL
        glfwMakeContextCurrent(window);
        
        // Enable v-sync
        glfwSwapInterval(1);
        
        // Fenster sichtbar machen
        glfwShowWindow(window);
        
        // s. http://forum.lwjgl.org/index.php?topic=6858.0 und http://forum.lwjgl.org/index.php?topic=6459.0
        // OpenGL auf gerade gesetzten Kontext ausrichten
        GL.createCapabilities();
    }
    
    private void setupInputCallbacks()
    {
        // Callback für Tasten; wird bei jedem drücken, wiederholtem drücken oder loslassen gecallt
        // TODO Input Handler
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // Aktuell: Fenster schliessen bei ESC
        });
        
        // TODO Input Handler
        //glfwSetMouseButtonCallback()
        //glfwSetCursorEnterCallback()
        //glfwSetScrollCallback()
        //glfwSetCursorPosCallback()
        //glfwSetFramebufferSizeCallback() // Für Fenster skalierungen
    }
    
    private void freeInputCallbacks()
    {
        glfwFreeCallbacks(window);
    }
    
    private void destroyWindow()
    {
        glfwDestroyWindow(window);
    }
    
    private void cleanupGLFW()
    {
        // GLFW terminieren, Fehler callback free'n
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    
    public static void main(String... args)
    {
        new Main(new Ludo()).run();
    }
}
