package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {
    Sorry game;
    Group root = new Group();
    int rowNum = 16, colNum = 31, squareSize = 50;
    Scene scene = new Scene(root, colNum*squareSize, rowNum*squareSize);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        game = new Sorry();
        //update(primaryStage,game);

        /*scene.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.print("("+e.getX()+", "+e.getY()+") ");
                System.out.println(getInput((int)e.getY()/squareSize,(int)e.getX()/squareSize));
                game.takeTurn(getInput((int)e.getY()/squareSize,(int)e.getX()/squareSize));
                if(game.getWinner() == -1){
                    update(primaryStage,game);
                }else{
                    WinScreen(primaryStage,game);
                }
            }
        });*/
        WinScreen(primaryStage,game);
    }

    public void update(Stage primaryStage, Sorry game) {
        System.out.println("Update");
        root.getChildren().clear();
        for(int row = 0; row<rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                if (getInput(row,col) != -1){
                    //Draw Square
                    placeImage(row,col,spaceImage(getInput(row,col)));
                    if(game.getOptions().contains(getInput(row,col))){
                        placeImage(row,col,"Board/Selection.png");
                    }

                    //Draw Pawn
                    if(getInput(row,col) <= 119){
                        if(game.getBoard().getSpaces()[getInput(row,col)] != null){
                            placeImage(row,col,pawnImage(getInput(row,col)));
                        }
                    }

                    //Draw numbers
                    //Starts
                    if(col == 9 && row == 1){
                        placeImage(row, col, "Board/"+game.getBoard().startAmount(0)+".png");
                    }else if(col == 24 && row == 1){
                        placeImage(row, col, "Board/"+game.getBoard().startAmount(1)+".png");
                    }else if(col == 29 && row == 9){
                        placeImage(row, col, "Board/"+game.getBoard().startAmount(2)+".png");
                    }else if(col == 21 && row == 14){
                        placeImage(row, col, "Board/"+game.getBoard().startAmount(3)+".png");
                    }else if(col == 6 && row == 14){
                        placeImage(row, col, "Board/"+game.getBoard().startAmount(4)+".png");
                    }else if(col == 1 && row == 6){
                        placeImage(row, col, "Board/"+game.getBoard().startAmount(5)+".png");
                    }
                    //Homes
                    else if(col == 7 && row == 6){
                        placeImage(row, col, "Board/"+game.getBoard().homeAmount(0)+".png");
                    }else if(col == 22 && row == 6){
                        placeImage(row, col, "Board/"+game.getBoard().homeAmount(1)+".png");
                    }else if(col == 24 && row == 7){
                        placeImage(row, col, "Board/"+game.getBoard().homeAmount(2)+".png");
                    }else if(col == 23 && row == 9){
                        placeImage(row, col, "Board/"+game.getBoard().homeAmount(3)+".png");
                    }else if(col == 8 && row == 9){
                        placeImage(row, col, "Board/"+game.getBoard().homeAmount(4)+".png");
                    }else if(col == 6 && row == 8){
                        placeImage(row, col, "Board/"+game.getBoard().homeAmount(5)+".png");
                    }
                }
            }
        }

        //Skip Button
        Button skip = new Button();
        skip.setLayoutX(650);
        skip.setLayoutY(650);
        skip.setMinSize(250,50);
        skip.setMaxSize(250,50);
        skip.setStyle("-fx-font-size:20");
        if(game.getSelected() == -1){
            skip.setText("Skip Turn");
            skip.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    game.skipTurn();
                    System.out.println("Handle!");
                    update(primaryStage,game);
                }
            });
        }else{
            skip.setText("Deselect Pawn");
            skip.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    game.deselectPawn();
                    update(primaryStage,game);
                }
            });
        }
        root.getChildren().add(skip);

        //Draw and position the card in screen
        Image cardBack = new Image(setNextBack(game.getTurn()));
        ImageView backview = new ImageView(cardBack);
        backview.setFitWidth(250);
        backview.setFitHeight(350);
        backview.setX(650);
        backview.setY(225);
        root.getChildren().add(backview);

        Image cardImage = new Image(setNextCard(game.getCard()));
        ImageView cardView = new ImageView(cardImage);
        cardView.setFitWidth(250);
        cardView.setFitHeight(350);
        cardView.setX(650);
        cardView.setY(225);
        root.getChildren().add(cardView);

        //scene = new Scene(root, colNum*squareSize, rowNum*squareSize);
        primaryStage.setTitle("Sorry! Cycle 2.3");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //Returns the image to represent the space the space
    public String spaceImage(int index){
        String s = "Board/";
        if((index >= 90 && index <= 94) || index == 120 || index == 126){
            s+= "Square_Red";
        }else if((index >= 95 && index <= 99) || index == 121 || index == 127){
            s+= "Square_Orange";
        }else if((index >= 100 && index <= 104) || index == 122 || index == 128){
            s+= "Square_Yellow";
        }else if((index >= 105 && index <= 109) || index == 123 || index == 129){
            s+= "Square_Green";
        }else if((index >= 110 && index <= 114) || index == 124 || index == 130){
            s+= "Square_Blue";
        }else if((index >= 115 && index <= 119) || index == 125 || index == 131){
            s+= "Square_Purple";
        }else if(index <= 89 && ((index%15 >= 6 && index%15 <= 9) || (index%15 >= 0 && index%15 <= 3) || index%15 >= 14)){
            s+= "Slide_";
            if(index-5 < 0){
                s+= "Purple_";
            }else{
                switch((index-6)/15){
                    case 0: s+= "Red_"; break;
                    case 1: s+= "Orange_"; break;
                    case 2: s+= "Yellow_"; break;
                    case 3: s+= "Green_"; break;
                    case 4: s+= "Blue_"; break;
                    case 5: s+= "Purple_"; break;
                }
            }
            if(index == 0 || index == 30 || index == 45 || index == 75){
                s+= "Corner";
            }else{
                int ind = (index-6)%15;
                if(ind == 0 || ind == 8){
                    s+= "Start";
                }else{
                    if(ind == 3 || ind == 12 || index == 3){
                        s+= "End_";
                    }
                    if((index > 0 && index < 30) || (index > 45 && index < 75)){
                        s+= "Horizontal";
                    }else{
                        s+= "Vertical";
                    }
                }
            }
        }else{
            s += "Square";
        }
        return s+".png";
    }

    //Returns the image to represent a pawn
    public String pawnImage(int index){
        String s;
        if(game.getColorblind()){
            s = "ColorBlind/";
        }else{
            s = "Default/";
        }
        switch(game.getBoard().getValue(game.getBoard().getSpaces()[index].getPawnColor())){
            case 0: s+="Red"; break;
            case 1: s+="Orange"; break;
            case 2: s+="Yellow"; break;
            case 3: s+="Green"; break;
            case 4: s+="Blue"; break;
            case 5: s+="Purple"; break;
        }
        if(game.getSelected() == index){
            s += "Select.png";
        }else{
            s += "Pawn.png";
        }
        return s;
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

    //Places an image in a grid dictated by the variable "squareSize" which should be the size of the images placed with this method
    public void placeImage(int row, int col, String img){
        try{
            Image image = new Image(img);
            ImageView imageView = new ImageView(image);
            imageView.setX(col*squareSize);
            imageView.setY(row*squareSize);
            imageView.setFitHeight(squareSize);
            imageView.setFitWidth(squareSize);
            root.getChildren().add(imageView);
        }
        catch (Exception e){
            System.out.println("Error: \""+img+"\" Not Found.");
        }
    }

    //Get the face of the card
    public String setNextCard(int nextCard){
        switch(nextCard){
            case 1: return "One.png";
            case 2: return "Two.png";
            case 3: return "Three.png";
            case 4: return "Four.png";
            case 5: return "Five.png";
            case 7: return "Seven.png";
            case 8: return "Eight.png";
            case 10: return "Ten.png";
            case 11: return "Eleven.png";
            case 12: return "Twelve.png";
            case 13: return "Sorry.png";
            default: return "none.jpg";
        }
    }

    //Get the back of the card
    public String setNextBack(int turn){
        String img;
        if(game.getColorblind()){
            img = "ColorBlind/";
        }else{
            img = "Default/";
        }
        switch(turn){
            case 0: img += "RedCard.png"; break;
            case 1: img += "OrangeCard.png"; break;
            case 2: img += "YellowCard.png"; break;
            case 3: img += "GreenCard.png"; break;
            case 4: default: img += "BlueCard.png"; break;
            case 5: img += "PurpleCard.png"; break;
        }
        return img;
    }

    //Draw win screen
    public void WinScreen(Stage primaryStage, Sorry game) {
        System.out.println("Winscreen");
        root.getChildren().clear();

        DrawText("A Winrar is you!",scene.getWidth()/2,scene.getHeight()/2,80,true);

        primaryStage.setTitle("Sorry! Cycle 2.3");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void DrawText(String string, double x, double y, int size, boolean center){
        Text text = new Text();
        text.setText(string);
        text.setFont(Font.font("Verdana",size));

        text.setY(y);
        if(center){
            text.setX(x-(text.getLayoutBounds().getWidth()/2));
        }else{
            text.setX(x);
        }

        root.getChildren().add(text);
    }
}