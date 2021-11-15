package sweng_plus.framework.networking;

public interface IClient
{
    String getIP();
    
    NetworkRole getRole();
    
    ClientStatus getStatus();
}
