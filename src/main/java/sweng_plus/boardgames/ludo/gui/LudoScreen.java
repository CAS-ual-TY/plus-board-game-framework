package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gamelogic.LudoBoard;
import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.boardgames.ludo.gamelogic.LudoGameLogic;
import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gamelogic.networking.ChatMessage;
import sweng_plus.boardgames.ludo.gui.util.LudoBoardMapper;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.boardgames.ludo.gui.widget.ChatWidget;
import sweng_plus.boardgames.ludo.gui.widget.LudoNodeWidget;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.style.CorneredTextureStyle;
import sweng_plus.framework.userinterface.gui.style.HoverStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.*;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.io.IOException;
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
    public final HashMap<INode, LudoNodeWidget> nodeWidgetMap;
    
    public int thisPlayerID;
    
    public FontRenderer chatFontRenderer;
    public int chatWidth;
    public int chatHeight;
    public ButtonWidget sendChatWidget;
    public TextWidget sentChatTextWidget;
    public InputWidget inputWidget;
    public FunctionalTextWidget chatWidget;
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
        
        widgets.add(sendChatWidget = new FunctionalButtonWidget(screenHolder, new Dimensions(chatHeight, chatHeight, AnchorPoint.BR), new HoverStyle(new CorneredTextureStyle(LudoTextures.inactiveButton), new CorneredTextureStyle(LudoTextures.activeButton)), this::sendMessage));
        widgets.add(sentChatTextWidget = new TextWidget(screenHolder, new Dimensions(chatHeight, chatHeight, AnchorPoint.BR), chatFontRenderer, ">", Color4f.BLACK));
        
        widgets.add(inputWidget = new InputWidget(screenHolder, new Dimensions(chatWidth - chatHeight, chatHeight, AnchorPoint.BR, -chatHeight, 0), chatFontRenderer, (w) -> sendMessage()));
        widgets.add(chatWidget = new ChatWidget(screenHolder, new Dimensions(chatWidth, 0, AnchorPoint.BR, 0, -chatHeight), chatFontRenderer, this::getChat, Color4f.BLACK));
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
        widgets.remove(sentChatTextWidget);
        widgets.remove(inputWidget);
        widgets.remove(chatWidget);
        
        consumer.accept(sendChatWidget);
        consumer.accept(sentChatTextWidget);
        consumer.accept(inputWidget);
        consumer.accept(chatWidget);
    }
    
    public void reAddUniversalWidgets()
    {
        widgets.add(sendChatWidget);
        widgets.add(sentChatTextWidget);
        widgets.add(inputWidget);
        widgets.add(chatWidget);
    }
    
    public void sendMessage()
    {
        try
        {
            Ludo.instance().clientManager.sendMessageToServer(new ChatMessage(Ludo.instance().name, inputWidget.getText()));
            inputWidget.clearText();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
            LudoNode startNode = (LudoNode) selectedFigure.getCurrentNode();
            LudoNode endNode = logic.getTargetNode(selectedFigure);
            
            logic.startPhaseMoveFigure(figure);
            
            Runnable onEnd = () ->
            {
                logic.endPhaseMoveFigure(selectedFigure, endNode).ifPresentOrElse((hitFigure) ->
                {
                    LudoNode outsideNode = logic.ludoBoard.getFreeOutsideNode(hitFigure);
                    hitFigure.getCurrentNode().removeNodeFigure(hitFigure);
                    
                    Runnable onEnd2 = () ->
                    {
                        hitFigure.move(outsideNode);
                        outsideNode.addNodeFigure(hitFigure);
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
    public void gameWon(int winningTeamIndex)
    {
        System.out.println("Screen: gameWon");
        
        screenHolder.setScreen(new WinScreen(this, logic.teams[winningTeamIndex].getName()));
        
        logic.gameWon = true;
    }
    
    @Override
    public void chat(ChatMessage message)
    {
        chatMessages.add(message);
        chatWidget.adjustSizeToText();
        chatWidget.initWidget(this);
        System.out.println("chat: " + chatWidget.getDimensions().x + " / " + chatWidget.getDimensions().y);
    }
}
