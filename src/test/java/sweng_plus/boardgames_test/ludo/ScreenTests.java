package sweng_plus.boardgames_test.ludo;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.boardgame.Engine;

import java.io.IOException;

public class ScreenTests
{
    public static void main(String[] args) throws IOException
    {
        Ludo l = new Ludo()
        {
            @Override
            public void init()
            {
                super.init();
                try
                {
                    setScreen(new BoardGameDebugScreen(this));
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        
        new Engine(l).run();
    }
}
