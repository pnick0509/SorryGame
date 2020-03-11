package com.company;

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
            start[i] = 4;
            home[i] = 0;
        }
    }

    //Keeps track of how many pawns in start and home
    //Adds a pawn to the start count
    public void startAdd(int player){
        start[player]++;
    }

    //Removes a pawn form the start count
    public void startSubtract(int player){
        start[player]--;
    }

    //Adds a pawn to the home count
    public void homeAdd(int player){
        home[player]++;
    }

    //Returns the number of pawns at start
    public int startAmount(int player){
        return start[player];
    }

    //Returns the number of pawns at home
    public int homeAmount(int player){
        return home[player];
    }

    //Returns the button id of the start position
    public int myStart(int player){
        return 120+player;
    }

    //Returns the button id of the end position
    public int myHome(int player){
        return 126+player;
    }

    //Return the button id of the "spawn" position of the pawn. A.k.a. the position in front of the start.
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
        if(spaces[post] != null){
            startAdd(getValue(spaces[post].getPawnColor()));
        }

        spaces[post] = spaces[pre];
        spaces[pre] = null;
        System.out.println(pre+" --> "+post);
    }

    public void destroyPawn(int index){
        spaces[index] = null;
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