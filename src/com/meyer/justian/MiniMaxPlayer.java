package com.meyer.justian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MiniMaxPlayer implements Player
{

    private static final int MAX_TURN_DEPTH = 3;

    private int              player;
    private int              enemyPlayer;

    public MiniMaxPlayer(int player)
    {
        this.player = player;
        enemyPlayer = (player == 1) ? 2 : 1;
    }

    public int getMove(Rack rack)
    {
        ActionNode an = minimax(rack);

        return an.action();
    }

    private ActionNode minimax(final Rack rack)
    {
        return minimax(-1, rack, 0, true);
    }

    // TODO choose between actions of same weight
    private ActionNode minimax(final int action, final Rack rack, final int depth, final boolean maximizing)
    {
        int score = rack.threat(player, enemyPlayer);

        if (depth >= MAX_TURN_DEPTH || rack.gameOver()) {
            return new ActionNode(action, score, depth);
        }

        ActionNode bestAction = null;

        if (maximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (Integer move : rack.possibleMoves()) {
                Rack newRack = rack.clone();
                newRack.place(player, move);
                ActionNode an = minimax(move, newRack, depth, false);

                if (an.score() > bestScore) {
                    bestAction = new ActionNode(move, an.score(), an.turns());
                    bestScore = an.score();
                }
                else if (an.score() == bestScore) {
                    if (bestAction == null || an.compareTo(bestAction) < 0) {
                        bestAction = new ActionNode(move, an.score(), an.turns());
                    }
                }
            }
        }
        else {
            int worstScore = Integer.MAX_VALUE;

            for (Integer move : rack.possibleMoves()) {
                Rack newRack = rack.clone();
                newRack.place(enemyPlayer, move);
                ActionNode an = minimax(move, newRack, depth + 1, true);

                if (an.score() < worstScore) {
                    bestAction = new ActionNode(move, an.score(), an.turns());
                    worstScore = an.score();
                }
                else if (an.score() == worstScore) {
                    if (bestAction == null || an.compareTo(bestAction) < 0) {
                        bestAction = new ActionNode(move, an.score(), an.turns());
                    }
                }
            }
        }

        // System.out.printf("Depth: %d, Actions: %s\n", depth, bestAction);

        return bestAction;
    }

    private class ActionNode implements Comparable<ActionNode>
    {
        private final int action;
        private final int score;
        private final int turns;

        public ActionNode(int action, int score, int turns)
        {
            this.action = action;
            this.score = score;
            this.turns = turns;
        }

        public int action()
        {
            return action;
        }

        public int score()
        {
            return score;
        }

        public int turns()
        {
            return turns;
        }

        public String toString()
        {
            return "[Action: " + action + ", Score: " + score + ", Turns: " + turns + "]";
        }

        @Override
        public int compareTo(ActionNode that)
        {
            return this.turns - that.turns();
        }
    }

}
