package sweng_plus.framework.boardgame;

import com.google.gson.stream.JsonReader;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.EngineUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class I18n
{
    private Map<String, String> map;
    
    private String locale;
    
    public String getLocale()
    {
        return locale;
    }
    
    public void initializeI18N(String locale) throws IOException
    {
        this.locale = locale;
        
        String file = "/i18n/" + locale + ".json";
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
    
    public String translate(String key)
    {
        return map.getOrDefault(key, key);
    }
}
