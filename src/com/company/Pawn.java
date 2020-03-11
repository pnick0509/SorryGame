package com.company;

//A basic pawn who holds a color
class Pawn{
    private pColor pawnColor;

    //Basic Pawn constructor that assigns it with a user defined color
    public Pawn(pColor c){
        pawnColor = c;
    }
    public pColor getPawnColor()
    {
        return pawnColor;
    }
}

//The color enum for 4 different Sorry! players.
//The players will always move in this turn order.
enum pColor{
    RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE;
}