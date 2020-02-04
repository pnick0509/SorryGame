package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;


public class Main extends Application {

    public void start(Stage primaryStage) {
        int rowNum = 16, colNum = 16, gridH = 16, gridW = 16;
        GridPane squareGrid = new GridPane();
        BorderPane gameBoard = new BorderPane();



        Random rand = new Random();
        Color[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED};

        for(int row = 0; row<rowNum; row++){
            for(int col = 0; col<colNum; col++){
                int n = rand.nextInt(4);
                Rectangle gameSquare = new Rectangle();
                gameSquare.setHeight(60);
                gameSquare.setWidth(60);
                gameSquare.setStroke(Color.BLACK);
                gameSquare.setStrokeWidth(3);
                gameSquare.setFill(Color.WHITE);
                squareGrid.setRowIndex(gameSquare, row);
                squareGrid.setColumnIndex(gameSquare, col);
                squareGrid.getChildren().addAll(gameSquare);
            }
        }

        gameBoard.setCenter(squareGrid);

        Scene scene = new Scene(gameBoard);

        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();



    }
}
