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
        //Timer t = new Timer();
        //t.set(1.0);
        //game.getMain().update();
        //while(!t.checkTime());
        System.out.println("Card: "+card+" Options: "+game.getOptions().size());
        if(game.getOptions().size() > 0){
            int s = Math.abs(r.nextInt()%game.getOptions().size());
            int r = (int) game.getOptions().get(s);
            System.out.println(game.getOptions());
            game.takeTurn(r);
            //int index = game.getOptions().get(s);
            //int index = game.getOptions().get(s);
            //game.takeTurn(game.getOptions().get(r.nextInt()%game.getOptions().size()));
        }else{
            ArrayList myPawns = new ArrayList<Integer>();
            Pawn[] spaces = game.getBoard().getSpaces();
            for(int i=0; i<spaces.length; i++)
            {
                if(spaces[i] != null){
                    if(spaces[i].getPawnColor()==this.playerColor)
                    {
                        myPawns.add(i);
                    }
                }

            }
            boolean success = false;
            while(!myPawns.isEmpty() && !success){
                int randIndex = Math.abs(r.nextInt()%myPawns.size());
                game.takeTurn((int)myPawns.get(randIndex));
                if(!game.getOptions().isEmpty()){
                    game.takeTurn((int)game.getOptions().get(Math.abs(r.nextInt()%game.getOptions().size())));
                    success = true;
                }

                myPawns.remove(randIndex);
            }
            if(!success){
                game.skipTurn();
            }else if(game.remainder != 0){
                System.out.println("Need to do remainder "+game.remainder);
                myPawns.clear();
                for(int i=0; i<spaces.length; i++)
                {
                    if(spaces[i] != null){
                        if(spaces[i].getPawnColor()==this.playerColor)
                        {
                            myPawns.add(i);
                        }
                    }

                }
                success = false;
                while(!myPawns.isEmpty() && !success){
                    int randIndex = Math.abs(r.nextInt()%myPawns.size());
                    game.takeTurn((int)myPawns.get(randIndex));
                    if(!game.getOptions().isEmpty()){
                        game.takeTurn((int)game.getOptions().get(Math.abs(r.nextInt()%game.getOptions().size())));
                        success = true;
                    }

                    myPawns.remove(randIndex);
                }
            }
        }

        System.out.println("Done with ai player "+player+"'s turn!");
    }
}