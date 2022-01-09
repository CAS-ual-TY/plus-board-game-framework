package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;

public interface IMillScreen
{
    void figureNodeSelected(int figure, int node);
    
    void gameWon(int winningTeamIndex);
    
    //void chat(ChatMessage message);
}
