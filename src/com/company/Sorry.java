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
    private boolean colorBlind;

    //Starts a new game with six players
    public Sorry(){
        gb = new GameBoard();
        players = 1;
        turn = -1; //0: Red, 1: Orange, 2: Yellow, 3: Green, 4: Blue, 5: Purple
        cards = new ArrayList<Integer>();
        createCards();
        rand = new Random();
        currCard = pullCard();

        options = new ArrayList<Integer>();
        nextTurn();

        cardCheats = true;
        colorBlind = true;
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
        colorBlind = true;
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
        }*/
        cards.add(1);
        cards.add(3);
        cards.add(2);
        cards.add(7);
    }

    //Remove a random card from the deck
    public int pullCard()
    {
        int temp = Math.abs(rand.nextInt())%cards.size();
        int card = cards.get(temp);
        if(cardCheats){
            cards.remove(temp);

            //Reshuffle cards
            if(cards.size() <= 0){
                System.out.println("Reshuffling");
                createCards();
            }
        }

        return card;
    }

    //Get the card that is currently set to currCard
    public int getCard(){
        return currCard;
    }

    //This script skips the turn and goes to the next player
    public void skipTurn(){
        options.clear();
        nextTurn();
    }

    //This script ensures that the turn is always set to an active player
    public void nextTurn()
    {
        remainder = 0;
        System.out.println("Reset Remainder A");
        selected = -1;
        turn = (turn+1)%players;
        System.out.println("Turn: "+turn);
        currCard = pullCard();

        //Set options
        startOptions();
    }

    //Sets the options that don't require a pawn to be selected
    public void startOptions(){
        if(gb.startAmount(turn) > 0){
            if(currCard == 13){
                options.clear();
                if(gb.startAmount(turn) > 0){
                    for(int i = 0; i < gb.getMarginCount(); i++){
                        if(gb.getSpaces()[i] != null){
                            if(gb.getSpaces()[i].getPawnColor() != getTurnColor()){
                                options.add(i);
                            }
                        }
                    }
                }
            }else if(currCard == 1 || currCard == 2){
                if(gb.getSpaces()[gb.mySpawn(getTurn())] == null){
                    options.clear();
                    safeAdd(gb.myStart(getTurn()));
                }else{
                    if(gb.getSpaces()[gb.mySpawn(getTurn())].getPawnColor() != getTurnColor()){
                        options.clear();
                        safeAdd(gb.myStart(getTurn()));
                    }
                }
            }
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
                            index = gb.movePawn(selected,index);
                        }else{
                            //Swap
                            Pawn one = gb.getSpaces()[index];
                            Pawn two = gb.getSpaces()[selected];
                            gb.setSpace(index,two);
                            gb.setSpace(selected,one);
                        }
                        //Next move
                        specialNext(index);
                    }
                }
            }else{ //Sorry! Card
                if(options.contains(index)){
                    gb.startAdd(gb.getValue(gb.getSpaces()[index].getPawnColor()));
                    gb.startSubtract(turn);
                    gb.newPawn(getTurnColor(),index);
                    options.clear();
                    nextTurn();
                }
            }
        }else{ //Home and start
            if(options.contains(index)){
                if(index >= 120 && index <= 125 && index == gb.myStart(getTurn())){ //Start
                    if(gb.getSpaces()[gb.mySpawn(getTurn())] != null){
                        gb.startAdd(gb.getValue(gb.getSpaces()[gb.mySpawn(getTurn())].getPawnColor()));
                    }

                    gb.newPawn(getTurnColor(),gb.mySpawn(getTurn()));
                    gb.startSubtract(getTurn());
                    options.clear();

                    if(currCard == 2){
                        //Go again
                        System.out.println("Go Again");
                        turn--;
                        nextTurn();
                    }else{
                        nextTurn();
                    }
                }else if(index >= 126 && index <= 131){ //Home
                    gb.destroyPawn(selected);
                    gb.homeAdd(getTurn());
                    options.clear();

                    specialNext(index);
                }
            }
        }
    }

    //Does special next turns if the card is a 2 or a 7, otherwise does normal next turn.
    public void specialNext(int index){
        if(currCard == 7){
            pColor tempColor = gb.getSpaces()[index].getPawnColor();
            System.out.println("Remain "+remainder);
            if(remainder == 0){ //First move with 7
                remainder = 7-gb.distanceBetweenSpaces(selected,gb.getLand(),tempColor);
                System.out.println("Now: "+remainder);
                if(remainder != 0){
                    selected = -1;
                }else{
                    nextTurn();
                }
            }else{ //Second move with 7
                remainder = 0;
                System.out.println("Reset Remainder");
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

    //Clears the options array list and sets it with all the available spaces to move
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
                for(int i = 1; i <= 7; i++){
                    if(canMoveOther(c,7-i,index,i) || i == 7){
                        safeAdd(gb.countForward(index, i, c));
                    }
                }
            }else{
                System.out.println("Remain: "+remainder);
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

    //Checks if there is any pawns of a certain color that can move a set number of spaces. It must not be the index of orig.
    public boolean canMoveOther(pColor c, int spaces, int orig, int origOffset){
        boolean can = false;
        //Calculate next position of pawn
        int newOrig, newI;
        if(canMove(orig,origOffset)){
            newOrig = gb.countForward(orig,origOffset,c);
        }else{
            newOrig = -1;
        }
        //Add exceptions
        ArrayList<Integer> except = new ArrayList<Integer>();
        except.add(orig);
        //Add exeptions for slides
        if(newOrig == 6){
            for(int i = 6; i < 9; i++){
                except.add(i);
            }
            newOrig = 9;
        }else if(newOrig == 14){
            for(int i = 14; i < 18; i++){
                except.add(i);
            }
            newOrig = 18;
        }else if(newOrig == 21){
            for(int i = 21; i < 24; i++){
                except.add(i);
            }
            newOrig = 24;
        }else if(newOrig == 29){
            for(int i = 29; i < 33; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 33;
        }else if(newOrig == 36){
            for(int i = 36; i < 39; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 39;
        }else if(newOrig == 44){
            for(int i = 44; i < 18; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 48;
        }else if(newOrig == 51){
            for(int i = 51; i < 54; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 54;
        }else if(newOrig == 59){
            for(int i = 59; i < 63; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 63;
        }else if(newOrig == 66){
            for(int i = 66; i < 69; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 69;
        }else if(newOrig == 74){
            for(int i = 74; i < 78; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 78;
        }else if(newOrig == 81){
            for(int i = 81; i < 84; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 84;
        }else if(newOrig == 89){
            except.add(89);
            for(int i = 0; i < 3; i++){
                except.add(i);
            }
            System.out.println("Added exception "+newOrig);
            newOrig = 3;
        }
        //Check pawns
        for(int i = 0; i < gb.getSpaces().length; i++){
            if(gb.getSpaces()[i] != null && i != orig){
                //Check to see if pawn is of same color
                if(gb.getSpaces()[i].getPawnColor() == c){
                    //Project movement
                    newI = gb.countForward(i,spaces,c);
                    if(newI != -1){
                        //Check
                        if(gb.getSpaces()[newI] == null){
                            if(newI != newOrig){
                                can = true;
                            }
                        }else if((gb.getSpaces()[newI].getPawnColor() != c || except.contains(newI)) && newI != newOrig){
                            can = true;
                        }
                    }
                }
            }
        }
        return can;
    }

    //Checks if a pawn can move a certain number of spaces
    public boolean canMove(int index, int spaces){
        pColor c = gb.getSpaces()[index].getPawnColor();
        int newSpace = gb.countForward(index,spaces,c);
        if(newSpace != -1){
            if(newSpace > gb.getSpaces().length){
                return true;
            }else if(gb.getSpaces()[newSpace] == null){
                return true;
            }else{
                if(gb.getSpaces()[newSpace].getPawnColor() != c){
                    return true;
                }
            }
        }
        return false;
    }

    //Ensures that the pawn movement would be valid. For example, it cannot move onto a pawn of the same color or a negative space.
    public void safeAdd(int i){
        if(i >= 0){
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

    public void deselectPawn(){
        options.clear();
        startOptions();
        selected = -1;
    }

    //Get color blind setting
    public boolean getColorblind(){
        return colorBlind;
    }

    //Toggle color blind setting
    public void toggleColorblind(){
        colorBlind = !colorBlind;
    }
}