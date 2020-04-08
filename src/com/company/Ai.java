package com.company;
import java.util.Random;
public class Ai
{
boolean hard;
int player;
Random r = new Random();
pColor playerColor;
Ai(Sorry game, boolean diff, int player)
{
    hard = diff;
    player = c.get;
    playerColor = pColor.values()[player];
}
public int getPlayer()
{
return player;
}
public void taketurn(int card)
{
int index[] = new int[4];
int z = 0;//Pawns on the board
spaces = game.getGameboard().getSpaces(); 
for(int i=0; i<getSize(); i++)
{
    if(spaces[i].getPawnColor()==this.player)
    {
        index[z] = i;
        z++;
    }

}
Arraylist options = new Arraylist[4];
/*
If the spawn still has pawns and the card is 1,2 move from spawn.
*/
if(card<=2 && (game.getGameBoard().getStart(player)>1))
{
    if(game.getBoard().getSpaces[mySpawn(player)] != null)
        {
            newPawn(playerColor,mySpawn(getTurn()));
            break;
        }
}
/*
Can't use the Sorry Card the bot would be too difficult otherwise.
*/
if(card == 13)
{
break;
}
/*
If it can't move don't try.
*/
else if(z==0)
{
break;
}
/*
Makes one move randomized from its options
I.E. if card == 7 it will only move once and then skip turn.
*/
else 
{
    int j = r.nextInt(z+1);
    game.setOptions(index[j], player, card);
    options[0] = game.getOptions();
    int k = r.nextInt(options[0].size());
    finalPos = options[0].get(k);
    move(index[j],finalPos);
break;
}


}
















}