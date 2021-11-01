package sweng_plus.framework.boardgame;

import org.lwjgl.glfw.GLFWErrorCallback;
import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.userinterface.InputHandler;
import sweng_plus.framework.userinterface.Window;

import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements Runnable
{
    private static Main instance;
    
    private final IGame game;
    
    private Window window;
    private InputHandler inputHandler;
    
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
        
        // Render Schleife wiederholen, bis das Fenster geschlossen wird
        // Oder bis ESC gedrückt wird TODO entfernen (ist auch als TODO markiert)
        while(!window.shouldClose())
        {
            window.preUpdate();
            
            // --------- LOOP ---------
            
            currentMillis += System.currentTimeMillis() - lastMillis;
            lastMillis = System.currentTimeMillis();
            
            inputHandler.inputScreen(game.getScreen());
            
            if(currentMillis >= millisPerTick)
            {
                game.update();
                game.getScreen().update(inputHandler.getMouseX(), inputHandler.getMouseY());
                
                currentMillis -= millisPerTick;
            }
            
            deltaTick = (float) currentMillis / ticksPerMillis;
            
            // vielleicht zu HZ von Monitor limitieren? s. GLFWVidMode
            game.render(deltaTick);
            game.getScreen().render(deltaTick, inputHandler.getMouseX(), inputHandler.getMouseY());
            
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
    
    public static void main(String... args)
    {
        new Main(new Ludo()).run();
    }
}
