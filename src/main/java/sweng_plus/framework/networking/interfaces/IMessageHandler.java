package sweng_plus.framework.networking.interfaces;

import java.util.Optional;

public interface IMessageHandler<M, C extends IClient>
{
    void handleMessage(Optional<C> clientOptional, M msg);
}
