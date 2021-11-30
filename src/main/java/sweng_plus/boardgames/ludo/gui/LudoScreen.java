package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gamelogic.LudoGameLogic;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;

import java.util.HashMap;

public class LudoScreen extends Screen
{
    public final LudoGameLogic logic;
    public final LudoBoard board;
    public final HashMap<INode, LudoNodeWidget> nodeWidgetMap;
    
    public int thisPlayerID;
    
    public LudoScreen(IScreenHolder screenHolder, LudoGameLogic logic)
    {
        super(screenHolder);
        
        this.logic = logic;
        board = logic.getLudoBoard();
        nodeWidgetMap = LudoBoardMapper.mapLudoBoard(this, board, LudoTextures.node, LudoTextures.figure);
        widgets.addAll(nodeWidgetMap.values());
        thisPlayerID = -1;
    }
    
    public void requestDice()
    {
        screenHolder.setScreen(new DiceScreen(this));
    }
    
    public void requestFigure() {} //TODO
    
    public void newTurn(int turnTeam)
    {
        logic.setTurnTeam(turnTeam);
        logic.startPhaseRoll();
        
        if(logic.currentTeamIndex == thisPlayerID)
        {
            requestDice();
        }
    }
    
    public void diceResult(int dice)
    {
        logic.setLatestRoll(dice);
        logic.startPhaseSelectFigure();
        
        if(logic.currentTeamIndex == thisPlayerID)
        {
            requestFigure();
        }
    }
    
    public void figureSelected(int figure)
    {
        logic.endPhaseSelectFigure(figure);
        newTurn(logic.currentTeamIndex);
    }
}
