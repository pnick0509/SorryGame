package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;



public class Main extends Application {

    public void start(Stage primaryStage) {

        //Grid and board parameters
        int rowNum = 16, colNum = 16, gridH = 16, gridW = 16;
        //Grid pane to hold the grid of squares, a borderbane to hold the grid or game board in
        GridPane squareGrid = new GridPane();
        BorderPane gameBoard = new BorderPane();

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
                                if (finalRow == 0) { //Top row
                                    System.out.println(" " + finalCol);
                                } else if (finalRow == 15) { //Bottom row
                                    System.out.println(" " + (45 - finalCol));
                                } else if (finalCol == 15) { //Right col
                                    System.out.println(" " + (finalRow + 15));
                                } else if (finalCol == 0) { //Left col
                                    System.out.println(" " + (60 - finalRow));
                                } else if (finalCol == 2 && finalRow <=5){ // Top Left Home
                                    System.out.println(" " + (59 + finalRow));
                                } else if (finalRow == 13 && finalCol <= 5) { // Bottom left home
                                    System.out.println(" " + (74 + finalCol));
                                } else if (finalCol == 13 && finalRow >= 10){
                                    System.out.println(" " + (84 - finalRow)); // Bottom Right Home
                                } else if (finalCol >= 10 && finalRow == 2 ){
                                    System.out.println(" " + (79 - finalCol));
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
}
