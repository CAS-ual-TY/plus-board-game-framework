package sweng_plus.framework_test.boardgame;

import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.IGame;

public class EngineTest extends Engine
{
    public EngineTest(IGame game)
    {
        super(game);
    }
    
    @Override
    public void pre()
    {
        super.pre();
    }
    
    @Override
    public void post()
    {
        super.post();
    }
    
    @Override
    public void loop()
    {
        super.loop();
    }
    
    @Override
    public boolean isBeingClosed()
    {
        return true;
    }
}
