package sweng_plus.boardgames.mill.gui;

public interface IMillScreen
{
    void figureNodeSelected(int figure, int node);
    
    void figureTaken(int figure);
    
    void gameWon(String winnerName);
}
