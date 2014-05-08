package com.meyer.justian;

public class Game
{

    public static void main(String[] args)
    {
        Rack rack = new Rack();
        //Player[] players = { null, new RandomPlayer(), new RandomPlayer() };
        Player[] players = { null, new HumanPlayer(), new MiniMaxPlayer(2) };

        int player;
        int turn = 0;

        while (!rack.gameOver()) {
            player = (turn % 2 == 0) ? 1 : 2;

            rack.place(player, players[player].getMove(rack));
            System.out.println(rack.toString(false));
            //System.out.println(rack.toString(true));

            turn++;
        }

        System.out.printf("Turn: %d, Winner: %d\n", turn, rack.winner());
    }

}
