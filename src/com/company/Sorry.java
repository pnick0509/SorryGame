package com.company;
import java.util.ArrayList;
import java.util.Random;

//Run this class to start a new game of Sorry!
class Sorry{
    private GameBoard gb;
    private int players;
    private int turn;
    private ArrayList<Integer> cards;
    private Random rand;
    private int currCard;
    int remainder = 0;

    private int selected = -1;
    private ArrayList<Integer> options;

    private boolean cardCheats;

    //Starts a new game with four players
    public Sorry(){
        gb = new GameBoard();
        players = 6;
        turn = -1; //0: Red, 1: Orange, 2: Yellow, 3: Green, 4: Blue, 5: Purple
        cards = new ArrayList<Integer>();
        createCards();
        rand = new Random();
        currCard = pullCard();

        options = new ArrayList<Integer>();
        nextTurn();

        cardCheats = true;
    }

    //Starts a new game with a variable amount of players
    //Value should be in the range of 1 and 4
    public Sorry(int num){
        gb = new GameBoard();
        players = num;
        turn = -1; //0: Red, 1: Orange, 2: Yellow, 3: Green, 4: Blue, 5: Purple
        cards = new ArrayList<Integer>();
        createCards();
        rand = new Random();
        currCard = pullCard();

        options = new ArrayList<Integer>();
        nextTurn();

        cardCheats = true;
    }

    public void createCards()
    {
        /*for(int y=1; y<=13; y++) {
            if(y==6||y==9){
                y++;
            }
            for (int x=0; x<5; x++) {
                cards.add(y);
            }
        }
        cards.add(1);*/
        cards.add(1);
    }

    //Remove a random card from the deck
    public int pullCard()
    {
        int temp = Math.abs(rand.nextInt())%cards.size();
        int card = cards.get(temp);
        cards.remove(temp);

        //Reshuffle cards
        if(cards.size() <= 0 || !cardCheats){
            System.out.println("Reshuffling");
            createCards();
        }

        return card;
    }

    //Get the card that is currently set to currCard
    public int getCard(){
        return currCard;
    }

    //This script ensures that the turn is always set to an active player
    public void nextTurn()
    {
        selected = -1;
        turn = (turn+1)%players;
        System.out.println("Turn: "+turn);
        currCard = pullCard();
        //For the Sorry! card you don't have to select a pawn
        if(currCard == 13){
            options.clear();
            for(int i = 0; i < gb.getMarginCount(); i++){
                if(gb.getSpaces()[i] != null){
                    if(gb.getSpaces()[i].getPawnColor() != getTurnColor()){
                        options.add(i);
                    }
                }
            }
        }else if(currCard == 1){
            options.clear();
            safeAdd(gb.myStart(getTurn()));
        }
    }

    //This script takes in the index of a pawn you want to move.
    //This will not work if the space is empty or the color doesn't match
    public void takeTurn(int index){
        if(index < gb.getSpaces().length && index >= 0){
            if(currCard != 13){ //For every card but the Sorry! card
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
                            //Swap
                            Pawn temp = gb.getSpaces()[index];
                            gb.movePawn(selected,index);
                            gb.setSpace(selected,temp);
                        }
                        //Next move
                        if(currCard == 7){
                            System.out.println("Remain "+remainder);
                            if(remainder == 0){ //First move with 7
                                remainder = 7-gb.distanceBetweenSpaces(selected,index,gb.getSpaces()[index].getPawnColor());
                                if(remainder != 0){
                                    selected = -1;
                                }else{
                                    nextTurn();
                                }
                            }else{ //Second move with 7
                                remainder = 0;
                                nextTurn();
                            }
                        }else if(currCard == 2){
                            //Go again
                            System.out.println("Go Again");
                            turn--;
                            nextTurn();
                        }else{
                            nextTurn();
                        }
                    }
                }
            }else{ //Sorry! Card
                if(options.contains(index)){
                    gb.newPawn(getTurnColor(),index);
                    options.clear();
                    nextTurn();
                }
            }
        }else{ //Home and start
            if(options.contains(index)){
                if(index >= 120 && index <= 125 && index == gb.myStart(getTurn())){ //Start
                    /*switch(getTurn()){
                        case 120:
                            gb.newPawn(getTurnColor(),9);
                            break;
                        case 121:
                            gb.newPawn(getTurnColor(),24);
                            break;
                        case 122:
                            gb.newPawn(getTurnColor(),39);
                            break;
                        case 123:
                            gb.newPawn(getTurnColor(),54);
                            break;
                        case 124:
                            gb.newPawn(getTurnColor(),69);
                            break;
                        case 125:
                            gb.newPawn(getTurnColor(),84);
                            break;
                    }*/
                    gb.newPawn(getTurnColor(),gb.mySpawn(getTurn()));
                    nextTurn();
                }else if(index >= 126 && index <= 131){ //Home
                    gb.destroyPawn(selected);

                }
            }
        }
    }

    //Clears the options array list and sets it with all the available spaces to move
    public void setOptions(int index, pColor c, int card) {
        options.clear();
        if(card == 1){
            safeAdd(gb.countForward(index, 1, c));
        }else if(card == 2){
            safeAdd(gb.countForward(index, 2, c));
            safeAdd(gb.myStart(getTurn()));
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

    //Ensures that the pawn movement would be valid. For example, it cannot move onto a pawn of the same color or a negative space.
    public void safeAdd(int i){
        System.out.println("Safe "+i);
        if(i >= 0 /*&& i < gb.getSpaces().length*/){
            if(i < gb.getSpaces().length){
                if(gb.getSpaces()[i] != null){
                    if(gb.getSpaces()[i].getPawnColor() != gb.getSpaces()[selected].getPawnColor()){
                        options.add(i);
                    }
                }else{
                    options.add(i);
                }
            }else{
                options.add(i);
            }
        }
    }

    //Add all the pawns around the side of the board of different colors.
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
    private int[] start;
    private int[] home;

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

        start = new int[maxPlayers];
        home = new int[maxPlayers];
        for(int i = 0; i < maxPlayers; i++){
            start[i] = i;
            home[i] = i;
        }

        //Temp
        newPawn(pColor.RED,7);
        newPawn(pColor.RED,8);
        newPawn(pColor.RED,9);

        newPawn(pColor.ORANGE,22);
        newPawn(pColor.ORANGE,23);
        newPawn(pColor.ORANGE,24);

        newPawn(pColor.YELLOW,37);
        newPawn(pColor.YELLOW,38);
        newPawn(pColor.YELLOW,39);

        newPawn(pColor.GREEN,52);
        newPawn(pColor.GREEN,53);
        newPawn(pColor.GREEN,54);

        newPawn(pColor.BLUE,67);
        newPawn(pColor.BLUE,68);
        newPawn(pColor.BLUE,69);

        newPawn(pColor.PURPLE,82);
        newPawn(pColor.PURPLE,83);
        newPawn(pColor.PURPLE,84);
    }

    public void startAdd(int player){
        start[player]++;
    }

    public void startSubtract(int player){
        start[player]--;
    }

    public void homeAdd(int player){
        home[player]++;
    }

    public int startAmount(int player){
        return start[player];
    }

    public int homeAmount(int player){
        return home[player];
    }

    public int myStart(int player){
        return 120+player;
    }

    public int myHome(int player){
        return 126+player;
    }

    public int mySpawn(int player){
        return homeOffset+2+player*homeSpaces;
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

    public void destroyPawn(int index){
        spaces[index] = null;
    }

    //Do not confuse with movePawn. This moves a pawn a set number of spaces.
    //Takes a pre-existing pawn from an index and moves it a certain number of spaces forward or backwards
    //Calls movePawn to change position
    /*public void advancePawn(int index, int amount){
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
    }*/

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
    /*public void swapPawn(int startSpace,int endSpace)
    {
        Pawn temp = spaces[endSpace];
        spaces[endSpace]=spaces[startSpace];
        spaces[startSpace]=spaces[endSpace];
    }*/

    //This takes in an index of a space on the board and the pawn color and returns the next space
    //Exception: If there is no next space, a -1 will be returned.
    public int nextSpace(int curr, pColor c){
        if(curr == -1 || curr == -2 || curr >= spaces.length){
            return -1;
        }else if(curr == getValue(c)*homeSpaces+homeOffset && curr < marginCount){ //Enter Safe Area
            return marginCount+(getValue(c)*safeLength);
        }else if(curr == marginCount-1){ //Go around the board
            return 0;
        }else if(curr == marginCount+(getValue(c)+1)*safeLength-1){ //Enter Home
            if(curr == 94){
                return 126;
            }else if(curr == 99){
                return 127;
            }else if(curr == 104){
                return 128;
            }else if(curr == 109){
                return 129;
            }else if(curr == 114){
                return 130;
            }else if(curr == 119){
                return 131;
            }else{
                return -1;
            }
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