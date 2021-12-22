package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.TeamColor;

public class MillGameLogic
{
    private TeamColor[] teams;
    private final boolean isHost;
    
    public MillGameLogic(TeamColor[] teams, boolean isHost) {
        this.teams = teams;
        this.isHost = isHost;
    }
    
    public void startGame()
    {
    }
}
