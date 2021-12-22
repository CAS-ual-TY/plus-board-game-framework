package sweng_plus.boardgames.mill.networking;

import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.NetworkRole;

public class MillClient extends Client
{
    public String name;
    public int teamIndex;
    
    public MillClient(String ip, NetworkRole role, ClientStatus status)
    {
        super(ip, role, status);
        name = "";
        teamIndex = -1;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
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
