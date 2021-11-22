package sweng_plus.framework_test.networking.manual_chat;

import sweng_plus.framework.boardgame.Engine;

public class NetTestMain
{
    public static void main(String[] args)
    {
        new Engine(new NetTestGame()).run();
    }
}
