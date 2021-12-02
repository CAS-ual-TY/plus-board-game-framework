package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.*;
import sweng_plus.framework.networking.messages.KickClientMessage;
import sweng_plus.framework.networking.messages.LeaveServerMessage;
import sweng_plus.framework.networking.messages.PingMessage;

import java.io.IOException;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class AdvancedMessageRegistry<C extends IClient> extends MessageRegistry<C>
{
    public AdvancedMessageRegistry(int messages, byte pingID, byte kickID, byte leaveID,
                                   Supplier<IClientManager> clientManager, Supplier<IHostManager<C>> hostManager,
                                   IAdvancedClientEventsListener clientEventsListener,
                                   IAdvancedHostEventsListener<C> hostEventsListener)
    {
        super(messages);
        registerMessages(pingID, kickID, leaveID, clientManager, hostManager, clientEventsListener, hostEventsListener);
    }
    
    protected void registerMessages(byte pingID, byte kickID, byte leaveID,
                                    Supplier<IClientManager> clientManager, Supplier<IHostManager<C>> hostManager,
                                    IAdvancedClientEventsListener clientEventsListener,
                                    IAdvancedHostEventsListener<C> hostEventsListener)
    {
        registerPingMessage(pingID, clientManager, hostManager);
        registerKickClientMessage(kickID, clientEventsListener);
        registerLeaveServerMessage(leaveID, hostEventsListener);
    }
    
    protected AdvancedMessageRegistry<C> registerPingMessage(byte id, Supplier<IClientManager> clientManager,
                                                             Supplier<IHostManager<C>> hostManager)
    {
        registerMessage(id, PingMessage.Handler::encodeMessage, PingMessage.Handler::decodeMessage,
                (clientOptional, message) ->
                {
                    if(message.code() == PingMessage.REQUEST)
                    {
                        clientOptional.ifPresentOrElse(
                                (client) ->
                                {
                                    try
                                    {
                                        hostManager.get().sendMessageToClient(client, PingMessage.respondToPing());
                                    }
                                    catch(IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                },
                                () ->
                                {
                                    try
                                    {
                                        clientManager.get().sendMessageToServer(PingMessage.respondToPing());
                                    }
                                    catch(IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                });
                    }
                }, PingMessage.class);
        return this;
    }
    
    protected AdvancedMessageRegistry<C> registerKickClientMessage(byte id, IAdvancedClientEventsListener eventsListener)
    {
        registerMessage(id, KickClientMessage.Handler::encodeMessage, KickClientMessage.Handler::decodeMessage,
                (clientOptional, message) ->
                        clientOptional.ifPresentOrElse((client) -> {}, () ->
                        {
                            if(message.code() == KickClientMessage.UNKNOWN)
                            {
                                eventsListener.forcedDisconnected();
                            }
                            else if(message.code() == KickClientMessage.SERVER_CLOSED)
                            {
                                eventsListener.serverClosed();
                            }
                            else if(message.code() == KickClientMessage.CLIENT_KICKED)
                            {
                                eventsListener.kickedFromServer();
                            }
                            else if(message.code() == KickClientMessage.CLIENT_KICKED_MESSAGE)
                            {
                                eventsListener.kickedFromServerWithMessage(message.message());
                            }
                        }), KickClientMessage.class);
        return this;
    }
    
    protected AdvancedMessageRegistry<C> registerLeaveServerMessage(byte id, IAdvancedHostEventsListener<C> eventsListener)
    {
        registerMessage(id, LeaveServerMessage.Handler::encodeMessage, LeaveServerMessage.Handler::decodeMessage,
                (clientOptional, message) ->
                        clientOptional.ifPresent(client ->
                        {
                            if(message.code() == LeaveServerMessage.ORDERLY_DISCONNECTED)
                            {
                                eventsListener.clientDisconnectedOrderly(client);
                            }
                            else if(message.code() == LeaveServerMessage.DISCONNECTED_DUE_TO_EXCEPTION)
                            {
                                eventsListener.clientDisconnectedDueToException(client);
                            }
                        }), LeaveServerMessage.class);
        return this;
    }
}
