package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;

import java.io.IOException;
import java.util.HashMap;

public class LudoScreen extends Screen
{
    public final LudoBoard board;
    public final HashMap<INode, LudoNodeWidget> nodeWidgetMap;
    
    public LudoScreen(IScreenHolder screenHolder, LudoBoard board) throws IOException
    {
        super(screenHolder);
        
        this.board = board;
        nodeWidgetMap = LudoBoardMapper.mapLudoBoard(this, board, LudoTextures.node);
        widgets.addAll(nodeWidgetMap.values());
    }
}
