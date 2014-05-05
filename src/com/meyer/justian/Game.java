package com.meyer.justian;

public class Game
{

    public static void main(String[] args)
    {
        Rack rack = new Rack();
        Player[] players = { null, new MiniMaxPlayer(1), new RandomPlayer() };

        int player;
        int turn = 0;
        
        /*
         * int[][] rackShell = { { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0
         * }, { 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 1,
         * 0, 0 }, { 0, 2, 2, 2, 1, 0, 0 } }; Rack testRack = new
         * Rack(rackShell);
         * 
         * System.out.println(testRack.threat(1, 2));
         * System.out.println(testRack.threat(2, 1));
         */

        while (!rack.gameOver()) {
            player = (turn % 2 == 0) ? 1 : 2;

            rack.place(player, players[player].getMove(rack));
            System.out.println(rack.toString(false));

            turn++;
        }

        System.out.printf("Turn: %d, Winner: %d\n", turn, rack.winner());
    }

}
