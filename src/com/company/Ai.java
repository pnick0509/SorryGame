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
            }
        }

        System.out.println("Done with ai player "+player+"'s turn!");
        //game.nextTurn();
        /*
        ArrayList options = new ArrayList<Integer>();
        //If the spawn still has pawns and the card is 1,2 move from spawn.
        if(card<=2 && (game.getBoard().getStart(player)>1))
        {
            if(game.getBoard().getSpaces[mySpawn(player)] != null)
                {
                    newPawn(playerColor,mySpawn(getTurn()));
                    break;
                }
        }
        //Can't use the Sorry Card the bot would be too difficult otherwise.
        if(card == 13)
        {
        break;
        }
        //If it can't move don't try.
        else if(z==0)
        {
        break;
        }
        //Makes one move randomized from its options
        //I.E. if card == 7 it will only move once and then skip turn.
        else
        {
            int j = r.nextInt(z+1);
            game.setOptions(index[j], player, card);
            options[0] = game.getOptions();
            int k = r.nextInt(options[0].size());
            finalPos = options[0].get(k);
            move(index[j],finalPos);
            break;
        }*/
    }
}