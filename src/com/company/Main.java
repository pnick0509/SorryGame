package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.paint.*;

public class Main extends Application {
    Sorry game;
    Group root = new Group();
    int rowNum = 16, colNum = 31, squareSize = 50;
    Scene scene = new Scene(root, colNum*squareSize, rowNum*squareSize);
    int[] points = new int[6];
    int screen;
    String header = "Sorry! Cycle 3.1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        game = new Sorry();

        //Set points
        for(int i = 0; i < 6; i++){
            points[i] = 0;
        }

        StartScreen(primaryStage,game);
        //update(primaryStage);

        scene.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && screen == 1) {
                System.out.print("("+e.getX()+", "+e.getY()+") ");
                System.out.println(getInput((int)e.getY()/squareSize,(int)e.getX()/squareSize));

                //Check if AI Player. And then goes through the AI Loop.
                game.AITurn();
                game.takeTurn(getInput((int)e.getY()/squareSize,(int)e.getX()/squareSize));
                if(game.getWinner() == -1){
                    update(primaryStage);
                }else{
                    //Add points
                    for(int i = 0; i < 6; i++){
                        if(i != game.getWinner()){
                            points[i] += game.getBoard().homeAmount(i)*5;
                        }else{
                            points[i] += game.getBoard().homeAmount(i)*10;
                        }
                    }
                    //Show screen
                    WinScreen(primaryStage,game);
                }
            }
        });
        //WinScreen(primaryStage,game);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.F1) {
                System.out.println("You pressed f1");
                game.preset1();
                update(primaryStage);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.F2) {
                System.out.println("You pressed f2");
                game.preset2();
                update(primaryStage);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.F3) {
                System.out.println("You pressed f3");
                game.preset3();
                update(primaryStage);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.F4) {
                System.out.println("You pressed f4");
                game.preset4();
                update(primaryStage);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.F5) {
                System.out.println("You pressed f5");
                game.preset5();
                update(primaryStage);
            }
        });
    }

    public void update(Stage primaryStage) {
        screen = 1;
        System.out.println("Update");
        root.getChildren().clear();

        DrawBackground(Color.rgb(250,250,250));

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
        skip.setFont(getFont(20));
        if(game.getSelected() == -1){
            skip.setText("Skip Turn");
            skip.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    game.skipTurn();
                    System.out.println("Handle!");
                    update(primaryStage);
                }
            });
        }else{
            skip.setText("Deselect Pawn");
            skip.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    game.deselectPawn();
                    update(primaryStage);
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
        primaryStage.setTitle(header);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //Returns the image to represent the space the space
    public String spaceImage(int index){
        String s = "Board/";
        if((index >= 90 && index <= 94) || index == 120 || index == 126){
            s+= "Square_Red";
            if(game.getColorblind()){
                s += "_CB";
            }
        }else if((index >= 95 && index <= 99) || index == 121 || index == 127){
            s+= "Square_Orange";
            if(game.getColorblind()){
                s += "_CB";
            }
        }else if((index >= 100 && index <= 104) || index == 122 || index == 128){
            s+= "Square_Yellow";
            if(game.getColorblind()){
                s += "_CB";
            }
        }else if((index >= 105 && index <= 109) || index == 123 || index == 129){
            s+= "Square_Green";
            if(game.getColorblind()){
                s += "_CB";
            }
        }else if((index >= 110 && index <= 114) || index == 124 || index == 130){
            s+= "Square_Blue";
            if(game.getColorblind()){
                s += "_CB";
            }
        }else if((index >= 115 && index <= 119) || index == 125 || index == 131){
            s+= "Square_Purple";
            if(game.getColorblind()){
                s += "_CB";
            }
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
            default: case 0: return "Faces/Zero.png";
            case 1: return "Faces/One.png";
            case 2: return "Faces/Two.png";
            case 3: return "Faces/Three.png";
            case 4: return "Faces/Four.png";
            case 5: return "Faces/Five.png";
            case 6: return "Faces/Six.png";
            case 7: return "Faces/Seven.png";
            case 8: return "Faces/Eight.png";
            case 9: return "Faces/Nine.png";
            case 10: return "Faces/Ten.png";
            case 11: return "Faces/Eleven.png";
            case 12: return "Faces/Twelve.png";
            case 13: return "Faces/Sorry.png";
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
        screen = 2;
        System.out.println("Winscreen");
        root.getChildren().clear();

        DrawBackground(Color.rgb(250,250,250));

        String s = "";
        switch(game.getWinner()){
            case 0: s = "Red"; break;
            case 1: s = "Orange"; break;
            case 2: s = "Yellow"; break;
            case 3: s = "Green"; break;
            case 4: s = "Blue"; break;
            case 5: s = "Purple"; break;
        }
        s += " is the winner!";

        DrawText(s,scene.getWidth()/2,64,80,true);

        //Draw points
        DrawText("Red: "+points[0],10,scene.getHeight()-50,40,false);
        DrawText("Orange: "+points[1],10,scene.getHeight()-10,40,false);
        DrawText("Yellow: "+points[2],10+(scene.getWidth()/3),scene.getHeight()-50,40,false);
        DrawText("Green: "+points[3],10+(scene.getWidth()/3),scene.getHeight()-10,40,false);
        DrawText("Blue: "+points[4],10+(2*scene.getWidth()/3),scene.getHeight()-50,40,false);
        DrawText("Purple: "+points[5],10+(2*scene.getWidth()/3),scene.getHeight()-10,40,false);

        //Again Button
        Button again = new Button();
        again.setLayoutX(650);
        again.setLayoutY(650);
        again.setMinSize(250,50);
        again.setMaxSize(250,50);
        again.setFont(getFont(20));
        again.setText("Play Again!");
        again.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                reset();
                update(primaryStage);
                System.out.println("Handle!");
            }
        });

        root.getChildren().add(again);

        primaryStage.setTitle(header);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Draw start screen
    public void StartScreen(Stage primaryStage, Sorry game) {
        screen = 0;
        System.out.println("Start screen");
        root.getChildren().clear();

        DrawBackground(Color.rgb(250,250,250));

        Image image = new Image("GUI/Title.png");
        ImageView imageView = new ImageView(image);
        imageView.setX(300);
        imageView.setY(-50);
        root.getChildren().add(imageView);

        //DrawText("Test",50,200,200,false);

        //Start Button
        Button again = new Button();
        again.setLayoutX(650);
        again.setLayoutY(650);
        again.setMinSize(250,50);
        again.setMaxSize(250,50);
        again.setFont(getFont(20));
        again.setText("Start!");
        again.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                update(primaryStage);
                System.out.println("Handle!");
            }
        });
        root.getChildren().add(again);

        primaryStage.setTitle(header);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Font getFont(int size){
        return Font.loadFont(getClass().getResourceAsStream("/Fonts/Comfortaa-Regular.ttf"),size);
    }

    public void DrawText(String string, double x, double y, int size, boolean center){
        Text text = new Text();
        text.setText(string);
        Font fnt = getFont(size);
        text.setFont(fnt);

        text.setY(y);
        if(center){
            text.setX(x-(text.getLayoutBounds().getWidth()/2));
        }else{
            text.setX(x);
        }

        root.getChildren().add(text);
    }

    public void DrawBackground(Color c){
        Rectangle r = new Rectangle();
        r.setX(0);
        r.setY(0);
        r.setWidth(scene.getWidth());
        r.setHeight(scene.getHeight());
        r.setFill(c);
        root.getChildren().add(r);
    }

    public void reset(){
        game = new Sorry();
    }
}