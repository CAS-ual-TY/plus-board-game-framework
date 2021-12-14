package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;

public interface ILudoScreen
{
    void diceResult(int dice);
    
    void figureSelected(int figure);
    
    void gameWon(int winningTeamIndex);
    
    void chat(ChatMessage message);
}
