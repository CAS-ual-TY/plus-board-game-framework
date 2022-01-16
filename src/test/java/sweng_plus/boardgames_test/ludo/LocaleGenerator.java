package sweng_plus.boardgames_test.ludo;

import com.google.gson.JsonObject;
import sweng_plus.framework.boardgame.Engine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class LocaleGenerator
{
    protected static LinkedList<StringTuple> german = new LinkedList<>();
    protected static LinkedList<StringTuple> english = new LinkedList<>();
    
    public static void main(String[] args)
    {
        // Ludo Main Menu
        LocaleGenerator.add("menu.main.new_game", "Neues Spiel", "New Game");
        LocaleGenerator.add("menu.main.join_game", "Spiel beitreten", "Join Game");
        LocaleGenerator.add("menu.main.settings", "Einstellungen", "Settings");
        LocaleGenerator.add("menu.main.quit", "Beenden", "Quit");
        
        // Ludo ConnectScreen
        LocaleGenerator.add("menu.connect.name", "Name", "Name");
        LocaleGenerator.add("menu.connect.ip", "IP", "IP");
        LocaleGenerator.add("menu.connect.port", "Port", "Port");
        
        // Ludo HostScreen
        LocaleGenerator.add("menu.host.name", "Name", "Name");
        LocaleGenerator.add("menu.host.port", "Port", "Port");
        
        // Ludo Cancel and Ready Button
        LocaleGenerator.add("menu.cancel", "Abbruch", "Cancel");
        LocaleGenerator.add("menu.ready", "Fertig", "Ready");
        
        LocaleGenerator.add("menu.lobby.back", "Zurück zum Menü", "Back to menu");
        LocaleGenerator.add("ludo.start_game", "Spiel starten", "Start game");
        
        // Ludo Roll Screen
        LocaleGenerator.add("ludo.roll_dice", "Würfeln", "Roll Dice");
        
        // Ludo Win Message
        LocaleGenerator.add("ludo.win.prefix", "", "");
        LocaleGenerator.add("ludo.win.suffix", "hat gewonnen", "has won");
        
        
        toJson(german, "de_de");
        toJson(english, "en_us");
    }
    
    private static void add(String key, String valueGerman, String valueEnglish)
    {
        german.add(new StringTuple(key, valueGerman));
        english.add(new StringTuple(key, valueEnglish));
    }
    
    private static void toJson(LinkedList<StringTuple> list, String locale)
    {
        JsonObject jsonObject = new JsonObject();
        
        list.forEach(stringTuple -> jsonObject.addProperty(stringTuple.key(), stringTuple.value()));
        
        String filePath = "src/main/resources/i18n/" + locale + ".json";
        try(FileWriter writer = new FileWriter(filePath))
        {
            Engine.GSON.toJson(jsonObject, writer);
            System.out.println(Engine.GSON.toJson(jsonObject));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private record StringTuple(String key, String value) {}
}
