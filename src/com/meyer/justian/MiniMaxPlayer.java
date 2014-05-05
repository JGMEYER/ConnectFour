package com.meyer.justian;

import java.util.List;

public class MiniMaxPlayer implements Player
{

    private static final int MAX_DEPTH = 5;

    private int              player;
    private int              enemyPlayer;

    public MiniMaxPlayer(int player)
    {
        this.player = player;
        enemyPlayer = (player == 1) ? 2 : 1;
    }

    public int getMove(Rack rack)
    {
        ActionNode an = minimax(-1, rack, 0, true);

        return an.action();
    }

    public ActionNode minimax(final int action, final Rack rack, final int depth, final boolean maximizing)
    {
        /*int score = rack.threatDifference(player, enemyPlayer);

        if (depth >= MAX_DEPTH || rack.gameOver()) {
            return new ActionNode(action, score);
        }

        if (maximizing) {
            int bestValue = Integer.MIN_VALUE;
            List<Integer> moves = rack.possibleMoves();
            
            for (Integer move : moves) {
                Rack newRack = rack.clone();
                newRack.place(player, move);
                ActionNode an = minimax();
            }
        }
        else {
            int bestValue = Integer.MAX_VALUE;
            List<Integer> moves = rack.possibleMoves();
            
            for (Integer move : moves) {
                
            }
        }*/

        return null;
    }

    private class ActionNode
    {
        private final int action;
        private final int score;

        public ActionNode(int action, int score)
        {
            this.action = action;
            this.score = score;
        }

        public int action()
        {
            return action;
        }

        public int score()
        {
            return score;
        }
    }

}
