package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Main extends Application {

    public void start(Stage primaryStage){
        Sorry game = new Sorry();

        for(int i = 0; i < 3; i++) {
            update(primaryStage,game);
        }
    }

    public void update(Stage primaryStage, Sorry game) {

        //Grid and board parameters
        int rowNum = 16, colNum = 16, gridH = 16, gridW = 16;
        //Grid pane to hold the grid of squares, a borderbane to hold the grid or game board in
        GridPane squareGrid = new GridPane();
        BorderPane gameBoard = new BorderPane();
        Pawn[] screen = game.getBoard().getSpaces();

        //For loop that constructs the gameboard by going through rows/columns
        //As it goes through it checks for specific column and row values to put buttons in
        for(int row = 0; row<rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                if ((row == 0) || (col == 0) || (row == 15) || (col == 15) || (col == 2 && row < 7) || (row == 13 && col < 7) || (col == 13 && row > 8) || (row == 2 && col > 8) ||
                        (col == 4 && row == 1) || (row == 4 && col == 14) || (row == 14 && col == 11) || (col == 1 && row == 11)) {

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
                                image = new Image(getClass().getResourceAsStream("Red.png"));
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


        //Put the gameboard into the borderpane
        gameBoard.setCenter(squareGrid);

        //Put the borderpane into the scene
        Scene scene = new Scene(gameBoard);

        //Construct the scene and launch
        primaryStage.setTitle("Sorry! v .01");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public int getInput(int row, int col){
        if (row == 0) { //Top row
            return col;
        } else if (row == 15) { //Bottom row
            return 45-col;
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
        }
    }
}