package com.company;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Ai {
    private boolean hard;
    private int player;
    private Random r = new Random();
    private pColor playerColor;
    private Sorry game;
    Ai(Sorry game, boolean diff, int player)
    {
        hard = diff;
        this.player = player;
        playerColor = pColor.values()[player];
        this.game = game;
    }

    public int getPlayer()
    {
        return player;
    }

    public void taketurn(int card) {
        boolean turnDone=false;
        System.out.println("Card: " + card + " Options: " + game.getOptions().size());
        if(hard)
        {
            ArrayList myPawns = new ArrayList<Integer>();
            Pawn[] spaces = game.getBoard().getSpaces();
            for (int i = 0; i < spaces.length; i++) {
                if (spaces[i] != null) {
                    if (spaces[i].getPawnColor() == this.playerColor) {
                        myPawns.add(i);
                    }
                }

            }
            if((card ==2||card==1)&&game.getOptions().contains(game.getBoard().myStart(player)))
            {
                game.takeTurn(game.getBoard().myStart(player));
                turnDone = true;
            }
            if((card == 4))
            {
                for(int z=0; z<myPawns.size() &&!turnDone; z++)
                {
                    if ((int)myPawns.get(z)==game.getBoard().mySpawn(player)//If in the first three spaces go backwards
                            || ((int)myPawns.get(z)==game.getBoard().mySpawn(player) + 1)
                            || ((int)myPawns.get(z)==game.getBoard().mySpawn(player) + 2))
                    {    //Move Backwards 4
                        turnDone = true;
                        game.takeTurn((int)myPawns.get(z));
                        game.takeTurn(game.getBoard().countBackward((int) myPawns.get(z), 4, playerColor));
                    }


                }
                for(int z=0; z<myPawns.size() &&!turnDone; z++)
                {   //Check if there is a pawn 4 spaces backwards
                    if(game.getBoard().getSpaces()[game.getBoard().countBackward((int) myPawns.get(z), 4, playerColor)]!=null
                    && (game.getBoard().getSpaces()[game.getBoard().countBackward((int) myPawns.get(z), 4, playerColor)].getPawnColor()!=playerColor))
                    {    //Move Backwards 4
                        turnDone = true;
                        game.takeTurn((int)myPawns.get(z));
                        game.takeTurn(game.getBoard().countBackward((int) myPawns.get(z), 4, playerColor));
                    }
                }
                if(!turnDone)
                {
                    game.skipTurn();
                    turnDone=true;
                }
            }
            if(card==7)
            {   //First check if we can reach two slides.
                for(int z=0; z<myPawns.size() &&!turnDone; z++)
                {
                    for(int i=1; i<7; i++)
                    {
                        System.out.println("Start 2 slide attempt");
                        if(game.getBoard().checkSlides(game.getBoard().countForward((int)myPawns.get(z),i,playerColor),playerColor))
                        {
                            System.out.println("Continuing 2 slide attempt");
                            for(int j=z+1; j<myPawns.size() &&!turnDone; j++)
                            {
                                    System.out.println("Continuing 2 slide attempt part 2");
                                    if(game.getBoard().checkSlides(game.getBoard().countForward((int)myPawns.get(z),7-i,playerColor),playerColor))
                                    {
                                        game.takeTurn((int)myPawns.get(z));
                                        game.takeTurn(game.getBoard().countForward((int)myPawns.get(z),i,playerColor));
                                        game.takeTurn((int)myPawns.get(j));
                                        game.takeTurn(game.getBoard().countForward((int)myPawns.get(z),7-i,playerColor));
                                        turnDone=true;
                                        System.out.println("Successfully took a 7 card turn with 2 slides");
                                    }
                            }
                        }
                    }
                }
                //Next Check if it can reach one of them.
                for(int z=0; z<myPawns.size() &&!turnDone; z++)
                {
                    game.takeTurn((int)myPawns.get(z));
                    for(int i=1; i<=7 && !turnDone; i++)
                    {
                        System.out.println("Started 7 card turn 1 slide attempt");
                        if(game.getBoard().checkSlides(game.getBoard().countForward((int)myPawns.get(z),i,playerColor),playerColor))
                        {
                            if(game.getOptions().contains(game.getBoard().countForward((int)myPawns.get(z),i,playerColor)))
                            {
                                game.takeTurn(game.getBoard().countForward((int) myPawns.get(z), i, playerColor));
                                for(int j=0; j<myPawns.size()&&!turnDone;j++)
                                {
                                    if(j!=z)
                                        {
                                            System.out.println("Inside the Outer loop: AI 7 Card");
                                            game.takeTurn((int)myPawns.get(j));
                                                if(game.getOptions().size()>0)
                                                {
                                                    game.takeTurn(game.getBoard().countForward((int)myPawns.get(j),game.remainder,playerColor));
                                                    turnDone=true;
                                                    System.out.println("Inside the AI 7 Card");
                                                }
                                            game.deselectPawn();
                                        }
                                }
                            }

                        }
                    }
                    game.deselectPawn();
                }

            }
            if(card==11)
        {
            int maxSpace=-1;
            int minSpace=-1;
            int homeIndex = game.getBoard().countBackward(game.getBoard().myStart(player),2,playerColor);//Outside home stretch
            for(int i =0; i<myPawns.size();i++)
            {
                if(minSpace==-1)
                {
                    minSpace=(int)myPawns.get(i);
                }
                if(game.getBoard().distanceBetweenSpaces(game.getBoard().mySpawn(player),(int)myPawns.get(i),playerColor)
                        < game.getBoard().distanceBetweenSpaces(game.getBoard().mySpawn(player),minSpace,playerColor))
                {
                    minSpace=(int)myPawns.get(i);
                }
            }
            game.takeTurn(minSpace);
            System.out.println("Home Index is "+homeIndex);
            for(int j=0; j<game.getOptions().size(); j++)
            {
                int i = (int)game.getOptions().get(j);
                System.out.println("Choice is "+i);
                if(game.getBoard().getSpaces()[i]!=null && game.getBoard().getSpaces()[i].getPawnColor()!=playerColor)
                {
                    if(maxSpace==-1)
                    {
                        maxSpace=i;
                    }
                    if((game.getBoard().distanceBetweenSpaces(i,homeIndex,playerColor))<(game.getBoard().distanceBetweenSpaces(maxSpace,homeIndex,playerColor))

                    )
                    {
                        maxSpace=i;
                    }
                }
            }
            if(minSpace!=-1&&maxSpace!=-1)
            {
                if(game.getBoard().distanceBetweenSpaces(maxSpace,homeIndex,playerColor)
                        < game.getBoard().distanceBetweenSpaces(minSpace,homeIndex,playerColor))
                {
                    game.takeTurn(minSpace);
                    game.takeTurn(maxSpace);
                    turnDone=true;
                }
            }
            else
            {
                game.skipTurn();
                turnDone=true;
            }
        }

            if(card==13)
            {
                int maxSpace=-1;
                int homeIndex = game.getBoard().countBackward(game.getBoard().myStart(player),2,playerColor);//Outside home stretch
                System.out.println("Home Index is "+homeIndex);
                for(int j=0; j<game.getOptions().size(); j++)
                {
                    int i = (int)game.getOptions().get(j);
                    System.out.println("Choice is "+i);
                    if(game.getBoard().getSpaces()[i]!=null && game.getBoard().getSpaces()[i].getPawnColor()!=playerColor)
                    {
                        if(maxSpace==-1)
                        {
                            maxSpace=i;
                        }
                        if((game.getBoard().distanceBetweenSpaces(i,homeIndex,playerColor))<(game.getBoard().distanceBetweenSpaces(maxSpace,homeIndex,playerColor))

                            )
                        {
                            maxSpace=i;
                        }
                    }
                }
                if(maxSpace!=-1)
                {
                    System.out.println("Inside hard mode Sorry Card Success");
                    System.out.println("The max space is" + maxSpace);
                    game.takeTurn(maxSpace);
                    turnDone=true;
                }
                else
                {
                    System.out.println("Inside hard mode Sorry Card Skip");
                    game.skipTurn();
                    turnDone=true;
                }
            }



        }
        if(!turnDone)
        {
            if (game.getOptions().size() > 0) {
                int s = Math.abs(r.nextInt() % game.getOptions().size());
                int r = (int) game.getOptions().get(s);
                System.out.println(game.getOptions());
                game.takeTurn(r);

            } else {
                ArrayList myPawns = new ArrayList<Integer>();
                Pawn[] spaces = game.getBoard().getSpaces();
                for (int i = 0; i < spaces.length; i++) {
                    if (spaces[i] != null) {
                        if (spaces[i].getPawnColor() == this.playerColor) {
                            myPawns.add(i);
                        }
                    }

                }
                boolean success = false;
                while (!myPawns.isEmpty() && !success) {
                    int randIndex = Math.abs(r.nextInt() % myPawns.size());
                    game.takeTurn((int) myPawns.get(randIndex));
                    if (!game.getOptions().isEmpty()) {
                        game.takeTurn((int) game.getOptions().get(Math.abs(r.nextInt() % game.getOptions().size())));
                        success = true;
                    }

                    myPawns.remove(randIndex);
                }
                if (!success) {
                    game.skipTurn();
                } else if (game.remainder != 0) {
                    System.out.println("Need to do remainder " + game.remainder);
                    myPawns.clear();
                    for (int i = 0; i < spaces.length; i++) {
                        if (spaces[i] != null) {
                            if (spaces[i].getPawnColor() == this.playerColor) {
                                myPawns.add(i);
                            }
                        }

                    }
                    success = false;
                    while (!myPawns.isEmpty() && !success) {
                        int randIndex = Math.abs(r.nextInt() % myPawns.size());
                        game.takeTurn((int) myPawns.get(randIndex));
                        if (!game.getOptions().isEmpty()) {
                            game.takeTurn((int) game.getOptions().get(Math.abs(r.nextInt() % game.getOptions().size())));
                            success = true;
                        }

                        myPawns.remove(randIndex);
                    }
                }
            }
        }
        System.out.println("Done with ai player "+player+"'s turn!");
    }
}