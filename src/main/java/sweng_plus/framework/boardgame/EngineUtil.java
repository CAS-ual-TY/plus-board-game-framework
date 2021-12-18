package sweng_plus.framework.boardgame;

import java.io.*;
import java.net.URL;

public class EngineUtil
{
    public static URL getResourceURL(String resource)
    {
        URL url = EngineUtil.class.getResource(resource);
        
        if(url == null)
        {
            throw new NullPointerException("Can not find: " + resource);
        }
        
        return url;
    }
    
    public static InputStream getResourceInputStream(String resource) throws IOException
    {
        return getResourceURL(resource).openStream();
    }
    
    public static BufferedReader getResourceReader(String resource) throws IOException
    {
        return new BufferedReader(new InputStreamReader(getResourceInputStream(resource)));
    }
}
