package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gamelogic.LudoGameLogic;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalTextWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.HashMap;
import java.util.List;

public class LudoScreen extends Screen implements ILudoScreen
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
        
        // sort widgets (nodes), render top to bottom so bottom widgets are in front
        // and make figures render on top of nodes above (= behind) them
        widgets.sort(Widget.TOP_TO_BOTTOM_SORTER);
        
        thisPlayerID = -1;
        
        widgets.add(new FunctionalTextWidget(screenHolder, new Dimensions(80, 80, AnchorPoint.TR),
                Ludo.instance().fontRenderer48, () -> List.of(String.valueOf(logic.latestRoll)), Color4f.BLACK));
    }
    
    public void requestDice()
    {
        System.out.println("Screen: requestDice");
        screenHolder.setScreen(new DiceScreen(this));
    }
    
    public void requestFigure()
    {
        System.out.println("Screen: requestFigure");
        screenHolder.setScreen(new SelectFigureScreen(this));
    }
    
    public void rollDice(int result, Runnable onEnd)
    {
        screenHolder.setScreen(new DiceAnimationScreen(this, onEnd, result));
    }
    
    public void newTurn(int turnTeam)
    {
        System.out.println("Screen: newTurn");
        
        logic.setTurnTeam(turnTeam);
        logic.startPhaseRoll();
        
        if(logic.currentTeamIndex == thisPlayerID)
        {
            requestDice();
        }
    }
    
    @Override
    public void diceResult(int dice)
    {
        System.out.println("Screen: diceResult");
        
        logic.setLatestRoll(dice);
        logic.startPhaseSelectFigure();
        
        rollDice(dice, () ->
        {
            if(logic.movableFigures.size() > 0)
            {
                if(logic.currentTeamIndex == thisPlayerID)
                {
                    requestFigure();
                }
            }
            else
            {
                figureSelected(-1);
            }
        });
    }
    
    @Override
    public void figureSelected(int figure)
    {
        System.out.println("Screen: figureSelected");
        logic.endPhaseSelectFigure(figure);
        newTurn(logic.currentTeamIndex);
    }
}
