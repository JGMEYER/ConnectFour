package com.meyer.justian;

public interface Player
{

    /*
     * Returns the column that the player wishes to move to. Column returned must be a valid move.
     */
    public int getMove(Rack rack);

}
