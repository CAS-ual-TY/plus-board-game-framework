package sweng_plus.framework.networking.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.networking.interfaces.IClientSessionManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class ClientSessionManager implements IClientSessionManager
{
    protected File file;
    protected Map<UUID, UUID> sessionIdentifiers;
    
    public ClientSessionManager(File file) throws IOException
    {
        sessionIdentifiers = new HashMap<>();
        this.file = file;
        
        readFromFile();
    }
    
    public ClientSessionManager() throws IOException
    {
        this(new File("sessions.json"));
    }
    
    @Override
    public UUID getIdentifierForSession(UUID session)
    {
        if(sessionIdentifiers.containsKey(session))
        {
            return sessionIdentifiers.get(session);
        }
        else
        {
            UUID identifier = UUID.randomUUID();
            sessionIdentifiers.put(session, identifier);
            return identifier;
        }
    }
    
    public void readFromFile() throws IOException
    {
        sessionIdentifiers.clear();
        
        if(!file.exists())
        {
            file.createNewFile();
        }
        
        try(FileReader reader = new FileReader(file))
        {
            JsonObject json = Engine.GSON.fromJson(reader, JsonObject.class);
            
            if(json != null)
            {
                for(Entry<String, JsonElement> entry : json.entrySet())
                {
                    sessionIdentifiers.put(UUID.fromString(entry.getKey()), UUID.fromString(entry.getValue().getAsString()));
                }
            }
        }
        catch(ClassCastException | IllegalStateException e)
        {
            e.printStackTrace();
        }
    }
    
    public void writeToFile() throws IOException
    {
        if(!file.exists())
        {
            file.createNewFile();
        }
        
        try(FileWriter writer = new FileWriter(file))
        {
            JsonObject json = new JsonObject();
            
            for(Entry<UUID, UUID> entry : sessionIdentifiers.entrySet())
            {
                json.addProperty(entry.getKey().toString(), entry.getValue().toString());
            }
            
            Engine.GSON.toJson(json, writer);
        }
        catch(ClassCastException | IllegalStateException e)
        {
            e.printStackTrace();
        }
    }
}
