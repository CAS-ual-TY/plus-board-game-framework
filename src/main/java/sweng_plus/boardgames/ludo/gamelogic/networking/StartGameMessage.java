package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;

public record StartGameMessage()
{
    public static void handleMessage()
    {
        Ludo.instance().startGame();
    }
}
