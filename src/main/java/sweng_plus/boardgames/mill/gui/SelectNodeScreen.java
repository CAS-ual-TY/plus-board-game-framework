package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.gamelogic.MillFigure;
import sweng_plus.boardgames.mill.gamelogic.MillNode;
import sweng_plus.boardgames.mill.gui.util.MillBoardMapper;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;

import java.util.List;
import java.util.function.Consumer;

public class SelectNodeScreen extends MillExtensionScreen
{
    
    public SelectNodeScreen(MillScreen subScreen, TeamColor team, Consumer<MillNode> consumer, MillFigure selectedFigure)
    {
        super(subScreen);
        
        widgets.addAll(List.of(MillBoardMapper.createNodeButtonWidgets(subScreen.screenHolder, subScreen.board, MillScreen.BOARD_SIZE, (node) -> {
            returnToSubScreen();
            consumer.accept(node);
        }, selectedFigure)));
        
    }
}
