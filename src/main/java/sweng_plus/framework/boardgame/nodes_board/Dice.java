package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.IDice;

import java.util.Random;
import java.util.stream.IntStream;

public class Dice<T> implements IDice<T>
{
    public static final Dice<Integer> D6 = new Dice<>(IntStream.range(1, 7).boxed().toArray(Integer[]::new));
    public static final Dice<Integer> D8 = new Dice<>(IntStream.range(1, 9).boxed().toArray(Integer[]::new));
    public static final Dice<Integer> D20 = new Dice<>(IntStream.range(1, 21).boxed().toArray(Integer[]::new));
    public static final Dice<Integer> D100 = new Dice<>(IntStream.range(1, 101).boxed().toArray(Integer[]::new));
    
    private T[] eyes;
    private Random random;
    
    public Dice(T[] eyes)
    {
        this.eyes = eyes;
        random = new Random(27);
    }
    
    @Override
    public T roll()
    {
        return eyes[selectedIndex()];
    }
    
    private int selectedIndex()
    {
        return random.nextInt(eyes.length);
    }
}
