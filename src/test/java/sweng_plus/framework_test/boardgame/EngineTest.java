package sweng_plus.framework_test.boardgame;

import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.IGame;

public class EngineTest extends Engine
{
    public EngineTest(IGame game)
    {
        super(game);
    }
    
    public void pre()
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
    }
    
    public void post()
    {
        game.cleanup();
        inputHandler.free();
        window.destroy();
        cleanupGLFW();
    }
    
    @Override
    public boolean isBeingClosed()
    {
        return true;
    }
}
