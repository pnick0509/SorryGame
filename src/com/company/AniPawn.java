package com.company;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AniPawn {
    private ImageView myImage;
    private double goalX;
    private double goalY;
    private double speed;

    public AniPawn(ImageView imageview){
        myImage = imageview;
    }

    public void moveTo(double x, double y, double speed){
        if(x != myImage.getX() || y != myImage.getY()){
            double angle = Math.atan2((x-myImage.getX()),(y-myImage.getY()));
            System.out.println(angle+" "+speed*Math.cos(angle));
            myImage.setX(myImage.getX()+speed*Math.sin(angle));
            myImage.setY(myImage.getY()+speed*Math.cos(angle));
            if(Math.sqrt(Math.pow(myImage.getX()-x,2)-Math.pow(myImage.getY()-y,2)) <= speed){
                myImage.setX(x);
                myImage.setY(y);
            }
        }
    }

    public ImageView getImageView(){
        return myImage;
    }
}
