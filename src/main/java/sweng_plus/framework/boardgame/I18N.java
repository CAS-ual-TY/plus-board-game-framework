package sweng_plus.framework.boardgame;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class I18N
{
    private static Map<String, String> map;
    
    public static void initializeI18N(String file) throws IOException
    {
        try(JsonReader jsonReader = new JsonReader(new InputStreamReader(EngineUtil.getResourceInputStream(file))))
        {
            map = Engine.GSON.fromJson(jsonReader, Map.class);
        }
        catch(IOException e)
        {
            map = new HashMap<>();
            
            throw e;
        }
    }
    
    public static void initializeI18N(InputStream in) throws IOException
    {
        try(JsonReader jsonReader = new JsonReader(new InputStreamReader(in)))
        {
            map = Engine.GSON.fromJson(jsonReader, Map.class);
        }
        catch(IOException e)
        {
            map = new HashMap<>();
            
            throw e;
        }
    }
    
    public static String translate(String key)
    {
        return map.getOrDefault(key, key);
    }
}
