package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.*;
import sweng_plus.framework.networking.messages.AuthMessage;
import sweng_plus.framework.networking.messages.KickClientMessage;
import sweng_plus.framework.networking.messages.LeaveServerMessage;
import sweng_plus.framework.networking.messages.PingMessage;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class AdvancedMessageRegistry<C extends IAdvancedClient> extends MessageRegistry<C> implements IAdvancedMessageRegistry<C>
{
    protected Supplier<IAdvancedClientManager> clientManager;
    protected Supplier<IAdvancedHostManager<C>> hostManager;
    
    protected IAdvancedClientEventsListener clientEventsListener;
    protected IAdvancedHostEventsListener<C> hostEventsListener;
    
    public AdvancedMessageRegistry(int messages, byte pingID, byte kickID, byte leaveID, byte authID,
                                   Supplier<IAdvancedClientManager> clientManager, Supplier<IAdvancedHostManager<C>> hostManager,
                                   IAdvancedClientEventsListener clientEventsListener,
                                   IAdvancedHostEventsListener<C> hostEventsListener)
    {
        super(messages);
        this.clientManager = clientManager;
        this.hostManager = hostManager;
        this.clientEventsListener = clientEventsListener;
        this.hostEventsListener = hostEventsListener;
        registerMessages(pingID, kickID, leaveID, authID);
    }
    
    protected void registerMessages(byte pingID, byte kickID, byte leaveID, byte authID)
    {
        registerPingMessage(pingID);
        registerKickClientMessage(kickID);
        registerLeaveServerMessage(leaveID);
        registerAuthMessage(authID);
    }
    
    protected AdvancedMessageRegistry<C> registerPingMessage(byte id)
    {
        registerMessage(id, PingMessage.Handler::encodeMessage, PingMessage.Handler::decodeMessage,
                (clientOptional, message) ->
                {
                    if(message.code() == PingMessage.REQUEST)
                    {
                        clientOptional.ifPresentOrElse(
                                (client) -> hostManager.get().sendMessageToClient(client, respondToPing()),
                                () -> clientManager.get().sendMessageToServer(respondToPing()));
                    }
                }, PingMessage.class);
        return this;
    }
    
    protected AdvancedMessageRegistry<C> registerKickClientMessage(byte id)
    {
        registerMessage(id, KickClientMessage.Handler::encodeMessage, KickClientMessage.Handler::decodeMessage,
                (clientOptional, message) ->
                        clientOptional.ifPresentOrElse((client) -> {}, () ->
                        {
                            IClientManager clientManager1 = clientManager.get();
                            
                            if(message.code() == KickClientMessage.UNKNOWN)
                            {
                                clientManager1.close();
                                clientManager1.runOnMainThreadSafely(clientEventsListener::forcedDisconnected);
                            }
                            else if(message.code() == KickClientMessage.SERVER_CLOSED)
                            {
                                clientManager1.close();
                                clientManager1.runOnMainThreadSafely(clientEventsListener::serverClosed);
                            }
                            else if(message.code() == KickClientMessage.CLIENT_KICKED)
                            {
                                clientManager1.close();
                                clientManager1.runOnMainThreadSafely(clientEventsListener::kickedFromServer);
                            }
                            else if(message.code() == KickClientMessage.CLIENT_KICKED_MESSAGE)
                            {
                                clientManager1.close();
                                clientManager1.runOnMainThreadSafely(() -> clientEventsListener.kickedFromServerWithMessage(message.message()));
                            }
                        }), KickClientMessage.class);
        return this;
    }
    
    protected AdvancedMessageRegistry<C> registerLeaveServerMessage(byte id)
    {
        registerMessage(id, LeaveServerMessage.Handler::encodeMessage, LeaveServerMessage.Handler::decodeMessage,
                (clientOptional, message) ->
                        clientOptional.ifPresent(client ->
                        {
                            IHostManager<C> hostManager1 = hostManager.get();
                            
                            if(message.code() == LeaveServerMessage.ORDERLY_DISCONNECTED)
                            {
                                hostManager1.closeClient(client);
                                hostManager1.runOnMainThreadSafely(
                                        () -> hostEventsListener.clientDisconnectedOrderly(client));
                            }
                            else if(message.code() == LeaveServerMessage.DISCONNECTED_DUE_TO_EXCEPTION)
                            {
                                hostManager1.closeClient(client);
                                hostManager1.runOnMainThreadSafely(
                                        () -> hostEventsListener.clientDisconnectedDueToException(client));
                            }
                        }), LeaveServerMessage.class);
        return this;
    }
    
    protected AdvancedMessageRegistry<C> registerAuthMessage(byte id)
    {
        registerMessage(id, AuthMessage.Handler::encodeMessage, AuthMessage.Handler::decodeMessage,
                (clientOptional, message) ->
                        clientOptional.ifPresentOrElse(client ->
                        {
                            if(message.code() == AuthMessage.CLIENT_RESPONSE)
                            {
                                IAdvancedHostManager<C> hostManager1 = hostManager.get();
                                hostManager1.runOnMainThreadSafely(
                                        () -> hostManager1.authenticate(client, message.name(), message.identifier()));
                            }
                        }, () ->
                        {
                            if(message.code() == AuthMessage.SERVER_REQUEST)
                            {
                                IAdvancedClientManager clientManager1 = clientManager.get();
                                clientManager1.setSessionIdentifier(message.identifier());
                                clientManager1.sendMessageToServer(authResponse());
                            }
                        }), AuthMessage.class);
        return this;
    }
    
    @Override
    public PingMessage requestPing()
    {
        return new PingMessage(PingMessage.REQUEST);
    }
    
    @Override
    public PingMessage respondToPing()
    {
        return new PingMessage(PingMessage.RESPONSE);
    }
    
    @Override
    public KickClientMessage forceDisconnectClient()
    {
        return new KickClientMessage(KickClientMessage.UNKNOWN, "");
    }
    
    @Override
    public KickClientMessage serverClosed()
    {
        return new KickClientMessage(KickClientMessage.SERVER_CLOSED, "");
    }
    
    @Override
    public KickClientMessage kickClient()
    {
        return new KickClientMessage(KickClientMessage.CLIENT_KICKED, "");
    }
    
    @Override
    public KickClientMessage kickClient(String message)
    {
        return new KickClientMessage(KickClientMessage.CLIENT_KICKED_MESSAGE, message);
    }
    
    @Override
    public LeaveServerMessage orderlyDisconnected()
    {
        return new LeaveServerMessage(LeaveServerMessage.ORDERLY_DISCONNECTED);
    }
    
    @Override
    public LeaveServerMessage disconnectedDueToException()
    {
        return new LeaveServerMessage(LeaveServerMessage.DISCONNECTED_DUE_TO_EXCEPTION);
    }
    
    @Override
    public AuthMessage authRequest()
    {
        return new AuthMessage(AuthMessage.SERVER_REQUEST, hostManager.get().getSessionIdentifier(), "");
    }
    
    @Override
    public AuthMessage authResponse()
    {
        IAdvancedClientManager clientManager1 = clientManager.get();
        return new AuthMessage(AuthMessage.CLIENT_RESPONSE, clientManager1.getClientIdentifierForSession(), clientManager1.getName());
    }
}
