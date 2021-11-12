package sweng_plus.framework.networking;

public interface IClient
{
    String getName();
    
    <M> void sendMessage(M message);
}
