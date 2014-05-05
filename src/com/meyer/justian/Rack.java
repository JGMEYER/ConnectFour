package com.meyer.justian;

import java.util.ArrayList;
import java.util.List;

public class Rack
{

    // DO NOT MODIFY
    private static final int PLAYER       = 0;
    private static final int TOP_LEFT     = 1;
    private static final int TOP          = 2;
    private static final int TOP_RIGHT    = 3;
    private static final int LEFT         = 4;
    private static final int RIGHT        = 5;
    private static final int BOTTOM_LEFT  = 6;          // unused
    private static final int BOTTOM       = 7;          // unused
    private static final int BOTTOM_RIGHT = 8;          // unused
    private static final int SCORE_TO_WIN = 4;
    private static final int NULL_PLAYER  = 0;

    private int              winner       = NULL_PLAYER;
    private int[][][]        rack;

    // MODIFIABLE
    private static final int HEIGHT       = 6;
    private static final int WIDTH        = 7;

    public Rack()
    {
        this(new int[HEIGHT][WIDTH][9]);
    }

    public Rack(int[][] rackShell)
    {
        this.rack = new int[HEIGHT][WIDTH][9];

        for (int r = 0; r < rack.length; r++) {
            for (int c = 0; c < rack[0].length; c++) {
                rack[r][c][PLAYER] = rackShell[r][c];
            }
        }

        update();
    }

    private Rack(int[][][] rack)
    {
        this.rack = rack;
        update();
    }

    /*
     * Returns a shallow copy of the rack's inner array.
     */
    public int[][][] arrayCopy()
    {
        int[][][] copy = new int[HEIGHT][WIDTH][9];

        for (int r = 0; r < rack.length; r++) {
            for (int c = 0; c < rack[0].length; c++) {
                for (int a = 0; a < rack[0][0].length; a++) {
                    copy[r][c][a] = rack[r][c][a];
                }
            }
        }

        return copy;
    }

    /*
     * Returns a string with all a chip's attributes.
     */
    public String chipToString(int r, int c)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int a = 0; a < rack[0][0].length; a++) {
            sb.append(rack[r][c][a] + " ");
        }

        sb.append("}");
        return sb.toString();
    }

    /*
     * Creates a shallow copy of the rack. Used primarily for modeling and
     * simulation.
     * 
     * @see java.lang.Object#clone()
     */
    public Rack clone()
    {
        return new Rack(arrayCopy());
    }

    /*
     * Returns true if the game has ended.
     */
    public boolean gameOver()
    {
        return (winner != NULL_PLAYER || noMoreMoves());
    }

    /*
     * Returns true if a move is possible at the given column.
     */
    public boolean movePossible(int col)
    {
        return rack[0][col][PLAYER] == NULL_PLAYER;
    }

    /*
     * Returns true if no more moves are available to either player.
     */
    private boolean noMoreMoves()
    {
        return possibleMoves().size() == 0;
    }

    /*
     * Returns true if a location is occupied.
     */
    private boolean occupied(int r, int c)
    {
        return (rack[r][c][PLAYER] != NULL_PLAYER);
    }

    /*
     * Adds a player's chip to the specified column and returns whether
     * placement was successful.
     */
    public boolean place(int player, int col)
    {
        if (!gameOver()) {
            for (int r = rack.length - 1; r >= 0; r--) {
                if (!occupied(r, col)) {
                    rack[r][col][PLAYER] = player;
                    update();
                    return true;
                }
            }
        }

        return false;
    }

    /*
     * Returns a list of all the available moves on the rack.
     */
    public List<Integer> possibleMoves()
    {
        ArrayList<Integer> moves = new ArrayList<Integer>();

        for (int c = 0; c < rack[0].length; c++) {
            if (movePossible(c)) {
                moves.add(c);
            }
        }

        return moves;
    }

    /*
     * Returns p1's threat level. TODO
     */
    public int threat(int p1, int p2)
    {
        int threat1 = 0;
        int threat2 = 0;

        if (gameOver()) {
            if (winner == p1) {
                threat1 = Integer.MAX_VALUE;
            }
            else if (winner == p2) {
                threat2 = Integer.MAX_VALUE;
            }
        }
        else {
            for (int r = 0; r < rack.length; r++) {
                for (int c = 0; c < rack[0].length; c++) {
                    if (rack[r][c][PLAYER] == p1) {
                        for (int a = 1; a < rack[0][0].length; a++) {
                            threat1 += rack[r][c][a];
                        }
                    }
                    else if (rack[r][c][PLAYER] == p2) {
                        for (int a = 1; a < rack[0][0].length; a++) {
                            threat2 += rack[r][c][a];
                        }
                    }
                }
            }
        }

        return threat1 - threat2;
    }

    /*
     * Returns a string representation of the board that shows each player's
     * positions.
     */
    public String toString()
    {
        return toString(false);
    }

    /*
     * Returns a string representation of the board that shows each player's
     * positions with additional debug data if requested.
     */
    public String toString(boolean debug)
    {
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < rack.length; r++) {
            for (int c = 0; c < rack[0].length; c++) {
                if (debug) {
                    sb.append(chipToString(r, c) + " ");
                }
                else {
                    sb.append(rack[r][c][PLAYER] + " ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /*
     * Updates all chips' neighbor values.
     */
    public void update()
    {
        for (int r = 0; r < rack.length; r++) {
            for (int c = 0; c < rack[0].length; c++) {
                updateAttrib(r, c, r - 1, c - 1, TOP_LEFT);
                updateAttrib(r, c, r - 1, c, TOP);
                updateAttrib(r, c, r, c - 1, LEFT);
            }
        }
        for (int r = rack.length - 1; r >= 0; r--) {
            for (int c = 0; c < rack[0].length; c++) {
                updateAttrib(r, c, r - 1, c + 1, TOP_RIGHT);
                updateAttrib(r, c, r, c + 1, RIGHT);
            }
        }
    }

    /*
     * Increments a given chip's neighbor value, given that its neighbor at that
     * location is of the same PLAYER.
     */
    private void updateAttrib(int r1, int c1, int r2, int c2, int neighbor)
    {
        // r1, c1 within bounds
        if (r1 >= 0 && r1 < rack.length && c1 >= 0 && c1 < rack[0].length) {

            if (rack[r1][c1][PLAYER] == NULL_PLAYER) {
                rack[r1][c1][neighbor] = 0;
                return;
            }

            // r2, c2 within bounds
            if (r2 >= 0 && r2 < rack.length && c2 >= 0 && c2 < rack[0].length) {

                if (rack[r1][c1][PLAYER] == rack[r2][c2][PLAYER]) {
                    rack[r1][c1][neighbor] = rack[r2][c2][neighbor] + 1;

                    // If enough are connected, select a winner
                    if (rack[r1][c1][neighbor] >= SCORE_TO_WIN) {
                        winner = rack[r1][c1][PLAYER];
                    }

                }
                else {
                    // No match, start over
                    rack[r1][c1][neighbor] = 1;
                }
            }
            else {
                // Neighbor invalid, assume on edge of rack
                rack[r1][c1][neighbor] = 1;
            }
        }
    }

    /*
     * Returns the current game winner.
     */
    public int winner()
    {
        return winner;
    }

}
