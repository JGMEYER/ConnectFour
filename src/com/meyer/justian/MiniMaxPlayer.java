package com.meyer.justian;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniMaxPlayer implements Player
{

    private static final int MAX_TURN_DEPTH = 4;

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

    // TODO believed to be issue with new form of choosing random options
    private ActionNode minimax(final int action, final Rack rack, final int depth, final boolean maximizing)
    {
        int score = rack.threat(player, enemyPlayer);

        // Terminating conditions
        if (depth >= MAX_TURN_DEPTH || rack.gameOver()) {
            return new ActionNode(action, score, depth);
        }

        Random rand = new Random();
        int actionIndex;
        List<ActionNode> bestActions = new ArrayList<ActionNode>();

        if (maximizing) {
            int bestScore = Integer.MIN_VALUE;

            // Attempt to maximize player's score
            for (Integer move : rack.possibleMoves()) {
                Rack newRack = rack.clone();
                newRack.place(player, move);
                ActionNode an = minimax(move, newRack, depth, false);

                // Select the best action
                if (an.score() > bestScore) {
                    bestActions.clear();
                    bestActions.add(new ActionNode(move, an.score(), an.turns()));
                    bestScore = an.score();
                }
                // Or add to a list of possible actions
                else if (an.score() == bestScore) {
                    if (bestActions.isEmpty()) { // First action
                        bestActions.add(new ActionNode(move, an.score(), an.turns()));
                    }
                    else if (an.compareTo(bestActions.get(0)) < 0) { // Action completed in fewer turns
                        bestActions.clear();
                        bestActions.add(new ActionNode(move, an.score(), an.turns()));
                    }
                    else if (an.compareTo(bestActions.get(0)) == 0) { // Actions are equal
                        bestActions.add(new ActionNode(move, an.score(), an.turns()));
                    }
                }
            }
        }
        else {
            int worstScore = Integer.MAX_VALUE;

            // Attempt to minimize player's score
            for (Integer move : rack.possibleMoves()) {
                Rack newRack = rack.clone();
                newRack.place(enemyPlayer, move);
                ActionNode an = minimax(move, newRack, depth + 1, true);

                // Select the best action
                if (an.score() < worstScore) {
                    bestActions.clear();
                    bestActions.add(new ActionNode(move, an.score(), an.turns()));
                    worstScore = an.score();
                }
                // Or add to a list of possible actions
                else if (an.score() == worstScore) {
                    if (bestActions.isEmpty()) { // First action
                        bestActions.add(new ActionNode(move, an.score(), an.turns()));
                    }
                    else if (an.compareTo(bestActions.get(0)) < 0) { // Action completed in fewer turns
                        bestActions.clear();
                        bestActions.add(new ActionNode(move, an.score(), an.turns()));
                    }
                    else if (an.compareTo(bestActions.get(0)) == 0) { // Actions are equal
                        bestActions.add(new ActionNode(move, an.score(), an.turns()));
                    }
                }
            }
        }

        actionIndex = rand.nextInt(bestActions.size());

        return bestActions.get(actionIndex);
    }
    
    // TODO fix minimax
    /*private ActionNode minimax(final int action, final Rack rack, final int depth, final boolean maximizing, int alpha, int beta)
    {
        int score = rack.threat(player, enemyPlayer);

        // Terminating conditions
        if (depth >= MAX_TURN_DEPTH || rack.gameOver() || beta <= alpha) {
            return new ActionNode(action, score, depth);
        }

        Random rand = new Random();
        int actionIndex;
        List<ActionNode> bestActions = new ArrayList<ActionNode>();

        if (maximizing) {
            // Attempt to maximize player's score
            for (Integer move : rack.possibleMoves()) {
                Rack newRack = rack.clone();
                newRack.place(player, move);
                ActionNode an = minimax(move, newRack, depth, false, alpha, beta);

                // Select the best action
                if (an.score() > alpha) {
                    bestActions.clear();
                    alpha = an.score();
                    bestActions.add(new ActionNode(move, alpha, an.turns()));
                }
                // Or add to a list of possible actions
                else if (an.score() == alpha) {
                    if (bestActions.isEmpty()) { // First action
                        bestActions.add(new ActionNode(move, alpha, an.turns()));
                    }
                    else if (an.compareTo(bestActions.get(0)) < 0) { // Action completed in fewer turns
                        bestActions.clear();
                        bestActions.add(new ActionNode(move, alpha, an.turns()));
                    }
                    else if (an.compareTo(bestActions.get(0)) == 0) { // Actions are equal
                        bestActions.add(new ActionNode(move, alpha, an.turns()));
                    }
                }
                
                if (beta <= alpha) {
                    break;
                }
            }
        }
        else {
            // Attempt to minimize player's score
            for (Integer move : rack.possibleMoves()) {
                Rack newRack = rack.clone();
                newRack.place(enemyPlayer, move);
                ActionNode an = minimax(move, newRack, depth + 1, true, alpha, beta);

                // Select the best action
                if (an.score() < beta) {
                    bestActions.clear();
                    beta = an.score();
                    bestActions.add(new ActionNode(move, beta, an.turns()));
                }
                // Or add to a list of possible actions
                else if (an.score() == beta) {
                    if (bestActions.isEmpty()) { // First action
                        bestActions.add(new ActionNode(move, beta, an.turns()));
                    }
                    else if (an.compareTo(bestActions.get(0)) < 0) { // Action completed in fewer turns
                        bestActions.clear();
                        bestActions.add(new ActionNode(move, beta, an.turns()));
                    }
                    else if (an.compareTo(bestActions.get(0)) == 0) { // Actions are equal
                        bestActions.add(new ActionNode(move, beta, an.turns()));
                    }
                }
                
                if (beta <= alpha) {
                    break;
                }
            }
        }

        actionIndex = rand.nextInt(bestActions.size());

        return bestActions.get(actionIndex);
    }*/

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
