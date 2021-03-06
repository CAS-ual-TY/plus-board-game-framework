package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.boardgames.ludo.gamelogic.LudoGameLogic;
import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;
import sweng_plus.boardgames.ludo.gui.util.LudoBoardMapper;
import sweng_plus.boardgames.ludo.gui.util.LudoStyles;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.style.FunctionalTextStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LudoScreen extends Screen implements ILudoScreen
{
    public final LudoGameLogic logic;
    
    public final LudoBoard board;
    public final HashMap<LudoNode, LudoNodeWidget> nodeWidgetMap;
    
    public int thisPlayerID;
    
    public FontRenderer chatFontRenderer;
    public int chatWidth;
    public int chatHeight;
    public Widget sendChatWidget;
    public InputWidget inputWidget;
    public Widget chatWidget;
    public List<ChatMessage> chatMessages;
    
    public LudoScreen(IScreenHolder screenHolder, LudoGameLogic logic)
    {
        super(screenHolder);
        
        this.logic = logic;
        board = logic.getLudoBoard();
        
        //noinspection ThisEscapedInObjectConstruction
        nodeWidgetMap = LudoBoardMapper.mapLudoBoard(this, board, LudoTextures.node, LudoTextures.figure);
        widgets.addAll(nodeWidgetMap.values());
        
        // sort widgets (nodes), render top to bottom so bottom widgets are in front
        // and make figures render on top of nodes above (= behind) them
        widgets.sort(Widget.TOP_TO_BOTTOM_SORTER);
        
        thisPlayerID = -1;
        
        chatFontRenderer = Ludo.instance().fontRenderer24;
        chatWidth = 600;
        chatHeight = chatFontRenderer.getHeight() + chatFontRenderer.getHeight() / 2;
        chatMessages = new LinkedList<>();
        
        widgets.add(sendChatWidget = new FunctionalButtonWidget(screenHolder, new Dimensions(chatHeight, chatHeight, AnchorPoint.BR), LudoStyles.makeButtonStyle(">"), this::sendMessage));
        widgets.add(inputWidget = new InputWidget(screenHolder, new Dimensions(chatWidth - chatHeight, chatHeight, AnchorPoint.BR, -chatHeight, 0), LudoStyles.makeActiveInputStyle(() -> inputWidget, AnchorPoint.L), LudoStyles.makeInactiveInputStyle(() -> inputWidget, AnchorPoint.L), (w) -> sendMessage()));
        widgets.add(chatWidget = new SimpleWidget(screenHolder, new Dimensions(chatWidth, 0, AnchorPoint.BR, 0, -chatHeight), new FunctionalTextStyle(chatFontRenderer, this::getChat, AnchorPoint.BL, Color4f.BLACK)));
    }
    
    @Override
    public void initScreen(int screenW, int screenH)
    {
        super.initScreen(screenW, screenH);
        
        int right = nodeWidgetMap.values().stream().mapToInt(w -> w.getDimensions().x + w.getDimensions().w * 2)
                .max().orElse(screenW - 600);
        
        chatWidth = screenW - right;
        
        inputWidget.getDimensions().w = chatWidth - chatHeight;
        chatWidget.getDimensions().w = chatWidth;
        
        inputWidget.initWidget(this);
        chatWidget.initWidget(this);
    }
    
    public boolean isTurnPlayer()
    {
        return thisPlayerID == logic.currentTeamIndex;
    }
    
    public List<String> getChat()
    {
        return chatMessages.stream().map(m -> m.sender() + ": " + m.message())
                .map(s -> chatFontRenderer.splitStringToWidth(chatWidth, s)).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    
    public void removeUniversalWidgets(Consumer<Widget> consumer)
    {
        widgets.remove(sendChatWidget);
        widgets.remove(inputWidget);
        widgets.remove(chatWidget);
        
        consumer.accept(sendChatWidget);
        consumer.accept(inputWidget);
        consumer.accept(chatWidget);
    }
    
    public void reAddUniversalWidgets()
    {
        widgets.add(sendChatWidget);
        widgets.add(inputWidget);
        widgets.add(chatWidget);
    }
    
    public void sendMessage()
    {
        Ludo.instance().getNetworking().clientManager.sendMessageToServer(new ChatMessage("", inputWidget.getText()));
        inputWidget.clearText();
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
        if(!logic.gameWon)
        {
            System.out.println("Screen: newTurn");
            
            logic.setTurnTeam(turnTeam);
            logic.startPhaseRoll();
            
            if(logic.currentTeamIndex == thisPlayerID)
            {
                requestDice();
            }
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
                if(isTurnPlayer())
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
        
        LudoFigure selectedFigure = logic.getFigureForIndex(figure);
        
        if(selectedFigure != null)
        {
            LudoNode startNode = selectedFigure.getCurrentNode();
            LudoNode endNode = logic.getTargetNode(selectedFigure);
            
            logic.startPhaseMoveFigure(figure);
            
            Runnable onEnd = () ->
            {
                logic.endPhaseMoveFigure(selectedFigure, endNode).ifPresentOrElse((hitFigure) ->
                {
                    LudoNode outsideNode = logic.ludoBoard.getFreeOutsideNode(hitFigure);
                    hitFigure.getCurrentNode().removeFigure(hitFigure);
                    
                    Runnable onEnd2 = () ->
                    {
                        logic.getLudoBoard().moveFigure(hitFigure, outsideNode);
                        logic.endPhaseSelectFigure(figure);
                        newTurn(logic.currentTeamIndex);
                    };
                    
                    screenHolder.setScreen(new FigureAnimationScreen(this, onEnd2, hitFigure, endNode,
                            outsideNode, false, true, true));
                }, () ->
                {
                    logic.endPhaseSelectFigure(figure);
                    newTurn(logic.currentTeamIndex);
                });
            };
            
            screenHolder.setScreen(new FigureAnimationScreen(this, onEnd, selectedFigure, startNode, endNode,
                    true, !endNode.isOccupied(), false));
        }
        else
        {
            logic.endPhaseSelectFigure(figure);
            
            newTurn(logic.currentTeamIndex);
        }
    }
    
    @Override
    public void gameWon(String winnerName)
    {
        System.out.println("Screen: gameWon");
        
        screenHolder.setScreen(new WinScreen(this, winnerName));
        
        logic.gameWon = true;
    }
    
    @Override
    public void chat(ChatMessage message)
    {
        chatMessages.add(message);
        System.out.println("chat: " + chatWidget.getDimensions().x + " / " + chatWidget.getDimensions().y);
    }
}
