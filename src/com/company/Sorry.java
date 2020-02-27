package com.company;
import java.util.ArrayList;
import java.util.Random;
//Run this class to start a new game of Sorry!
class Sorry{
    private GameBoard gb;
    private int players;
    private int turn;
    private cardMethods rules;
    private ArrayList<Integer> cards;
    private Random rand;
    private int prevButton=-1;
    //Starts a new game with four players
    public Sorry(){
        gb = new GameBoard();
        rules = new cardMethods(this);
        players = 6;
        turn = 0; //0: Red, 1: Orange, 2: Yellow, 3: Green, 4: Blue, 5: Purple
        cards = new ArrayList<Integer>();
        createCards();
        rand = new Random();
    }

    //Starts a new game with a variable amount of players
    //Value should be in the range of 1 and 4
    public Sorry(int num){
        gb = new GameBoard();
        players = num;
        turn = 0;
        cards = new ArrayList<Integer>();
        createCards();
        rand = new Random();
    }
    public void createCards()
    {
        for(int y=1; y<=13; y++)
        {
            if(y==6||y==9)
            {
                y++;
            }

            for (int x=0; x<5; x++)
            {
                cards.add(y);
            }
        }
        cards.add(1);
    }
    
    public int pullCard()
    {
        int temp= rand.nextInt()%cards.size();
        int card = cards.get(temp);
        cards.remove(temp);
        return card;
    }
/*
Uses the class cardMethods to handle card choices.
*/ 
public boolean useCard(int cardNumber,int button)
{
    return(rules.useCard(cardNumber,button));
}
    //This script ensures that the turn is always set to an active player
    public void nextTurn()
    {
        turn = (turn+1)%players;
    }
    public void goAgain()
    {
        if(turn==0)
        {
            turn = players-1;
        }
        else 
        {
            turn--;
        }
    
    }

    //This script takes in the index of a pawn you want to move.
    //This will not work if the space is empty or the color doesn't match
    public boolean takeTurn(int click){
        boolean valid = false;
        int index = click;
        if(gb.getSpaces()[index] != null){
            if(gb.getSpaces()[index].getPawnColor() == pColor.values()[turn]){
                gb.advancePawn(index, 1);
                valid=true;
            }
        }
        if(valid)
        {
            nextTurn();
        }
        //System.out.println("Valid? "+valid);
        return valid;
    }

    public GameBoard getBoard(){
        return gb;
    }
}

//This class handles and maintains the board array(s)
class GameBoard{
    private Pawn[] spaces;
    private int marginCount; //The amount of spaces on the perimeter of the board
    private int safeLength; //The amount of spaces of each safe zone

    public Pawn[] getSpaces()
    {
        return spaces;
    }
    //Creates a default gameboard
    public GameBoard(){
        marginCount = 90;
        safeLength = 5;
        //spaces = new Pawn[(sideLength*sides)-sides+safeLength*sides];
        spaces = new Pawn[marginCount+safeLength*6];

        //Temp
        newPawn(pColor.RED,0);
        newPawn(pColor.YELLOW,6);
        newPawn(pColor.GREEN,12);
        newPawn(pColor.BLUE,18);
        newPawn(pColor.RED,24);
        newPawn(pColor.YELLOW,30);
        newPawn(pColor.GREEN,36);
        newPawn(pColor.BLUE,40);
    }

    //Creates a new pawn and places it somewhere on the board
    public void newPawn(pColor c, int index){
        spaces[index] = new Pawn(c);
    }

    //Do not confuse with advancePawn. This moves a pawn to a set index.
    //Takes a pre-existing pawn from and index and moves it to a new index on the array.
    //This will make the space it used to occupy null and will knock out any pawn at the new position
    public void movePawn(int pre, int post){
        spaces[post] = spaces[pre];
        spaces[pre] = null;
        //System.out.println(pre+" --> "+post);
    }

    //Do not confuse with movePawn. This moves a pawn a set number of spaces.
    //Takes a pre-existing pawn from an index and moves it a certain number of spaces forward or backwards
    //Calls movePawn to change position
    public void advancePawn(int index, int amount){
        int start = index;
        if(amount >= 0){
            for(int i = 0; i < amount; i++){
                index = nextSpace(index,spaces[start].getPawnColor());
            }
            if(index == -2){
                spaces[start] = null; //Pawn in home
            }else {
                movePawn(start, index);
            }
        }else{
            for(int i = 0; i > amount; i--){
                index = lastSpace(index,spaces[start].getPawnColor());
            }
            movePawn(start,index);
        }
    }
    /*
    Finds the distance between two spaces
    */
    public int distanceBetweenSpaces(int startSpace,int endSpace, pColor c)
    {   
        int i =0;
        while(nextSpace(startSpace,c)<endSpace)
        {
            i++;
        }
        return i;
    }
    /*
    Swaps the position of two pawns
    NEEDS TO BE UPDATED TO NOT ALLOW SWAPS AT END POSITIONS (not done since this requires knowledge of the new squares)
    */
    public void SwapPawn(int startSpace,int endSpace)
    {
        Pawn temp = spaces[endSpace];
        spaces[endSpace]=spaces[startSpace];
        spaces[startSpace]=spaces[endSpace];
    }
    //This takes in an index of a space on the board and the pawn color and returns the next space
    //Exception: If there is no next space, a -1 will be returned.
    public int nextSpace(int curr, pColor c){
        if(curr == 2 && c == pColor.RED){
            return 60;
        }else if(curr == 17 && c == pColor.YELLOW){
            return 65;
        }else if(curr == 32 && c == pColor.GREEN){
            return 70;
        }else if(curr == 47 && c == pColor.BLUE){
            return 75;
        }else if(curr >= 60 && curr <= 63){ //Final space on runway not counted because there is no next space
            return curr+1;
        }else if(curr >= 65 && curr <= 68){
            return curr+1;
        }else if(curr >= 70 && curr <= 73){
            return curr+1;
        }else if(curr >= 75 && curr <= 78){
            return curr+1;
        }else if(curr >= 0 && curr <= 58){
            return curr+1;
        }else if(curr == 59){ //Special case to go around the board
            return 0;
        }else if(curr == 64 || curr == 69 || curr == 74 || curr == 79){ //Sentinel for home
            return -2;
        }else{
            return -1;
        }
    }

    //This takes in an index of a space on the board and the pawn color and returns the previous space
    //Exception: If there is no previous space, a -1 will be returned.
    public int lastSpace(int curr, pColor c){
        if(curr == 60){
            return 2;
        }else if(curr == 65){
            return 17;
        }else if(curr == 70){
            return 32;
        }else if(curr == 75){
            return 47;
        }else if(curr >= 61 && curr <= 64){
            return curr-1;
        }else if(curr >= 66 && curr <= 69){
            return curr-1;
        }else if(curr >= 71 && curr <= 74){
            return curr-1;
        }else if(curr >= 76 && curr <= 79){
            return curr-1;
        }else if(curr >= 1 && curr <= 59){
            return curr-1;
        }else if(curr == 0){
            return 59;
        }else{
            return -1;
        }
    }
    public int getValue(pColor k)
    {
        if(k==pColor.RED){
            return 0;
        }else if(k==pColor.ORANGE){
            return 1;
        }else if(k==pColor.YELLOW){
            return 2;
        }else if(k==pColor.GREEN){
            return 3;
        }else if(k==pColor.BLUE) {
            return 4;
        }else if(k==pColor.PURPLE){
            return 5;
        }else{
            return -1;
        }
    }
}

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