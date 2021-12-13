package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.messages.KickClientMessage;
import sweng_plus.framework.networking.messages.LeaveServerMessage;

public interface IAdvancedMessageRegistry<C extends IClient> extends IMessageRegistry<C>
{
    <M> M requestPing();
    
    <M> M respondToPing();
    
    <M> M forceDisconnectClient();
    
    <M> M serverClosed();
    
    <M> M kickClient();
    
    <M> M kickClient(String message);
    
    <M> M orderlyDisconnected();
    
    <M> M disconnectedDueToException();
}
