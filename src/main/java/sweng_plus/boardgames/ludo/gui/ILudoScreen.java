package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;

public interface ILudoScreen
{
    void diceResult(int dice);
    
    void figureSelected(int figure);
    
    void gameWon(String winnerName);
    
    void chat(ChatMessage message);
}
