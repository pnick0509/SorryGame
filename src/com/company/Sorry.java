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
    private int currCard;
    private boolean firstGo;
    int remainder = 0;

    private int selected = -1;
    private ArrayList<Integer> options;

    //Starts a new game with four players
    public Sorry(){
        gb = new GameBoard();
        rules = new cardMethods(this);
        players = 6;
        turn = -1; //0: Red, 1: Orange, 2: Yellow, 3: Green, 4: Blue, 5: Purple
        cards = new ArrayList<Integer>();
        createCards();
        rand = new Random();
        currCard = pullCard();
        firstGo = true;

        options = new ArrayList<Integer>();
        nextTurn();
    }

    //Starts a new game with a variable amount of players
    //Value should be in the range of 1 and 4
    public Sorry(int num){
        gb = new GameBoard();
        players = num;
        turn = -1;
        cards = new ArrayList<Integer>();
        createCards();
        rand = new Random();
        firstGo = true;
        nextTurn();
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
        int temp = Math.abs(rand.nextInt())%cards.size();
        int card = cards.get(temp);
        cards.remove(temp);

        //Reshuffle cards
        if(cards.size() <= 0){
            createCards();
        }

        return card;
    }

    public int getCard(){
        return currCard;
    }

    //Uses the class cardMethods to handle card choices.
    public boolean useCard(int cardNumber,int button) {
        return(rules.useCard(cardNumber,button));
    }

    //This script ensures that the turn is always set to an active player
    public void nextTurn()
    {
        selected = -1;
        turn = (turn+1)%players;
        System.out.println("Turn: "+turn);
        currCard = pullCard();
        if(currCard == 13){
            options.clear();
            for(int i = 0; i < gb.getMarginCount(); i++){
                if(gb.getSpaces()[i] != null){
                    if(gb.getSpaces()[i].getPawnColor() != getTurnColor()){
                        options.add(i);
                    }
                }
            }
        }
    }

    public void goAgain()
    {
        if(turn==0) {
            turn = players-1;
        }
        else {
            turn--;
        }
        currCard = pullCard();
    }

    //This script takes in the index of a pawn you want to move.
    //This will not work if the space is empty or the color doesn't match
    public void takeTurn(int index){
        /*
        if(gb.getSpaces()[index] != null){
            //Add a check here to make sure pawn isn't wasting their turn if they try to move a pawn that cannot move into home.
            if(gb.getSpaces()[index].getPawnColor() == pColor.values()[turn] || !firstGo){
                if(useCard(currCard,index))
                {
                    valid=true;
                }
                firstGo = false;

            }
        }
        if(valid) {
            nextTurn();
            firstGo=true;
        }
        //System.out.println("Valid? "+valid);
        return valid;*/
        if(currCard != 13){
            if(selected == -1){
                if(gb.getSpaces()[index] != null){
                    if(gb.getSpaces()[index].getPawnColor() == getTurnColor()){
                        selected = index;
                        setOptions(selected,gb.getSpaces()[index].getPawnColor(),currCard);
                        if(options.size() == 0){
                            selected = -1;
                        }

                    }
                }
            }else{
                if(options.contains(index)){
                    //Take turn
                    options.clear();
                    if(currCard != 11){
                        gb.movePawn(selected,index);
                    }else{
                        Pawn temp = gb.getSpaces()[index];
                        gb.movePawn(selected,index);
                        gb.setSpace(selected,temp);
                    }
                    //Next move
                    if(currCard == 7){
                        System.out.println("Remain "+remainder);
                        if(remainder == 0){
                            remainder = 7-gb.distanceBetweenSpaces(selected,index,gb.getSpaces()[index].getPawnColor());
                            if(remainder != 0){
                                selected = -1;
                            }else{
                                nextTurn();
                            }
                        }else{
                            remainder = 0;
                            nextTurn();
                        }
                    }else{
                        nextTurn();
                    }
                }
            }
        }else{
            if(options.contains(index)){
                gb.newPawn(getTurnColor(),index);
                nextTurn();
            }
        }
    }

    public void setOptions(int index, pColor c, int card) {
        options.clear();
        if(card == 1){
            safeAdd(gb.countForward(index, 1, c));
        }else if(card == 2){
            safeAdd(gb.countForward(index, 2, c));
        }else if(card == 3){
            safeAdd(gb.countForward(index, 3, c));
        }else if(card == 4){
            safeAdd(gb.countBackward(index, 4, c));
        }else if(card == 5){
            safeAdd(gb.countForward(index, 5, c));
        }else if(card == 7){
            if(remainder == 0){
                safeAdd(gb.countForward(index, 1, c));
                safeAdd(gb.countForward(index, 2, c));
                safeAdd(gb.countForward(index, 3, c));
                safeAdd(gb.countForward(index, 4, c));
                safeAdd(gb.countForward(index, 5, c));
                safeAdd(gb.countForward(index, 6, c));
                safeAdd(gb.countForward(index, 7, c));
            }else{
                System.out.println(remainder);
                safeAdd(gb.countForward(index, remainder, c));
            }
        }else if(card == 8){
            safeAdd(gb.countForward(index,8,c));
        }else if(card == 10){
            safeAdd(gb.countForward(index,10,c));
            safeAdd(gb.countBackward(index,1,c));
        }else if(card == 11){
            safeAdd(gb.countForward(index,11,c));
            addMargins();
        }else if(card == 12){
            safeAdd(gb.countForward(index,12,c));
        }
        options.removeIf(a -> a.equals(-1));
        options.removeIf(a -> a.equals(-2));
        System.out.println(options);
    }

    public void safeAdd(int i){
        if(i >= 0){
            if(gb.getSpaces()[i] != null){
                if(gb.getSpaces()[i].getPawnColor() != gb.getSpaces()[selected].getPawnColor()){
                    options.add(i);
                }
            }else{
                options.add(i);
            }
        }
    }

    public void addMargins(){
        if(selected < gb.getMarginCount()){
            for(int i = 0; i < gb.getMarginCount(); i++){
                if(gb.getSpaces()[i] != null){
                    if(gb.getSpaces()[i].getPawnColor() != gb.getSpaces()[selected].getPawnColor()){
                        safeAdd(i);
                    }
                }
            }
        }
    }

    public GameBoard getBoard(){
        return gb;
    }

    public pColor getTurnColor(){
        return pColor.values()[turn];
    }

    public int getTurn(){
        return turn;
    }

    public int getSelected(){
        return selected;
    }

    public ArrayList getOptions(){
        return options;
    }
}

//This class handles and maintains the board array(s)
class GameBoard{
    private Pawn[] spaces;
    private int marginCount; //The amount of spaces on the perimeter of the board
    private int safeLength; //The amount of spaces of each safe zone
    private int homeOffset = 7;
    private int homeSpaces = 15;
    private int maxPlayers = 6;

    public Pawn[] getSpaces()
    {
        return spaces;
    }
    //Creates a default gameboard
    public GameBoard(){
        marginCount = 90;
        safeLength = 5;
        //spaces = new Pawn[(sideLength*sides)-sides+safeLength*sides];
        spaces = new Pawn[marginCount+safeLength*maxPlayers];

        //Temp
        newPawn(pColor.RED,0);
        newPawn(pColor.YELLOW,6);
        newPawn(pColor.GREEN,12);
        newPawn(pColor.BLUE,18);
        newPawn(pColor.RED,24);
        newPawn(pColor.YELLOW,30);
        newPawn(pColor.GREEN,36);
        newPawn(pColor.BLUE,40);
        newPawn(pColor.ORANGE,46);
        newPawn(pColor.PURPLE,52);
        newPawn(pColor.ORANGE,58);
        newPawn(pColor.PURPLE,64);

        newPawn(pColor.RED,92);
        newPawn(pColor.ORANGE,97);
        newPawn(pColor.YELLOW,102);
        newPawn(pColor.GREEN,107);
        newPawn(pColor.BLUE,112);
        newPawn(pColor.PURPLE,117);
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
        System.out.println(pre+" --> "+post);
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
            }else if(index != -1){
                movePawn(start, index);
            }
        }else{
            for(int i = 0; i > amount; i--){
                index = lastSpace(index,spaces[start].getPawnColor());
            }
            if(index != -1){
                movePawn(start,index);
            }
        }
    }

    //Finds the distance between two spaces
    public int distanceBetweenSpaces(int startSpace,int endSpace, pColor c)
    {   
        int i =0;
        int curr = startSpace;
        while(curr != endSpace)
        {
            curr=nextSpace(curr, c);
            i++;
        }
        return i;
    }

    //Swaps the position of two pawns
    //NEEDS TO BE UPDATED TO NOT ALLOW SWAPS AT END POSITIONS (not done since this requires knowledge of the new squares)
    public void swapPawn(int startSpace,int endSpace)
    {
        Pawn temp = spaces[endSpace];
        spaces[endSpace]=spaces[startSpace];
        spaces[startSpace]=spaces[endSpace];
    }
    //This takes in an index of a space on the board and the pawn color and returns the next space
    //Exception: If there is no next space, a -1 will be returned.
    public int nextSpace(int curr, pColor c){
        if(curr == -1 || curr == -2){
            return -1;
        }else if(curr == getValue(c)*homeSpaces+homeOffset && curr < marginCount){ //Enter Safe Area
            return marginCount+(getValue(c)*safeLength);
        }else if(curr == marginCount-1){ //Go around the board
            return 0;
        }else if(curr == marginCount+(getValue(c)+1)*safeLength-1){ //Enter Home
            return -2;
        }else if(curr < marginCount){ //Move forward while in margin
            return curr+1;
        }else if(curr >= marginCount && (curr-marginCount)%safeLength < safeLength-1){ //Move forward in safe area
            return curr+1;
        }else{ //Fail state
            System.out.println("Error"+(marginCount+(getValue(c)+1)*safeLength-1));
            return -1;
        }
    }

    //This takes in an index of a space on the board and the pawn color and returns the previous space
    //Exception: If there is no previous space, a -1 will be returned.
    public int lastSpace(int curr, pColor c){
        if(curr >= marginCount && (curr-marginCount)%safeLength == 0){ //Leave safe zone
            return homeOffset+(getValue(c)*homeSpaces);
        }if(curr == 0){ //Go around the board
            return marginCount-1;
        }else if(curr > 0 && curr < marginCount){ //Go backwards while in margin
            return curr-1;
        }else if(curr >= marginCount){
            return curr-1;
        }else{ //Fail state
            return -1;
        }
    }

    public int countForward(int index, int count, pColor c){
        for(int i = 0; i < count; i++){
            index = nextSpace(index,c);
        }
        return index;
    }

    public int countBackward(int index, int count, pColor c){
        for(int i = 0; i < count; i++){
            index = lastSpace(index,c);
        }
        return index;
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

    public int getMarginCount() {
        return marginCount;
    }

    public void setSpace(int i, Pawn p){
        spaces[i] = p;
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