package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.NetworkRole;

public class LudoClient extends Client
{
    public int teamIndex;
    
    public LudoClient(String ip, NetworkRole role, ClientStatus status)
    {
        super(ip, role, status);
        teamIndex = -1;
    }
    
    public void setTeamIndex(int teamIndex)
    {
        this.teamIndex = teamIndex;
    }
    
    public int getTeamIndex()
    {
        return teamIndex;
    }
    
    public boolean isPlayer()
    {
        return teamIndex != -1;
    }
}
