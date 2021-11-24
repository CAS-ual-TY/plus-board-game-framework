package sweng_plus.framework.boardgame.nodes_board.interfaces;

public interface INodeFigure
{
    INode getCurrentNode();
    
    void setCurrentNode(INode node);
    
    void move(INode node);
}
