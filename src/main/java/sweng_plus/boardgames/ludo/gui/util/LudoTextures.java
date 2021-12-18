package sweng_plus.boardgames.ludo.gui.util;

import org.joml.Vector2i;
import sweng_plus.framework.boardgame.EngineUtil;
import sweng_plus.framework.userinterface.gui.texture.SpriteTexture;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LudoTextures
{
    public static Texture activeButton;
    public static Texture inactiveButton;
    public static Texture node;
    public static Texture figure;
    public static Texture figureHuge;
    public static Texture logo;
    
    public static Texture[] dices;
    public static SpriteTexture[][] diceAnim;
    public static int[] diceAnimLast;
    public static Vector2i[][] diceAnimPositions;
    
    public static void load() throws IOException
    {
        activeButton = TextureHelper.createTexture("/textures/button_active.png");
        inactiveButton = TextureHelper.createTexture("/textures/button_inactive.png");
        node = TextureHelper.createTexture("/textures/node.png");
        figure = TextureHelper.createTexture("/textures/figure.png");
        figureHuge = TextureHelper.createTexture("/textures/background/figure_huge.png");
        logo = TextureHelper.createTexture("/textures/background/logo.png");
        
        dices = new Texture[6];
        diceAnim = new SpriteTexture[dices.length][];
        diceAnimLast = new int[dices.length];
        diceAnimPositions = new Vector2i[dices.length][];
        
        for(int dice = 0; dice < dices.length; ++dice)
        {
            dices[dice] = TextureHelper.createTexture("/textures/dice/dice_" + (dice + 1) + ".png");
            
            diceAnim[dice] = TextureHelper.createTexture("/textures/dice/anim_" + (dice + 1) + "/sprites.png").makeSprites(64, 64);
            diceAnimPositions[dice] = new Vector2i[diceAnim[dice].length];
            
            for(int i = 0; i < diceAnimPositions[dice].length; i++)
            {
                diceAnimPositions[dice][i] = new Vector2i(0, 0);
            }
            
            diceAnimLast[dice] = 0;
            try(BufferedReader reader = EngineUtil.getResourceReader("/textures/dice/anim_" + (dice + 1) + "/coordinates.txt"))
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
                    
                    diceAnimLast[dice] = Math.max(diceAnimLast[dice], index);
                    w = right - left;
                    h = bottom - top;
                    diceAnimPositions[dice][index].add(left, top).add(w / 2, h / 2);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
