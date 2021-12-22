package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;

public interface IMillScreen
{
    void figureSelected(int figure);
    
    void gameWon(int winningTeamIndex);
    
    //void chat(ChatMessage message);
}
