package com.meyer.justian;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player
{

    @Override
    public int getMove(Rack rack)
    {
        List<Integer> moves = rack.possibleMoves();
        Random rand = new Random();
        int index = rand.nextInt(moves.size());

        return moves.get(index);
    }
    
    @Override
    public boolean isHuman()
    {
        return false;
    }

}
