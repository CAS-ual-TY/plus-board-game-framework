package sweng_plus.framework.boardgame;

import org.lwjgl.glfw.GLFWErrorCallback;
import sweng_plus.framework.userinterface.InputHandler;
import sweng_plus.framework.userinterface.Window;
import sweng_plus.framework.userinterface.gui.Screen;

import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine implements Runnable
{
    protected static Engine instance;
    
    protected final IGame game;
    
    protected Window window;
    protected InputHandler inputHandler;
    
    public Engine(IGame game)
    {
        this.game = game;
        
        instance = this;
    }
    
    public static Engine instance()
    {
        return instance;
    }
    
    public Window getWindow()
    {
        return window;
    }
    
    public InputHandler getInputHandler()
    {
        return inputHandler;
    }
    
    @Override
    public void run()
    {
        initGLFW();
        game.preInit();
        
        createWindow();
        window.init();
        game.init();
        
        inputHandler = window.getInputHandler();
        inputHandler.setup();
        initOpenGL();
        game.postInit();
        
        loop();
        
        game.cleanup();
        inputHandler.free();
        window.destroy();
        cleanupGLFW();
    }
    
    protected void loop()
    {
        final int ticksPerSecond = game.getTicksPerSecond(); // eg. 20
        final long millisPerSecond = TimeUnit.SECONDS.toMillis(1); // 1000
        final long millisPerTick = millisPerSecond / ticksPerSecond; // 50
        final float ticksPerMillis = 1F / millisPerTick; // 1/50
        
        long currentMillis = millisPerTick;
        long lastMillis = System.currentTimeMillis();
        float deltaTick;
        
        Screen screen = null;
        
        // Render Schleife wiederholen, bis das Fenster geschlossen wird
        // Oder bis ESC gedrückt wird TODO entfernen (ist auch als TODO markiert)
        while(!isBeingClosed())
        {
            window.preUpdate();
            
            // --------- LOOP ---------
            
            currentMillis += System.currentTimeMillis() - lastMillis;
            lastMillis = System.currentTimeMillis();
            
            if(window.getWasResized() || game.getScreen() != screen)
            {
                if(game.getScreen() != screen)
                    screen = game.getScreen();
                
                glViewport(0, 0, window.getWindowW(), window.getWindowH());
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(0, window.getScreenW(), window.getScreenH(), 0, 1, -1);
                glClearColor(0, 0.7f, 1, 0);
                
                screen.init(window.getScreenW(), window.getScreenH());
            }
            
            inputHandler.inputListener(screen);
            
            if(currentMillis >= millisPerTick)
            {
                game.update();
                screen.update(inputHandler.getMouseX(), inputHandler.getMouseY());
                
                currentMillis -= millisPerTick;
            }
            
            deltaTick = (float) currentMillis / ticksPerMillis;
            
            glPushMatrix();
            
            // vielleicht zu HZ von Monitor limitieren? s. GLFWVidMode
            game.render(deltaTick);
            screen.render(deltaTick, inputHandler.getMouseX(), inputHandler.getMouseY());
            
            glPopMatrix();
            
            inputHandler.postUpdate();
            
            // --------- LOOP ---------
            
            window.postUpdate();
        }
    }
    
    protected void createWindow()
    {
        window = new Window(game.getWindowTitle(), game)
                .hint(GLFW_VISIBLE, GLFW_FALSE) // Setzt das Fenster unsichtbar
                .hint(GLFW_RESIZABLE, GLFW_TRUE); // Setzt das Fenster skalierbar
    }
    
    protected void initGLFW()
    {
        // Fehlernachrichten in System.err ausgeben
        GLFWErrorCallback.createPrint(System.err).set();
        
        // GLFW initialisieren, das meiste von GLFW funktioniert sonst nicht
        // Returnt false wenns nicht geklappt hat
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
    }
    
    protected void cleanupGLFW()
    {
        // GLFW terminieren, Fehler callback free'n
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    
    protected void initOpenGL()
    {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public boolean isBeingClosed() // Threadsafe
    {
        return window.shouldClose();
    }
    
    public void close()
    {
        window.close();
    }
}
