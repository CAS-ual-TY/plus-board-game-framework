package sweng_plus.boardgames_test.ludo;

import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gui.LudoBoardMapper;
import sweng_plus.boardgames.ludo.gui.LudoTextures;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;

import java.io.IOException;
import java.util.HashMap;

public class BoardGameDebugScreen extends Screen
{
    public HashMap<INode, LudoNodeWidget> nodeWidgetMap;
    
    public BoardGameDebugScreen(IScreenHolder screenHolder) throws IOException
    {
        super(screenHolder);
        
        LudoBoard b = new LudoBoard(TeamColor.TEAMS_6);
        nodeWidgetMap = LudoBoardMapper.mapLudoBoard(this, b, LudoTextures.node);
        widgets.addAll(nodeWidgetMap.values());
    }
}
