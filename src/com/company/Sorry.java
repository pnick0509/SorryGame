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
    private int winner = -1;

    private int selected = -1;
    private ArrayList<Integer> options;
    private ArrayList<Ai> AI;
    private ArrayList<Integer> TurnOrder;

    private boolean cardCheats;
    private boolean colorBlind;

    private int[] stat_forward = new int[6];
    private int[] stat_backward = new int[6];
    private int[] stat_slide = new int[6];

    private Main main;

    public int[] getStat_forward() {
        return stat_forward;
    }

    public int[] getStat_backward(){
        return stat_backward;
    }

    public int[] getStat_slide(){
        return stat_slide;
    }

    public void addStat_slide(int index, int amount){
        stat_slide[index] += amount;
    }

    //Starts a new game with six players
    public Sorry(Main main){
        gb = new GameBoard(this);
        players = 6;
        turn = -1; //0: Red, 1: Orange, 2: Yellow, 3: Green, 4: Blue, 5: Purple
        cards = new ArrayList<Integer>();
        createCards();
        rand = new Random();
        currCard = pullCard();

        AI = new ArrayList<Ai>();//List of AI players
        TurnOrder = new ArrayList<Integer>();
        
        System.out.println("Ais all set up");

        options = new ArrayList<Integer>();
        nextTurn();

        cardCheats = true;
        colorBlind = false;

        this.main = main;
    }

    public void newAI(int turn, boolean difficulty){
        AI.add(new Ai(this,difficulty,turn));
    }

    public void addTurnOrder(int turn){
        TurnOrder.add(turn);
    }

    public void createCards()
    {
        cards.clear();
        for(int y=0; y<=13; y++) {
            if(y==9){
                y++;
            }
            for (int x=0; x<5; x++) {
                cards.add(y);
            }
        }
        cards.add(1);
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
        checkWinner();
        System.out.println("Show winner? "+winner);
        if(getWinner() != -1){
            main.doWin();
        }else{
            remainder = 0;
            System.out.println("Reset Remainder A");
            selected = -1;
            if(TurnOrder.contains(turn)){
                turn = TurnOrder.get((TurnOrder.indexOf(turn)+1)%TurnOrder.size());
            }else if(TurnOrder.size() > 0){
                turn = TurnOrder.get(0);
            }else{
                turn = 0;
            }

            System.out.println("Turn: "+turn);
            currCard = pullCard();

            //Set options
            startOptions();

            //Do Ai stuffs
            try{
                main.queueAi(getAiTurn());
                main.update();
            }catch(Exception e){
                System.out.println("CATCH");
            }
            System.out.println("Report Back");
        }
    }

    //Check if it's an ai's turn
    public Ai getAiTurn(){
        int i = 0;
        Ai foundAi = null;
        while(i < AI.size() && foundAi == null){
            if(AI.get(i).getPlayer() == getTurn()){
                foundAi = AI.get(i);
            }
            i++;
        }
        return foundAi;
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
                        if(gb.getSpaces()[index].getPawnColor() == getTurnColor() && currCard != 6){
                            selected = index;
                            setOptions(selected,gb.getSpaces()[index].getPawnColor(),currCard);
                            if(options.size() == 0){
                                selected = -1;
                            }
                        }else if(gb.getSpaces()[index].getPawnColor() != getTurnColor() && currCard == 6){
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
                            if(currCard == 10){
                                Pawn temp = gb.getSpaces()[selected];
                                System.out.println("THIS: "+temp+" "+index+" "+gb.lastSpace(selected,temp.getPawnColor()));
                                if(index == gb.lastSpace(selected,temp.getPawnColor())){
                                    temp.setAnimateType(1);
                                    stat_backward[turn] += 1;
                                }else{
                                    temp.setAnimateType(0);
                                    stat_forward[turn] += 10;
                                }
                            }else if(currCard == 4){
                                Pawn temp = gb.getSpaces()[selected];
                                temp.setAnimateType(1);
                                stat_backward[turn] += 4;
                            }else if(currCard == 6){
                                Pawn temp = gb.getSpaces()[selected];
                                temp.setAnimateType(1);
                                //stat_backward[turn] += 6;
                            }else{
                                Pawn temp = gb.getSpaces()[selected];
                                temp.setAnimateType(0);
                                stat_forward[turn] += gb.distanceBetweenSpaces(selected,index, temp.getPawnColor());
                            }
                            index = gb.movePawn(selected,index);
                        }else{
                            //Swap
                            Pawn one = gb.getSpaces()[index];
                            Pawn two = gb.getSpaces()[selected];
                            if(one == null){
                                two.setAnimateType(0);
                                stat_forward[turn] += 11;
                            }else{
                                one.setAnimateType(2);
                                two.setAnimateType(2);
                            }
                            gb.setSpace(index,two);
                            gb.setSpace(selected,one);
                            gb.updatePawn(index);
                            gb.updatePawn(selected);
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
                gb.setLand(index);
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

                    stat_forward[turn] += gb.distanceBetweenSpaces(selected,index, getTurnColor());

                    System.out.println("In");

                    //Check to see if win state
                    checkWinner();
                    specialNext(index);
                }
            }
        }
    }

    public void checkWinner(){
        for(int i = 0; i < 6; i++){
            if(gb.homeAmount(i) == 4){
                winner = i;
            }
        }
    }

    //Does special next turns if the card is a 2 or a 7, otherwise does normal next turn.
    public void specialNext(int index){
        if(currCard == 7){
            //pColor tempColor = gb.getSpaces()[index].getPawnColor();
            System.out.println("Remain "+remainder);
            if(remainder == 0){ //First move with 7
                remainder = 7-gb.distanceBetweenSpaces(selected,gb.getLand(),getTurnColor());
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
        }else if(card == 6){
            safeAdd(gb.countBackward(index,6,gb.getSpaces()[index].getPawnColor()));
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
        ArrayList<Integer> except = new ArrayList<>();
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
            for(int i = 44; i < 47; i++){
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
                        if(newI < 120){
                            //Check
                            if(gb.getSpaces()[newI] == null){
                                if(newI != newOrig){
                                    can = true;
                                }
                            }else if((gb.getSpaces()[newI].getPawnColor() != c || except.contains(newI)) && newI != newOrig){
                                can = true;
                            }
                        }else{
                            if(newI >= 126 && newI <= 131){
                                can = true;
                            }
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

    public boolean getCardCheats(){
        return cardCheats;
    }

    public void toggleCardCheats(){
        cardCheats = !cardCheats;
    }

    public int getWinner(){
        return winner;
    }

    public void reset(){
        selected = -1;
        winner = -1;
        createCards();
        for(int i = 0; i < gb.getSpaces().length; i++){
            gb.destroyPawn(i);
        }
        for(int i = 0; i < players; i++){
            gb.setStart(i,4);
            gb.setHome(i,0);
        }
        Random r = new Random();
        turn = TurnOrder.get(Math.abs(r.nextInt()%TurnOrder.size()));
        Ai a = getAiTurn();
        if(a != null){
            a.taketurn(getCard());
        }
    }

    public void preset1(){
        selected = -1;
        for(int i = 0; i < gb.getSpaces().length; i++){
            gb.destroyPawn(i);
        }
        for(int i = 0; i < players; i++){
            gb.setStart(i,0);
            gb.setHome(i,3);
            gb.newPawn(pColor.values()[i],94+(i*5));
        }
    }

    public void preset2(){
        selected = -1;
        for(int i = 0; i < gb.getSpaces().length; i++){
            gb.destroyPawn(i);
        }
        for(int i = 0; i < players; i++){
            gb.setStart(i,4);
            gb.setHome(i,0);
        }
        gb.setStart(0,2);
        gb.setStart(2,3);
        gb.setStart(4,3);
        gb.newPawn(pColor.RED,13);
        gb.newPawn(pColor.RED,50);
        gb.newPawn(pColor.YELLOW,36);
        gb.newPawn(pColor.BLUE,66);
    }

    public void preset3(){
        selected = -1;
        currCard = 1;
        startOptions();
    }

    public void preset4(){
        selected = -1;
        currCard = 13;
        startOptions();
    }

    public void preset5(){
        selected = -1;
        int r;
        for(int i = 0; i < gb.getSpaces().length; i++){
            gb.destroyPawn(i);
        }
        for(int i = 0; i < players; i++){
            gb.setStart(i,0);
            gb.setHome(i,0);
            for(int j = 0; j < 4; j++){
                do{
                    r = Math.abs(rand.nextInt()%90);
                }while(gb.getSpaces()[r] != null);
                gb.newPawn(pColor.values()[i],r);
                gb.checkSlides(r);
            }
        }

    }

    public Main getMain(){
        return main;
    }

    public void setTurn(int turn){
        this.turn = turn;
    }

    public ArrayList<Ai> getTurnOrder(){
        return AI;
    }
}