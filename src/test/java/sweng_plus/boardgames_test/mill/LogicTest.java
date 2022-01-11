package sweng_plus.boardgames_test.mill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sweng_plus.boardgames.mill.gamelogic.MillBoard;
import sweng_plus.boardgames.mill.gamelogic.MillFigure;
import sweng_plus.boardgames.mill.gamelogic.MillGameLogic;
import sweng_plus.boardgames.mill.gamelogic.MillNode;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;

import java.util.List;

public class LogicTest
{
    private MillGameLogic logic;
    @BeforeEach
    public void init() {
        logic = new MillGameLogic(new TeamColor[] {TeamColor.WHITE, TeamColor.BLACK}, false);
    }
    
    @Test
    public void testMillOuterCircle() {
        MillBoard board = logic.getMillBoard();
        // place 3 figures for mill
        MillFigure placedFigure;
        // horizontal
        for(int i = 0; i < 2; i++)
        {
            logic.startPhaseSelectFigure();
            placedFigure = logic.startPhaseMoveFigure(i);
            Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(i)));
        }
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(2);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(2)));
    
        // vertical left
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(3);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(7)));
    
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(4);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(6)));
        
        // vertical middle
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(5);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(9)));
    
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(6);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(17)));
        
        // vertical right
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(7);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(3)));
    
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(8);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(4)));
    }
    
    @Test
    public void testMillMidCircle() {
        MillBoard board = logic.getMillBoard();
        Assertions.assertEquals(logic.getUnplacedFigures().size(), MillBoard.FIGURES_PER_TEAM);
        //logic.getUnplacedFigures().
        // place 3 figures for mill
        MillFigure placedFigure;
        for(int i = 0; i < 2; i++)
        {
            System.out.println(i);
            logic.startPhaseSelectFigure();
            placedFigure = logic.startPhaseMoveFigure(i);
            Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(i+8)));
        }
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(2);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(10)));
    
    
        for(int i = 0; i < 2; i++)
        {
            System.out.println(i);
            logic.startPhaseSelectFigure();
            placedFigure = logic.startPhaseMoveFigure(i+3);
            Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(i+12)));
        }
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(5);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(14)));
    }
    
    @Test
    public void testMillInnerCircle() {
        MillBoard board = logic.getMillBoard();
        // place 3 figures for mill
        MillFigure placedFigure;
        for(int i = 0; i < 2; i++)
        {
            logic.startPhaseSelectFigure();
            placedFigure = logic.startPhaseMoveFigure(i);
            Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(i+16)));
        }
        logic.startPhaseSelectFigure();
        placedFigure = logic.startPhaseMoveFigure(3);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure, board.getFieldNodes().get(18)));
    }
    
    @Test
    public void testMillAfterMove() {
        MillBoard board = logic.getMillBoard();
        // place 3 figures for mill
        MillFigure placedFigure1;
        MillFigure placedFigure2;
        MillFigure placedFigure3;
        
        logic.startPhaseSelectFigure();
        placedFigure1 = logic.startPhaseMoveFigure(0);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure1, board.getFieldNodes().get(0)));
    
        logic.startPhaseSelectFigure();
        placedFigure2 = logic.startPhaseMoveFigure(1);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure2, board.getFieldNodes().get(1)));
    
        logic.startPhaseSelectFigure();
        placedFigure3 = logic.startPhaseMoveFigure(2);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure3, board.getFieldNodes().get(2)));
        
        // place rest in middle
        for(int i = 3; i < MillBoard.FIGURES_PER_TEAM; i++) {
            logic.startPhaseSelectFigure();
            logic.endPhaseMoveFigure(logic.startPhaseMoveFigure(i), board.getFieldNodes().get(i-3+16));
        }
        
        logic.startPhaseSelectFigure();
        Assertions.assertEquals(logic.startPhaseMoveFigure(1), placedFigure2);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure2, board.getFieldNodes().get(14)));
        
        Assertions.assertFalse(placedFigure1.isInMill());
        Assertions.assertFalse(placedFigure3.isInMill());
    }
    
    @Test
    public void testMillAfterMoveMillRemaining() {
        MillBoard board = logic.getMillBoard();
        // place 3 figures for mill
        MillFigure placedFigure1;
        MillFigure placedFigure2;
        MillFigure placedFigure3;
        MillFigure placedFigure4;
        MillFigure placedFigure5;
        
        logic.startPhaseSelectFigure();
        placedFigure1 = logic.startPhaseMoveFigure(0);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure1, board.getFieldNodes().get(0)));
        
        logic.startPhaseSelectFigure();
        placedFigure2 = logic.startPhaseMoveFigure(1);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure2, board.getFieldNodes().get(1)));
        
        logic.startPhaseSelectFigure();
        placedFigure3 = logic.startPhaseMoveFigure(2);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure3, board.getFieldNodes().get(2)));
    
        logic.startPhaseSelectFigure();
        placedFigure4 = logic.startPhaseMoveFigure(3);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure4, board.getFieldNodes().get(3)));
    
        logic.startPhaseSelectFigure();
        placedFigure5 = logic.startPhaseMoveFigure(4);
        Assertions.assertTrue(logic.endPhaseMoveFigure(placedFigure5, board.getFieldNodes().get(4)));
        
        // place rest in middle
        for(int i = 5; i < MillBoard.FIGURES_PER_TEAM; i++) {
            logic.startPhaseSelectFigure();
            logic.endPhaseMoveFigure(logic.startPhaseMoveFigure(i), board.getFieldNodes().get(i-5+16));
        }
        
        logic.startPhaseSelectFigure();
        Assertions.assertEquals(logic.startPhaseMoveFigure(1), placedFigure2);
        Assertions.assertFalse(logic.endPhaseMoveFigure(placedFigure2, board.getFieldNodes().get(14)));
        
        for(int i = 8; i < 16; i++) {
            System.out.println(board.getFieldNode(i).isOccupied());
        }
        
        Assertions.assertFalse(placedFigure1.isInMill());
        Assertions.assertFalse(placedFigure2.isInMill());
        Assertions.assertTrue(placedFigure3.isInMill());
        Assertions.assertTrue(placedFigure4.isInMill());
        Assertions.assertTrue(placedFigure5.isInMill());
    }
    
    /*
    public static void main(String... args)
    {
        System.out.println("test");
        
        MillGameLogic logic = new MillGameLogic(new TeamColor[] {TeamColor.RED, TeamColor.BLUE}, false);
        MillBoard board = logic.getMillBoard();
        List<MillFigure> figures = board.getActiveTeamFigures(TeamColor.RED);
        
        System.out.println();
        System.out.println("NumNodes = " + board.getFieldNodes().size());
        
        
        for(int i = 0; i < figures.size(); i++)
        {
            board.moveFigure(figures.get(i), board.getFieldNodes().get((i*2) +1));
        }
        System.out.println("NumUnplacedM = " + logic.getUnplacedFigures().size());
        
        
        System.out.println("FWFields = " + logic.getMovableFigures().get(figures.get(figures.size()-1)).size());
        System.out.println();
        
        System.out.println("FWFieldsB = " + logic.getMovableFigures().get(figures.get(figures.size()-1)).size() + " - " + figures.size());
        for(int i = 1; i < MillBoard.FIGURES_PER_TEAM; i++)
        {
            logic.moveFigureToOutside(figures.get(0));
            
            if(logic.getMovableFigures().get(figures.get(0)) != null)
            {
                System.out.println("FWFieldsF = " + logic.getMovableFigures().get(figures.get(0)).size() + " - " + figures.size());
            }
            else
            {
                System.out.println("FWFieldsB = " + logic.getMovableFigures().get(figures.get(figures.size()-1)).size() + " - " + figures.size());
            }
            
            System.out.println("NumTaken = " + board.getTakenTeamFigures(TeamColor.RED).size());
        }
        figures.remove(figures.get(0));
        System.out.println("FWFields = " + logic.getMovableFigures().size() + " - " + figures.size());
        System.out.println();
        
        System.out.println("NumFigures = " + logic.getUnplacedFigures().size());
        
        //logic.millBoard.removeFigureFromTeam(TeamColor.RED);
        //logic.getMillBoard().
        //System.out.println();
    }
    
     */
}
