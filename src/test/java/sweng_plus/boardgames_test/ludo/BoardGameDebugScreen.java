package sweng_plus.boardgames_test.ludo;

import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gui.LudoBoardMapper;
import sweng_plus.boardgames.ludo.gui.LudoTextures;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.gui.widget.NodeWidget;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.io.IOException;
import java.util.HashMap;

public class BoardGameDebugScreen extends Screen
{
    public HashMap<INode, NodeWidget> nodeWidgetMap;
    
    public BoardGameDebugScreen(IScreenHolder screenHolder) throws IOException
    {
        super(screenHolder);
        
        LudoBoard b = new LudoBoard(TeamColor.TEAMS_2);
        nodeWidgetMap = new HashMap<>();
        
        HashMap<INode, LudoNodeWidget> map = LudoBoardMapper.mapLudoBoard(this, b, LudoTextures.node, LudoTextures.figure);
        nodeWidgetMap.putAll(map);
        
        widgets.addAll(nodeWidgetMap.values());
        
        widgets.add(new NodeConnectionsWidget(screenHolder, new Dimensions(AnchorPoint.M), nodeWidgetMap));
        //widgets.add(new WidgetConnectionsWidget(screenHolder, new Dimensions(AnchorPoint.M), nodeWidgetMap.values().stream().toList()));
    }
}
