package sweng_plus.boardgames.ludo.gui;

import org.joml.Vector2i;
import sweng_plus.framework.userinterface.gui.texture.SpriteTexture;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LudoTextures
{
    public static Texture activeButton;
    public static Texture inactiveButton;
    public static Texture node;
    public static Texture figure;
    
    public static SpriteTexture[] diceAnim1;
    public static int diceAnim1Last;
    public static Vector2i[] diceAnim1Positions;
    
    public static void load() throws IOException
    {
        activeButton = TextureHelper.createTexture("src/test/resources/textures/button_test_active.png");
        inactiveButton = TextureHelper.createTexture("src/test/resources/textures/button_test_inactive.png");
        node = TextureHelper.createTexture("src/main/resources/textures/node.png");
        figure = TextureHelper.createTexture("src/main/resources/textures/figure.png");
        
        diceAnim1 = TextureHelper.createTexture("src/main/resources/textures/dice/anim_1/sprites.png").makeSprites(64, 64);
        diceAnim1Positions = new Vector2i[diceAnim1.length];
        for(int i = 0; i < diceAnim1Positions.length; i++)
        {
            diceAnim1Positions[i] = new Vector2i(0, 0);
        }
        diceAnim1Last = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("src/main/resources/textures/dice/anim_1/coordinates.txt"))))
        {
            int index, left, top, right, bottom;
            int w, h;
            String line;
            String[] split;
            while((line = reader.readLine()) != null && !line.isEmpty())
            {
                split = line.split(" ");
                index = Integer.parseInt(split[0].substring(0, split[0].length() - 1));
                left = Integer.parseInt(split[1]);
                top = Integer.parseInt(split[2]);
                right = Integer.parseInt(split[3]);
                bottom = Integer.parseInt(split[4]);
                
                diceAnim1Last = Math.max(diceAnim1Last, index);
                w = right - left;
                h = bottom - top;
                diceAnim1Positions[index].add(left, top).add(w / 2, h / 2);
            }
        }
    }
}
