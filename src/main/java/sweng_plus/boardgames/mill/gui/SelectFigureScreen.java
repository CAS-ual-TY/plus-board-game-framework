package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.gamelogic.MillNode;
import sweng_plus.boardgames.mill.gui.util.MillBoardMapper;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;

import java.util.List;
import java.util.function.Consumer;

public class SelectFigureScreen extends MillExtensionScreen
{
    public SelectFigureScreen(MillScreen subScreen, TeamColor team, Consumer<MillNode> consumer)
    {
        super(subScreen);
        
        widgets.addAll(List.of(MillBoardMapper.createMovableTeamButtonWidgets(subScreen.screenHolder, subScreen.board, MillScreen.BOARD_SIZE, (node) -> { returnToSubScreen(); consumer.accept(node);}, team)));
        
    }
}
