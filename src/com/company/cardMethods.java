import javax.lang.model.util.ElementScanner6;

public class cardMethods
{
private Sorry game;
private Gameboard gb;
private int previousSelection;
private int remainingSpaces;
public cardMethods(Sorry game)
{
this.game = game;
game.gb=gb;
previousSelection = -1;
remainingSpaces=0;
}
public boolean one(int space)
{
gb.advancepawn(space,1);
return true;
}
public boolean two(int space)
{
gb.advance(space,2);
game.goAgain();
return true;
}
public boolean three(int space)
{
gb.advance(space,3);
return true;
}
public boolean four(int space)
{
gb.advance(space,-4);    
return true;
}
public boolean five(int space)
{
gb.advance(space,5);
return true;
}
/*
In case we add it
*/
public boolean six(int space)
{
return true;
} 
public boolean seven(int space)
{
if(remainingSpaces==0)
    {
        remainingSpaces=7;
    }
if(previousSpace==-1)
    {   
        previousSpace = space;
        return false;
    }
else if(remainingSpaces!=7)
{
    advancePawn(space,remainingSpaces);
    previousSpace=-1;
    remainingSpaces=0;
    return true;
}
else if(distanceBetweenSpaces(previousSpace,space)<7)
{
    int temp =distanceBetweenSpace(previousSpace,space);
    remainingSpaces=remainingSpaces-temp;
    advancePawn(previousSpace,temp);
    if(remainingSpaces==0)
    {
        previousSpace=-1;
        return true;
    }
    else 
    {
        return false;
    }
}
}
public boolean eight(int space)
{
    advancePawn(space,8);
    return true;
}
public boolean nine(int space)
{
    return true;
}
public boolean ten(int space)
{
    if(previousSpace==-1)
    {
        previousSpace = space;
        return false;
    }
    else if(space<previousSpace)
    {
        previousSpace = -1;
        advancePawn(space,-1);
        return true;
    }
    else
    {   
        previousSpace = -1;
        advancePawn(space,10);
        return true;
    }
}
public boolean eleven(int space)
{   
    if(previousSpace==-1)
    {
        previousSpace=space;
        return false;
    }
    else if(distanceBetweenSpaces(previousSpace,space)==11)
    {
        advancePawn(previousSpace,11);
        previousSpace=-1;
        return true;
    }
    else
    {
        swap(previousSpace,space);
        previousSpace=-1;
        return true;
    }
}
public boolean twelve(int space)
{
    if(previousSpace==-1)
    {
        previousSpace=space;
        return false;
    }
    else  
    {
        swap(previousSpace,space);
        previousSpace=-1;
        return true;
    }
}
public boolean useCard(int cardNumber, int button)
    {

        switch(cardNumber)
    {
            case 1:
                return one(button);
            case 2:
                return two(button);
            case 3:
                return three(button);
            case 4:
                return four(button);
            case 5:
                return five(button);
            case 6:
                return six(button);
            case 7:
                return seven(button);
            case 8:
                return eight(button);
            case 9:
                return nine(button);
            case 10:
                return ten(button);
            case 11:
                return eleven(button);
            case 12:
                return twelve(button);
            default:
                return -1;

        }
    }
}