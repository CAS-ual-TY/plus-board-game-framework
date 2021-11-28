package sweng_plus.boardgames_test.ludo;

import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gui.LudoBoardMapper;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.texture.TextureHelper;

import java.io.IOException;

public class BoardGameDebugScreen extends Screen
{
    public BoardGameDebugScreen(IScreenHolder screenHolder) throws IOException
    {
        super(screenHolder);
        
        Texture t = TextureHelper.createTexture("src/main/resources/textures/node.png");
        
        LudoBoard b = new LudoBoard(TeamColor.TEAMS_6);
        widgets.addAll(LudoBoardMapper.mapLudoBoard(this, b, t).values());
    }
}
