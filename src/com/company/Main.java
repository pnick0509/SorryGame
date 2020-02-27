package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static javafx.scene.paint.Color.GREEN;


public class Main extends Application {

    public void start(Stage primaryStage){
        Sorry game = new Sorry();

        for(int i = 0; i < 3; i++) {
            update(primaryStage,game);
        }
    }

    public void update(Stage primaryStage, Sorry game) {

        //Grid and board parameters
        int rowNum = 16, colNum = 31, gridH = 16, gridW = 16;
        //Grid pane to hold the grid of squares, a borderbane to hold the grid or game board in
        GridPane squareGrid = new GridPane();
        BorderPane gameBoard = new BorderPane();
        Pawn[] screen = game.getBoard().getSpaces();

        //For loop that constructs the gameboard by going through rows/columns
        //As it goes through it checks for specific column and row values to put buttons in
        for(int row = 0; row<rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                if (    (row == 0) || (col == 0) || (row == 15) || (col == 30) || //The board itself
                        (col == 7 && row <= 6) || (col == 9 && row < 2) || // Top left Start & Home
                        (col == 22 && row <= 6) || (col == 24 && row < 2) || // Top right Start & Home
                        (col > 23 && row == 7) || (col == 29 && row == 9) || // Rightmost start & home
                        (col == 23 && row > 8) || ( col == 21 && row == 14) || //Bottom right start & home
                        (col == 8 && row > 8) || (col == 6 && row == 14) || // Bottom left start & home
                        (col < 7 && row == 8) || (col == 1 && row == 6) // Leftmost start & home
                        ) {

                    //Make a button, assign it the rectangle object/shape
                    Button bt = new Button();
                    Rectangle gameSquare = new Rectangle();


                    bt.setShape(gameSquare);
                    bt.setMaxSize(60, 60);
                    bt.setMinHeight(50);
                    bt.setMinWidth(50);

                    //Set the button action, make sure the row/col are final before we go in- as temp variables
                    int finalCol = col;
                    int finalRow = row;
                    bt.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle (ActionEvent event) {
                            int click = getInput(finalRow,finalCol);
                            System.out.println("Input: "+click);
                            if(click != -1 && game.takeTurn(click)){
                                update(primaryStage,game);
                            }
                        }
                    });

                    //Rectangle alterations
                    gameSquare.setHeight(60);
                    gameSquare.setWidth(60);
                    gameSquare.setStroke(Color.BLACK);
                    gameSquare.setStrokeWidth(3);
                    gameSquare.setFill(Color.WHITE);

                    squareGrid.setRowIndex(bt, row);
                    squareGrid.setColumnIndex(bt, col);

                    if(getInput(row,col) != -1) {
                        if (screen[getInput(row, col)] != null) {
                            Image image;
                            if(screen[getInput(row, col)].getPawnColor() == pColor.RED){
                                image = new Image(getClass().getResourceAsStream("Red.png"));
                            }else if(screen[getInput(row, col)].getPawnColor() == pColor.YELLOW){
                                image = new Image(getClass().getResourceAsStream("Yellow.png"));
                            }else if(screen[getInput(row, col)].getPawnColor() == pColor.GREEN){
                                image = new Image(getClass().getResourceAsStream("Green.png"));
                            }else if(screen[getInput(row, col)].getPawnColor() == pColor.BLUE){
                                image = new Image(getClass().getResourceAsStream("Blue.png"));
                            }else{
                                image = new Image(getClass().getResourceAsStream("Unknown.png"));
                            }
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(30);
                            imageView.setFitHeight(30);
                            bt.setGraphic(imageView);
                        }
                    }

                    squareGrid.getChildren().addAll(bt);
                }
            }
        }

        //Draw and position the card in screen
        Image cardImage = new Image(getNextCard(2));
        ImageView cardView = new ImageView(cardImage);
        cardView.setFitWidth(250);
        cardView.setFitHeight(350);
        cardView.setX(650);
        cardView.setY(225);


        Group root = new Group();

        //Put the card and board into Root, then add Root into scene later
        root.getChildren().addAll(cardView, gameBoard);
        //Put the gameboard into the borderpane
        gameBoard.setCenter(squareGrid);


        //Put the borderpane into the scene
        Scene scene = new Scene(root);


        //Construct the scene and launch
        primaryStage.setTitle("Sorry! v .01");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Gets the next card to draw
    public String getNextCard(int nextCard){
        switch(nextCard){
            case 1: return "One.png";
            case 2: return "Two.png";
            case 3: return "Three.png";
            case 4: return "4.png";
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

    public int getInput(int row, int col){
        if(row == 0) { //Top row
            return col;
        }else if(row == 15){
            return 75-col;
        }else if(col == 0){
            return 90-row;
        }else if(col == 30){
            return 30+row;
        }else if(col == 7 && row <= 5){ //Red Home
            return row+89;
        }else if(col == 22 && row <= 5){ //Orange Home
            return row+94;
        }else if(row == 7 && col >= 25){ //Yellow Home
            return 104-col+25;
        }else if(col == 23 && row >= 10){ //Green Home
            return 109-row+10;
        }else if(col == 8 && row >= 10){ //Blue Home
            return 114-row+10;
        }else if(row == 8 && col <= 5){
            return 114+col;
        }else{
            return -1;
        }
        /*if (row == 0) { //Top row
            return col;
        } else if (row == 15) { //Bottom row
            return 75-col;
        } else if (col == 15) { //Right col
            return row+15;
        } else if (col == 0) { //Left col
            return 60-row;
        } else if (col == 2 && row <=5){ // Top Left Home
            return 59+row;
        } else if (row == 13 && col <= 5) { // Bottom left home
            return 74+col;
        } else if (col == 13 && row >= 10){ // Bottom Right Home
            return 84-row;
        } else if (col >= 10 && row == 2 ){
            return 79-col;
        } else {
            return -1; //Error case
        }*/
    }
}