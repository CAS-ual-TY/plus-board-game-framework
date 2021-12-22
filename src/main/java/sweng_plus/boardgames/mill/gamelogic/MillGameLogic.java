package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.TeamColor;

public class MillGameLogic
{
    private TeamColor[] teams;
    private final boolean isHost;
    
    private MillBoard millBoard;
    private int currentTeamIndex;
    
    public MillGameLogic(TeamColor[] teams, boolean isHost) {
        this.teams = teams;
        this.isHost = isHost;
    }
    
    public void startGame()
    {
    }
    
    public TeamColor gameWon() {
        for(TeamColor team : teams)
        {
            if(millBoard.getTeamFigures(team).length < 3) {
                return team;
            }
        }
        return null;
    }
    
    public boolean isGameWon() {
        return gameWon() != null;
    }
    
    public TeamColor[] getTeams()
    {
        return teams;
    }
    
    public MillBoard getMillBoard()
    {
        return millBoard;
    }
    
    // TODO Figur auswählen
        // TODO Aufbau oder Zugphase
    // TODO Zielfeld auswählen
    // TODO auf Mühle prüfen
    // TODO gegnerische Figur zum Entfernen auswählen
        // TODO Wincondition
}
