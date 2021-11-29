package sweng_plus.framework_test.boardgame;

import org.junit.jupiter.api.Test;
import sweng_plus.framework.boardgame.nodes_board.Dice;


public class DiceTest
{
    @Test
    void testRollD6()
    {
        Dice<Integer> d6 = Dice.D6;
        int[] numbers = new int[6];
        
        for(int i = 0; i < 1000000; i++)
        {
            numbers[d6.roll() - 1]++;
        }
        
        for(int i = 0; i < numbers.length; i++)
        {
            System.out.println(i + ": " + numbers[i]);
        }
    }
}
