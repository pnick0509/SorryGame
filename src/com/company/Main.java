package com.company;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    Sorry game;
    Group root = new Group();
    int rowNum = 16, colNum = 31, squareSize = 50;
    Scene scene = new Scene(root, colNum*squareSize, rowNum*squareSize);
    int[] points = new int[6];
    int screen;
    String header = "Sorry! Cycle 3.3";
    int[] playerSetting = new int[6]; //0 for off, 1 for player, 2 for computer, 3 for "hard" computer

    TextField[] nameField = new TextField[6];
    String[] playerNames = new String[6];

    String cardFrontImage = "";
    String cardBackImage = "";
    boolean cardUpdated = false;

    ArrayList<Pawn> pawnList = new ArrayList<>();
    Timer pawnTimer = new Timer(0);

    Ai aiQueued = null;

    Stage pStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        game = new Sorry(this);

        pStage = primaryStage;

        //Set up settings
        playerSetting[0] = 1;
        playerSetting[1] = 2;
        playerSetting[2] = 0;
        playerSetting[3] = 0;
        playerSetting[4] = 0;
        playerSetting[5] = 0;

        //Names
        for(int n = 0; n < 6; n++){
            playerNames[n] = "Player "+(n+1);
        }

        //Set points
        for(int i = 0; i < 6; i++){
            points[i] = 0;
        }

        StartScreen(primaryStage,game);
        //update(primaryStage);

        scene.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && screen == 1 && aiQueued == null) {
                System.out.print("("+e.getX()+", "+e.getY()+") ");
                System.out.println(getInput((int)e.getY()/squareSize,(int)e.getX()/squareSize));

                game.takeTurn(getInput((int)e.getY()/squareSize,(int)e.getX()/squareSize));
                if(game.getWinner() == -1){
                    update(primaryStage);
                }else{
                    doWin();
                }
            }
        });

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

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(pawnTimer.checkTime() && screen == 1){
                    //System.out.println("PAWNLIST B"+pawnList);
                    if(pawnList.size() > 0){
                        for(int i = 0; i < pawnList.size(); i++){
                            if(pawnList.get(i).animate()){
                                pawnList.remove(i);
                                i--;
                            }
                        }
                    }else{
                        for(int i = 0; i <= 119; i++){
                            if(game.getBoard().getSpaces()[i] != null){
                                game.getBoard().getSpaces()[i].ensurePosition(i);
                            }
                        }
                        //System.out.println(aiQueued);
                        if(aiQueued != null){
                            Timer wait = new Timer(0.5);
                            while(!wait.checkTime());
                            System.out.println("Taking turn with AI: "+aiQueued.getPlayer());
                            aiQueued.taketurn(game.getCard());
                        }
                    }
                    pawnTimer.set(0.1);
                }
                if(!cardUpdated && pawnList.isEmpty() && screen == 1){
                    updateCardImage();
                    System.out.println("CarD");
                }
            }
        }.start();
    }

    public void doWin(){
        //Add points
        for(int i = 0; i < 6; i++){
            if(i != game.getWinner()){
                points[i] += game.getBoard().homeAmount(i)*5;
            }else{
                points[i] += game.getBoard().homeAmount(i)*10;
            }
        }
        //Show screen
        WinScreen(pStage,game);
    }

    public void queueAi(Ai a){
        aiQueued = a;
    }

    public void update(){
        update(pStage);
    }

    public void update(Stage primaryStage) {
        cardUpdated = false;

        screen = 1;
        System.out.println("Update");
        root.getChildren().clear();

        DrawBackground(Color.rgb(250,250,250));
        placeImage(0,0,"Backdrop/MarbleBlackMark.png");

        pawnList.clear();
        for(int row = 0; row<rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                if (getInput(row,col) != -1){
                    //Draw Square
                    placeTile(row,col,spaceImage(getInput(row,col)));
                    if(game.getOptions().contains(getInput(row,col))){
                        placeTile(row,col,"Board/Selection.png");
                    }

                    if(getInput(row,col) <= 119){
                        if(game.getBoard().getSpaces()[getInput(row,col)] != null){
                            //placeTile(row,col,pawnImage(getInput(row,col)));
                            //drawPawn(getInput(row,col));
                            root.getChildren().add(game.getBoard().getSpaces()[getInput(row,col)].getIv());
                            pawnList.add(game.getBoard().getSpaces()[getInput(row,col)]);
                            //System.out.println("PAWNLIST A"+pawnList);
                            if(getInput(row,col) == game.getSelected()){
                                placeTile(row,col,"Default/Selector.png");
                            }
                        }
                    }

                    //Draw numbers
                    //Starts
                    if(col == 9 && row == 1 && playerSetting[0] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().startAmount(0)+".png");
                    }else if(col == 24 && row == 1 && playerSetting[1] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().startAmount(1)+".png");
                    }else if(col == 29 && row == 9 && playerSetting[2] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().startAmount(2)+".png");
                    }else if(col == 21 && row == 14 && playerSetting[3] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().startAmount(3)+".png");
                    }else if(col == 6 && row == 14 && playerSetting[4] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().startAmount(4)+".png");
                    }else if(col == 1 && row == 6 && playerSetting[5] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().startAmount(5)+".png");
                    }
                    //Homes
                    else if(col == 7 && row == 6 && playerSetting[0] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().homeAmount(0)+".png");
                    }else if(col == 22 && row == 6 && playerSetting[1] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().homeAmount(1)+".png");
                    }else if(col == 24 && row == 7 && playerSetting[2] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().homeAmount(2)+".png");
                    }else if(col == 23 && row == 9 && playerSetting[3] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().homeAmount(3)+".png");
                    }else if(col == 8 && row == 9 && playerSetting[4] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().homeAmount(4)+".png");
                    }else if(col == 6 && row == 8 && playerSetting[5] != 0){
                        placeTile(row, col, "Board/"+game.getBoard().homeAmount(5)+".png");
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
                    if(aiQueued == null){
                        game.skipTurn();
                        System.out.println("Handle Skip!");
                        update(primaryStage);
                        pawnTimer.set(0.1);
                    }
                }
            });
        }else{
            skip.setText("Deselect Pawn");
            skip.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    if(aiQueued == null){
                        game.deselectPawn();
                        update(primaryStage);
                    }
                }
            });
        }
        root.getChildren().add(skip);

        //Draw and position the card in screen
        if(cardBackImage == ""){
            updateCardImage();
        }else{
            Image cardBack = new Image(cardBackImage);
            ImageView backview = new ImageView(cardBack);
            backview.setFitWidth(250);
            backview.setFitHeight(350);
            backview.setX(650);
            backview.setY(225);
            root.getChildren().add(backview);

            Image cardImage = new Image(cardFrontImage);
            ImageView cardView = new ImageView(cardImage);
            cardView.setFitWidth(250);
            cardView.setFitHeight(350);
            cardView.setX(650);
            cardView.setY(225);
            root.getChildren().add(cardView);
        }

        //scene = new Scene(root, colNum*squareSize, rowNum*squareSize);
        primaryStage.setTitle(header);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //Returns the image to represent the space the space
    public String spaceImage(int index){
        String s = "Board/";
        if((index >= 90 && index <= 94) || index == 120 || index == 126){
            if(playerSetting[0] != 0){
                s+= "Square_Red";
                if(game.getColorblind()){
                    s += "_CB";
                }
            }else{
                s+= "Disabled";
            }
        }else if((index >= 95 && index <= 99) || index == 121 || index == 127){
            if(playerSetting[1] != 0){
                s+= "Square_Orange";
                if(game.getColorblind()){
                    s += "_CB";
                }
            }else{
                s+= "Disabled";
            }
        }else if((index >= 100 && index <= 104) || index == 122 || index == 128){
            if(playerSetting[2] != 0){
                s+= "Square_Yellow";
                if(game.getColorblind()){
                    s += "_CB";
                }
            }else{
                s+= "Disabled";
            }
        }else if((index >= 105 && index <= 109) || index == 123 || index == 129){
            if(playerSetting[3] != 0){
                s+= "Square_Green";
                if(game.getColorblind()){
                    s += "_CB";
                }
            }else{
                s+= "Disabled";
            }
        }else if((index >= 110 && index <= 114) || index == 124 || index == 130){
            if(playerSetting[4] != 0){
                s+= "Square_Blue";
                if(game.getColorblind()){
                    s += "_CB";
                }
            }else{
                s+= "Disabled";
            }
        }else if((index >= 115 && index <= 119) || index == 125 || index == 131){
            if(playerSetting[5] != 0){
                s+= "Square_Purple";
                if(game.getColorblind()){
                    s += "_CB";
                }
            }else{
                s+= "Disabled";
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
    public void placeTile(int row, int col, String img){
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

    public void placeImage(int x, int y, String img){
        try{
            Image image = new Image(img);
            ImageView imageView = new ImageView(image);
            imageView.setX(x);
            imageView.setY(y);
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
        s += playerNames[game.getWinner()];
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
        again.setLayoutX(450);
        again.setLayoutY(650);
        again.setMinSize(250,50);
        again.setMaxSize(250,50);
        again.setFont(getFont(20));
        again.setText("Play Again!");
        again.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                game.reset();
                update(primaryStage);
                System.out.println("Handle!");
            }
        });
        root.getChildren().add(again);

        //Again Button
        Button stat = new Button();
        stat.setLayoutX(850);
        stat.setLayoutY(650);
        stat.setMinSize(250,50);
        stat.setMaxSize(250,50);
        stat.setFont(getFont(20));
        stat.setText("View Stats!");
        stat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                StatScreen(pStage,game);
                System.out.println("Handle!");
            }
        });
        root.getChildren().add(stat);

        primaryStage.setTitle(header);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Draw start screen
    public void StartScreen(Stage primaryStage, Sorry game) {
        try{
            for(int i = 0; i < 6; i++){
                playerNames[i] = nameField[i].getText();
            }
        }catch (Exception e){

        }

        screen = 0;
        System.out.println("Start screen");
        root.getChildren().clear();

        DrawBackground(Color.rgb(250,250,250));

        Image image = new Image("GUI/Title.png");
        ImageView imageView = new ImageView(image);
        imageView.setX(300);
        imageView.setY(-50);
        root.getChildren().add(imageView);

        Button btn;
        for(int i = 0; i < 6; i++){
            btn = new Button();
            btn.setLayoutX(20);
            btn.setLayoutY(200+(75*i));
            btn.setMinSize(200,50);
            btn.setMaxSize(200,50);
            btn.setFont(getFont(20));
            String txt = "";
            switch(i){
                case 0: txt += "Red"; break;
                case 1: txt += "Orange"; break;
                case 2: txt += "Yellow"; break;
                case 3: txt += "Green"; break;
                case 4: txt += "Blue"; break;
                case 5: txt += "Purple"; break;
            }
            txt += ": ";
            switch(playerSetting[i]){
                case 0: txt += "Off"; break;
                case 1: txt += "Human"; break;
                case 2: txt += "Easy"; break;
                case 3: txt += "Hard"; break;
            }
            btn.setText(txt);
            int k = i;
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    playerSetting[k]++;
                    if(playerSetting[k] > 3){
                        playerSetting[k] = 0;
                    }
                    StartScreen(primaryStage,game);
                    System.out.println("Handle!");
                }
            });
            root.getChildren().add(btn);

            for(int n = 0; n < 6; n++){
                nameField[n] = new TextField();
                nameField[n].setPromptText("Player "+(n+1)+"'s Name");
                nameField[n].setText(playerNames[n]);
                nameField[n].setTranslateX(240);
                nameField[n].setTranslateY(200+(75*n)+10);
                nameField[n].setMinHeight(50-20);
                nameField[n].setMaxHeight(50-20);
                nameField[n].setMinWidth(250);
                nameField[n].setMaxWidth(250);
                root.getChildren().add(nameField[n]);
            }
        }

        //Draw options
        //Colorblind
        btn = new Button();
        btn.setLayoutX(1330-100);
        btn.setLayoutY(200);
        btn.setMinSize(200+100,50);
        btn.setMaxSize(200+100,50);
        btn.setFont(getFont(20));
        String txt = "Color Blind: ";
        if(game.getColorblind()){
            txt += "On";
        }else{
            txt += "Off";
        }
        btn.setText(txt);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                game.toggleColorblind();
                StartScreen(primaryStage,game);
                System.out.println("Handle!");
            }
        });
        root.getChildren().add(btn);

        //Infinite Deck
        btn = new Button();
        btn.setLayoutX(1330-100);
        btn.setLayoutY(200+75);
        btn.setMinSize(200+100,50);
        btn.setMaxSize(200+100,50);
        btn.setFont(getFont(20));
        txt = "Infinite Deck: ";
        if(!game.getCardCheats()){
            txt += "On";
        }else{
            txt += "Off";
        }
        btn.setText(txt);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                game.toggleCardCheats();
                StartScreen(primaryStage,game);
                System.out.println("Handle!");
            }
        });
        root.getChildren().add(btn);

        //Pawn Start Out
        btn = new Button();
        btn.setLayoutX(1330-100);
        btn.setLayoutY(200+75*2);
        btn.setMinSize(200+100,50);
        btn.setMaxSize(200+100,50);
        btn.setFont(getFont(20));
        txt = "Start Pawn Out: ";
        if(game.getPawnStartOut()){
            txt += "On";
        }else{
            txt += "Off";
        }
        btn.setText(txt);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                game.togglePawnStartOut();
                StartScreen(primaryStage,game);
                System.out.println("Handle!");
            }
        });
        root.getChildren().add(btn);

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
                if(playerSetting[0] == 1 || playerSetting[1] == 1 || playerSetting[2] == 1 || playerSetting[3] == 1 || playerSetting[4] == 1 || playerSetting[5] == 1){
                    //Set up options
                    for(int b = 0; b < 6; b++){
                        //Establish turn order
                        if(playerSetting[b] == 1){
                            game.addTurnOrder(b);
                        }else if(playerSetting[b] == 2){
                            game.addTurnOrder(b);
                            game.newAI(b,false);
                        }else if(playerSetting[b]==3){
                            game.addTurnOrder(b);
                            game.newAI(b,true);
                        }
                        //Set name
                        playerNames[b] = nameField[b].getText();
                        System.out.println("b:: "+nameField[b].getText());
                    }

                    if(game.getPawnStartOut()){
                        game.presetPawnStart();
                    }

                    int c;
                    Random r = new Random();
                    do{
                        c = Math.abs(r.nextInt()%6);
                    }while(playerSetting[c] == 0);
                    game.setTurn(c);
                    System.out.println("Start Turn "+c);
                    game.startOptions();
                    Ai a = game.getAiTurn();
                    if(a != null){
                        aiQueued = a;
                        //a.taketurn(game.getCard());
                    }
                    System.out.println("Turn"+a);
                    //Display board
                    update(primaryStage);
                    System.out.println("Handle!");
                }
            }
        });
        root.getChildren().add(again);

        primaryStage.setTitle(header);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void StatScreen(Stage primaryStage, Sorry game) {
        screen = 3;
        System.out.println("Stats");
        root.getChildren().clear();

        DrawBackground(Color.rgb(250,250,250));
        placeImage(0,0,"Backdrop/Statscreen.png");

        DrawText("Player Names",115,42,20,true);
        for(int i = 0; i < 6; i++){
            DrawText(playerNames[i],115+220*(i+1),42,20,true);
        }
        DrawText("Spaces Forward",115,42+50,20,true);
        for(int i = 0; i < 6; i++){
            DrawText(""+game.getStat_forward()[i],115+220*(i+1),42+50,20,true);
        }
        DrawText("Spaces Backward",115,42+50*2,20,true);
        for(int i = 0; i < 6; i++){
            DrawText(""+game.getStat_backward()[i],115+220*(i+1),42+50*2,20,true);
        }
        DrawText("Spaces Slid",115,42+50*3,20,true);
        for(int i = 0; i < 6; i++){
            DrawText(""+game.getStat_slide()[i],115+220*(i+1),42+50*3,20,true);
        }

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
                game.reset();
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

    public void updateCardImage(){
        cardUpdated = true;

        cardBackImage = setNextBack(game.getTurn());
        Image cardBack = new Image(cardBackImage);
        ImageView backview = new ImageView(cardBack);
        backview.setFitWidth(250);
        backview.setFitHeight(350);
        backview.setX(650);
        backview.setY(225);
        root.getChildren().add(backview);

        cardFrontImage = setNextCard(game.getCard());
        Image cardImage = new Image(cardFrontImage);
        ImageView cardView = new ImageView(cardImage);
        cardView.setFitWidth(250);
        cardView.setFitHeight(350);
        cardView.setX(650);
        cardView.setY(225);
        root.getChildren().add(cardView);
    }
}