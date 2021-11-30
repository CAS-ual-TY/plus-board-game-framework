package sweng_plus.boardgames.ludo.gui.widget;

import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gamelogic.LudoNodeType;
import sweng_plus.framework.boardgame.gui.widget.NodeWidget;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import java.util.List;

public class LudoNodeWidget extends NodeWidget
{
    protected Texture texture;
    
    protected TeamColor team;
    protected LudoNodeType type;
    
    public LudoNodeWidget(IScreenHolder screenHolder, Dimensions dimensions, LudoNode node, Texture texture)
    {
        super(screenHolder, dimensions, node);
        this.texture = texture;
        
        team = node.getColor();
        type = node.getNodeType();
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        if(type != LudoNodeType.NEUTRAL)
            team.getColor().glColor3fStrength(type == LudoNodeType.HOME_ENTRANCE ? 0.15F : type == LudoNodeType.START ? 0.5F : 1F);
        else
            team.getColor().glColor3fStrength(0.1F);
        
        texture.render(
                dimensions.x + (dimensions.w - texture.getWidth()) / 2,
                dimensions.y + (dimensions.h - texture.getHeight()) / 2);
    }
}
