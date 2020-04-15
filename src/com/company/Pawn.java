package com.company;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//A basic pawn who holds a color
class Pawn{
    private pColor pawnColor;
    private int squareSize = 50;
    private ImageView iv;
    //private int lastX = -1;
    //private int lastY = -1;
    //private int goalX = lastX;
    //private int goalY = lastY;
    private int displayIndex = -1;
    private int displayGoal = -1;
    private int animateType = 0; //0: Forward, 1: Backwards, 2: None
    private GameBoard gb;

    //Basic Pawn constructor that assigns it with a user defined color
    public Pawn(pColor c, GameBoard gb){
        pawnColor = c;
        this.gb = gb;

        Image image = new Image(pawnImage(pawnColor,false,false));
        iv = new ImageView(image);
        iv.setX(-1);
        iv.setY(-1);
        iv.setFitHeight(squareSize);
        iv.setFitWidth(squareSize);
    }

    public pColor getPawnColor()
    {
        return pawnColor;
    }

    public void setAnimateType(int type){
        animateType = type;
    }

    public int getAnimateType(){
        return animateType;
    }

    public String pawnImage(pColor c, boolean colorBlind, boolean selected){
        String s;
        if(colorBlind){
            s = "ColorBlind/";
        }else{
            s = "Default/";
        }
        if(c == pColor.RED){
            s += "Red";
        }else if(c == pColor.ORANGE){
            s += "Orange";
        }else if(c == pColor.YELLOW){
            s += "Yellow";
        }else if(c == pColor.GREEN){
            s += "Green";
        }else if(c == pColor.BLUE){
            s += "Blue";
        }else if(c == pColor.PURPLE){
            s += "Purple";
        }
        if(selected){
            s += "Select.png";
        }else{
            s += "Pawn.png";
        }
        return s;
    }

    public ImageView getIv(){
        return iv;
    }

    public void updateThisPawn(int index){
        iv.setImage(new Image(pawnImage(pawnColor,false,false)));
        displayGoal = index;
        if(iv.getX() != -1){
            displayIndex = getInput((int)iv.getY()/squareSize,(int)iv.getX()/squareSize);
            iv.setX(getSquareX(displayIndex)*squareSize);
            iv.setY(getSquareY(displayIndex)*squareSize);
            System.out.println("Pawn at ("+iv.getX()+", "+iv.getY()+") going from "+displayIndex+" --> "+displayGoal);
        }else{
            displayIndex = index;
            iv.setX(getSquareX(displayIndex)*squareSize);
            iv.setY(getSquareY(displayIndex)*squareSize);
        }
    }

    public boolean animate(){
        if(displayGoal != displayIndex){
            if(animateType == 0){
                displayIndex = gb.nextSpace(displayIndex,pawnColor);
            }else if(animateType == 1){
                displayIndex = gb.lastSpace(displayIndex,pawnColor);
            }else{
                displayIndex = displayGoal;
            }
            iv.setX(getSquareX(displayIndex)*squareSize);
            iv.setY(getSquareY(displayIndex)*squareSize);
            iv.toFront();
            System.out.println("("+iv.getX()+", "+iv.getY()+")");
        }
        return displayGoal == displayIndex;
    }

    private int getSquareX(int index){
        if(index <= 30){
            return index;
        }else if(index <= 45){
            return 30;
        }else if(index <= 75){
            return (75-index);
        }else if(index <= 89){
            return 0;
        }else if(index == 125){
            return 1;
        }else if(index == 131 || index == 124){
            return 6;
        }else if(index == 126){
            return 7;
        }else if(index == 130){
            return 8;
        }else if(index == 120){
            return 9;
        }else if(index == 123){
            return 21;
        }else if(index == 127){
            return 22;
        }else if(index == 129){
            return 23;
        }else if(index == 121 || index == 126){
            return 24;
        }else if(index == 122){
            return 29;
        }else if(index >= 115 && index <= 119){
            return index-114;
        }else if(index >= 90 && index <= 94){
            return 7;
        }else if(index >= 110 && index <= 114){
            return 8;
        }else if(index >= 95 && index <= 99){
            return 22;
        }else if(index >= 105 && index <= 109){
            return 23;
        }else if(index >= 100 && index <= 104){
            return 129-index;
        }else{
            return 1;
        }
    }

    private int getSquareY(int index){
        if(index <= 30){
            return 0;
        }else if(index <= 45){
            return (index-30);
        }else if(index <= 75){
            return 15;
        }else if(index <= 89){
            return (90-index);
        }else if(index == 9 || index == 121){
            return 1;
        }else if(index == 125 || index == 126 || index == 127){
            return 6;
        }else if(index == 128){
            return 7;
        }else if(index == 131){
            return 8;
        }else if(index == 130 || index == 129 || index == 122){
            return 9;
        }else if(index == 124 || index == 123){
            return 14;
        }else if(index >= 115 && index <= 119){
            return 8;
        }else if(index >= 90 && index <= 94){
            return index-89;
        }else if(index >= 110 && index <= 114){
            return 124-index;
        }else if(index >= 95 && index <= 99){
            return index-94;
        }else if(index >= 105 && index <= 109){
            return 23;
        }else if(index >= 100 && index <= 104){
            return 119-index;
        }else{
            return 1;
        }
    }

    //Gets the input on the spaces array based on row and column
    public int getInput(int row, int col){
        if(row == 0) { //Top row
            return col;
        }else if(row == 15){
            return 75-col;
        }else if(col == 0){
            return 90-row;
        }else if(col == 30){
            return 30+row;
        }else if(col == 7 && row <= 5){ //Red Safety
            return row+89;
        }else if(col == 22 && row <= 5){ //Orange Safety
            return row+94;
        }else if(row == 7 && col >= 25){ //Yellow Safety
            return 104-col+25;
        }else if(col == 23 && row >= 10){ //Green Safety
            return 109-row+10;
        }else if(col == 8 && row >= 10){ //Blue Safety
            return 114-row+10;
        }else if(row == 8 && col <= 5){ //Purple Safety
            return 114+col;
        }else if(row == 1 && col == 9){ //Red Start
            return 120;
        }else if(row == 1 && col == 24){ //Orange start
            return 121;
        }else if(row == 9 && col == 29){ //Yellow start
            return 122;
        }else if(row == 14 && col == 21){ //Green start
            return 123;
        }else if(row == 14 && col == 6){ //Blue start
            return 124;
        }else if(row == 6 && col == 1){ //Purple start
            return 125;
        }else if(row == 6 && col == 7){ //Red home
            return 126;
        }else if(row == 6 && col == 22){ //Orange home
            return 127;
        }else if(row == 7 && col == 24){ //Yellow home
            return 128;
        }else if(row == 9 && col == 23){ //Green home
            return 129;
        }else if(row == 9 && col == 8){ //Blue home
            return 130;
        }else if(row == 8 && col == 6){ //Purple home
            return 131;
        }else{
            return -1;
        }
    }
}

//The color enum for 6 different Sorry! players.
//The players will always move in this turn order.
enum pColor{
    RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE;
}