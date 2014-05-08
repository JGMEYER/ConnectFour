package com.meyer.justian;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer implements Player
{

    private Scanner scan;
    
    public HumanPlayer() {
        scan = new Scanner(System.in);
    }

    @Override
    public int getMove(Rack rack)
    {
        List<Integer> moves = rack.possibleMoves();
        int playerMove = -1;
        
        // Display column numbers
        for (int c = 0; c < rack.width(); c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        
        // Continues to prompt user until valid number given
        while (!moves.contains(playerMove)) {
            System.out.print("Move: ");
            playerMove = scan.nextInt();
        }
        
        System.out.println();
        
        return playerMove;
    }
    
}
