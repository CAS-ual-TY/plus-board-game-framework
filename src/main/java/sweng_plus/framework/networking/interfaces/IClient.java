package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.NetworkRole;

public interface IClient
{
    String getIP();
    
    NetworkRole getRole();
    
    ClientStatus getStatus();
    
    void changeStatus(ClientStatus status);
}
