package sweng_plus.framework.boardgame;

import org.lwjgl.glfw.GLFWErrorCallback;
import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.userinterface.InputHandler;
import sweng_plus.framework.userinterface.Window;
import sweng_plus.framework.userinterface.gui.Screen;

import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;

public class Engine implements Runnable
{
    private static Engine instance;
    
    private final IGame game;
    
    private Window window;
    private InputHandler inputHandler;
    
    public Engine(IGame game)
    {
        this.game = game;
        
        instance = this;
    }
    
    public Engine instance()
    {
        return instance;
    }
    
    public IGame getGame()
    {
        return game;
    }
    
    public Window getWindow()
    {
        return window;
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
        game.postInit();
        
        loop();
        
        game.cleanup();
        inputHandler.free();
        window.destroy();
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
        
        Screen screen = null;
        
        // Render Schleife wiederholen, bis das Fenster geschlossen wird
        // Oder bis ESC gedrückt wird TODO entfernen (ist auch als TODO markiert)
        while(!window.shouldClose())
        {
            window.preUpdate();
            
            // --------- LOOP ---------
            
            currentMillis += System.currentTimeMillis() - lastMillis;
            lastMillis = System.currentTimeMillis();
            
            if(window.getWasResized() || game.getScreen() != screen)
            {
                if(game.getScreen() != screen)
                    screen = game.getScreen();
                
                screen.init(window.getScreenW(), window.getScreenH());
            }
            
            inputHandler.inputScreen(screen);
            
            if(currentMillis >= millisPerTick)
            {
                game.update();
                screen.update(inputHandler.getMouseX(), inputHandler.getMouseY());
                
                currentMillis -= millisPerTick;
            }
            
            deltaTick = (float) currentMillis / ticksPerMillis;
            
            // vielleicht zu HZ von Monitor limitieren? s. GLFWVidMode
            game.render(deltaTick);
            screen.render(deltaTick, inputHandler.getMouseX(), inputHandler.getMouseY());
            
            inputHandler.postUpdate();
            
            // --------- LOOP ---------
            
            window.postUpdate();
        }
    }
    
    private void createWindow()
    {
        window = new Window(game.getWindowTitle(), game)
                .hint(GLFW_VISIBLE, GLFW_FALSE) // Setzt das Fenster unsichtbar
                .hint(GLFW_RESIZABLE, GLFW_TRUE); // Setzt das Fenster skalierbar
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
    
    private void cleanupGLFW()
    {
        // GLFW terminieren, Fehler callback free'n
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
