package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gamelogic.MillBoard;
import sweng_plus.boardgames.mill.gamelogic.MillGameLogic;
import sweng_plus.boardgames.mill.gui.util.MillStyles;
import sweng_plus.boardgames.mill.gui.util.MillTextures;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.function.Consumer;

public class MillScreen extends Screen implements IMillScreen
{
    public final MillGameLogic logic;
    
    public final MillBoard board;
    //public final HashMap<MillNode, MillNodeWidget> nodeWidgetMap;
    
    public int thisPlayerID;
    
    public FontRenderer chatFontRenderer;
    public int chatWidth;
    public int chatHeight;
    public Widget sendChatWidget;
    public InputWidget inputWidget;
    public Widget chatWidget;
    //public List<ChatMessage> chatMessages;
    
    public MillScreen(IScreenHolder screenHolder, MillGameLogic logic)
    {
        super(screenHolder);
        
        this.logic = logic;
        board = logic.getMillBoard();
        
        //noinspection ThisEscapedInObjectConstruction
        //nodeWidgetMap = MillBoardMapper.mapMillBoard(this, board, MillTextures.node, MillTextures.figure);
        //widgets.addAll(nodeWidgetMap.values());
        
        // sort widgets (nodes), render top to bottom so bottom widgets are in front
        // and make figures render on top of nodes above (= behind) them
        widgets.sort(Widget.TOP_TO_BOTTOM_SORTER);
        
        thisPlayerID = -1;
        
        chatFontRenderer = Mill.instance().fontRenderer24;
        chatWidth = 600;
        chatHeight = chatFontRenderer.getHeight() + chatFontRenderer.getHeight() / 2;
        //chatMessages = new LinkedList<>();
        
        widgets.add(sendChatWidget = new FunctionalButtonWidget(screenHolder, new Dimensions(chatHeight, chatHeight, AnchorPoint.BR), MillStyles.makeButtonStyle(">"), this::sendMessage));
        widgets.add(inputWidget = new InputWidget(screenHolder, new Dimensions(chatWidth - chatHeight, chatHeight, AnchorPoint.BR, -chatHeight, 0), MillStyles.makeActiveInputStyle(() -> inputWidget, AnchorPoint.L), MillStyles.makeInactiveInputStyle(() -> inputWidget, AnchorPoint.L), (w) -> sendMessage()));
        //widgets.add(chatWidget = new SimpleWidget(screenHolder, new Dimensions(chatWidth, 0, AnchorPoint.BR, 0, -chatHeight), new FunctionalTextStyle(chatFontRenderer, this::getChat, AnchorPoint.BL, Color4f.BLACK)));
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
        /*
        int right = nodeWidgetMap.values().stream().mapToInt(w -> w.getDimensions().x + w.getDimensions().w * 2)
                .max().orElse(screenW - 600);
        
        chatWidth = screenW - right;
        
        inputWidget.getDimensions().w = chatWidth - chatHeight;
        chatWidget.getDimensions().w = chatWidth;
        
        inputWidget.initWidget(this);
        chatWidget.initWidget(this);
         */
    }
    
    public boolean isTurnPlayer()
    {
        //return thisPlayerID == logic.currentTeamIndex;
        return false;
    }
    
    /*
    public List<String> getChat()
    {
        return chatMessages.stream().map(m -> m.sender() + ": " + m.message())
                .map(s -> chatFontRenderer.splitStringToWidth(chatWidth, s)).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
     */
    
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
        //Mill.instance().getNetworking().clientManager.sendMessageToServer(new ChatMessage(Mill.instance().getNetworking().name, inputWidget.getText()));
        //inputWidget.clearText();
    }
    
    public void requestFigure()
    {
        System.out.println("Screen: requestFigure");
        screenHolder.setScreen(new SelectFigureScreen(this));
    }
    
    public void newTurn(int turnTeam)
    {
        
        /*
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
        */
    }
    
    @Override
    public void figureSelected(int figure)
    {
        /*
        System.out.println("Screen: figureSelected");
        
        MillFigure selectedFigure = logic.getFigureForIndex(figure);
        
        if(selectedFigure != null)
        {
            MillNode startNode = selectedFigure.getCurrentNode();
            MillNode endNode = logic.getTargetNode(selectedFigure);
            
            logic.startPhaseMoveFigure(figure);
            
            Runnable onEnd = () ->
            {
                logic.endPhaseMoveFigure(selectedFigure, endNode).ifPresentOrElse((hitFigure) ->
                {
                    MillNode outsideNode = logic.millBoard.getFreeOutsideNode(hitFigure);
                    hitFigure.getCurrentNode().removeFigure(hitFigure);
                    
                    Runnable onEnd2 = () ->
                    {
                        logic.getMillBoard().moveFigure(hitFigure, outsideNode);
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
        */
    }
    
    @Override
    public void gameWon(int winningTeamIndex)
    {
        /*
        System.out.println("Screen: gameWon");
        
        screenHolder.setScreen(new WinScreen(this, logic.teams[winningTeamIndex].getName()));
        
        logic.gameWon = true;
        */
    }
    /*
    @Override
    public void chat(ChatMessage message)
    {
        chatMessages.add(message);
        System.out.println("chat: " + chatWidget.getDimensions().x + " / " + chatWidget.getDimensions().y);
    }
     */
}
