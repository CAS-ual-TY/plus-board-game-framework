package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gamelogic.MillBoard;
import sweng_plus.boardgames.mill.gamelogic.MillFigure;
import sweng_plus.boardgames.mill.gamelogic.MillGameLogic;
import sweng_plus.boardgames.mill.gamelogic.MillNode;
import sweng_plus.boardgames.mill.gamelogic.networking.TellServerFigureNodeSelectedMessage;
import sweng_plus.boardgames.mill.gamelogic.networking.TellServerFigureTakenMessage;
import sweng_plus.boardgames.mill.gui.util.MillBoardMapper;
import sweng_plus.boardgames.mill.gui.util.MillTextures;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.List;
import java.util.function.Consumer;

public class MillScreen extends Screen implements IMillScreen
{
    public static final int BOARD_SIZE = 128;
    
    public final MillGameLogic logic;
    
    public final MillBoard board;
    public int thisPlayerID;
    
    
    public MillScreen(IScreenHolder screenHolder, MillGameLogic logic)
    {
        super(screenHolder);
        
        this.logic = logic;
        board = logic.getMillBoard();
        
        widgets.addAll(List.of(MillBoardMapper.createSimpleWidgets(getScreenHolder(), board, BOARD_SIZE)));
        
        // sort widgets (nodes), render top to bottom so bottom widgets are in front
        // and make figures render on top of nodes above (= behind) them
        widgets.sort(Widget.TOP_TO_BOTTOM_SORTER);
        
        widgets.add(0, new MillBoardWidget(getScreenHolder(), new Dimensions(AnchorPoint.M), BOARD_SIZE));
        
        thisPlayerID = -1;
        
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        Color4f.NEUTRAL.glColor4f();
        int x = screenW / 2 - MillTextures.background.getWidth() / 2;
        int y = screenH / 2 - MillTextures.background.getHeight() / 2;
        MillTextures.background.render(x, y);
        
        super.render(deltaTick, mouseX, mouseY);
    }
    
    @Override
    public void initScreen(int screenW, int screenH)
    {
        super.initScreen(screenW, screenH);
    }
    
    public boolean isTurnPlayer()
    {
        return thisPlayerID == logic.getCurrentTeamIndex();
    }
    
    public void moveFigureSelectAction(MillNode node)
    {
        
        requestNode(node.getFigures().get(0));
    }
    
    public Consumer<MillNode> nodeSelectAction(MillFigure figure)
    {
        return (node) -> Mill.instance().getNetworking().getClientManager().sendMessageToServer(new TellServerFigureNodeSelectedMessage(figure.getIndex(), node.getIndex()));
    }
    
    public void takenFigureSelectedAction(MillFigure figure)
    {
        Mill.instance().getNetworking().getClientManager().sendMessageToServer(new TellServerFigureTakenMessage(figure.getIndex()));
    }
    
    public void requestFigure()
    {
        System.out.println("Screen: requestFigure");
        screenHolder.setScreen(new SelectFigureScreen(this, logic.getCurrentTeam(), this::moveFigureSelectAction));
    }
    
    public void requestNode(MillFigure selectedFigure)
    {
        System.out.println("Screen: requestNode");
        System.out.println(selectedFigure.getIndex());
        screenHolder.setScreen(new SelectNodeScreen(this, logic.getCurrentTeam(), nodeSelectAction(selectedFigure), selectedFigure));
    }
    
    //@Override
    public void requestTakeFigure()
    {
        System.out.println("Screen: requestTakeFigure");
        
        screenHolder.setScreen(new TakeFigureScreen(this, logic.getOtherTeam(), this::takenFigureSelectedAction));
    }
    
    public void newTurn(int turnTeam)
    {
        if(!logic.isGameWon())
        {
            System.out.println("Screen: newTurn");
            
            logic.setTurnTeam(turnTeam);
            logic.startPhaseSelectFigure();
            
            if(logic.getCurrentTeamIndex() == thisPlayerID)
            {
                requestFigure();
            }
        }
    }
    
    @Override
    public void figureNodeSelected(int figure, int node)
    {
        System.out.println("Screen: figureNodeSelected");
        
        MillFigure selectedFigure = logic.getMovableFigureForIndex(figure);
        
        if(selectedFigure != null)
        {
            MillNode startNode = selectedFigure.getCurrentNode();
            MillNode endNode = logic.getNodeForIndex(node);
            
            logic.startPhaseMoveFigure(figure);
            
            if(logic.endPhaseMoveFigure(selectedFigure, endNode))
            {
                logic.startPhaseTakeFigure();
                
                if(!logic.getTakeableFigure().isEmpty())
                {
                    if(thisPlayerID == logic.getCurrentTeamIndex())
                    {
                        requestTakeFigure();
                    }
                }
                else
                {
                    endTurn();
                }
            }
            else
            {
                endTurn();
            }
        }
        else
        {
            endTurn();
        }
    }
    
    @Override
    public void figureTaken(int figure)
    {
        System.out.println("Screen: figureTaken");
        
        MillFigure selectedFigure = logic.getTakeableFigureForIndex(figure);
        
        if(selectedFigure != null)
        {
            logic.endPhaseTakeFigure(selectedFigure);
        }
        endTurn();
    }
    
    private void endTurn()
    {
        logic.endPhaseSelectFigure();
        newTurn(logic.getCurrentTeamIndex());
    }
    
    @Override
    public void gameWon(String winnerName)
    {
        System.out.println("Screen: gameWon");
        
        screenHolder.setScreen(new WinScreen(this, winnerName));
    }
}
