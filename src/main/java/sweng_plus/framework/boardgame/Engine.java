package sweng_plus.framework.boardgame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.lwjgl.glfw.GLFWErrorCallback;
import sweng_plus.framework.userinterface.Window;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.input.InputHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine implements Runnable
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    public static final float FULL_DELTA_TICK = 0.0F;
    
    protected static Engine instance;
    
    protected final IGame game;
    
    protected Window window;
    protected InputHandler inputHandler;
    
    public Engine(IGame game)
    {
        this.game = game;
        
        //noinspection ThisEscapedInObjectConstruction
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
        pre();
        loop();
        post();
    }
    
    protected void pre()
    {
        game.preInit();
        
        initGLFW();
        createWindow();
        window.init();
        inputHandler = window.getInputHandler();
        inputHandler.setup();
        initOpenGL();
        initI18n();
        
        game.init();
    }
    
    protected void post()
    {
        game.cleanup();
        
        inputHandler.free();
        window.destroy();
        cleanupGLFW();
        
        game.postCleanup();
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
        while(!isBeingClosed())
        {
            window.preUpdate();
            
            // --------- LOOP ---------
            
            currentMillis += System.currentTimeMillis() - lastMillis;
            lastMillis = System.currentTimeMillis();
            
            if(window.getWasResized() || game.getScreen() != screen)
            {
                if(game.getScreen() != screen)
                {
                    screen = game.getScreen();
                }
                
                glViewport(0, 0, window.getWindowW(), window.getWindowH());
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(0, window.getScreenW(), window.getScreenH(), 0, 1, -1);
                
                screen.initScreen(window.getScreenW(), window.getScreenH());
            }
            
            if(currentMillis >= millisPerTick)
            {
                inputHandler.inputListener(game.getScreen());
                game.getInputListeners().forEach(inputHandler::inputListener);
                inputHandler.postUpdate();
                
                game.update();
                screen.update(inputHandler.getMouseX(), inputHandler.getMouseY());
                
                currentMillis %= millisPerTick;
            }
            
            deltaTick = (float) currentMillis / millisPerTick;
            
            glPushMatrix();
            
            // vielleicht zu HZ von Monitor limitieren? s. GLFWVidMode
            game.render(deltaTick);
            screen.render(deltaTick, inputHandler.getMouseX(), inputHandler.getMouseY());
            
            glPopMatrix();
            
            // --------- LOOP ---------
            
            window.postUpdate();
        }
    }
    
    protected void createWindow()
    {
        window = new Window(game.getWindowTitle(), game.getWindowIconResource(), game)
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
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    }
    
    protected void cleanupGLFW()
    {
        // GLFW terminieren, Fehler callback free'n
        glfwTerminate();
        //noinspection ConstantConditions
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
    
    protected void initI18n()
    {
        try
        {
            I18n.initializeI18N(game.getDefaultLocale());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
