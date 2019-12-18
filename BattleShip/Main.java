package BattleShip;

import java.awt.*;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import BattleShip.Board;
import BattleShip.Ship;
import BattleShip.Board.Cell;

import static java.awt.Frame.MAXIMIZED_BOTH;

public class Main extends Application {

    static boolean aray[][] = new boolean [10][10];

    private boolean running = false;
    private Board enemyBoard, playerBoard;
    private boolean checker = false;

    private int shipsToPlace = 10;

    private boolean enemyTurn = false;

    private Random random = new Random();

    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(1000, 850);

        //root.setRight(new Text("Right Sidebar - Controls"));

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;

            enemyTurn = !cell.shoot();

            if (enemyBoard.ships == 0) {

                if(!checker) {
                    System.out.println("YOU WIN");

                    /*Text text = new Text(90, 420, "You win!");
                    text.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 60));
                    text.setFill(Color.CHOCOLATE);
                    text.setStroke(Color.BLACK);
                    text.setStrokeWidth(2);*/
                    //text.setUnderline(true);

                    //root.setCenter(text);
                    //root.getChildren().add(text);

                    //Make dialog window!!!

                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    VBox vbox = new VBox();
                    Button but = new Button("Ok");
                    but.setOnAction(event1 ->{
                        dialogStage.close();
                    });
                    vbox.getChildren().addAll(new Text("You win!"),but);
                    vbox.setAlignment(Pos.CENTER);
                    vbox.setPadding(new Insets(5));
                    dialogStage.setScene(new Scene(vbox));
                    dialogStage.showAndWait();

                    //System.exit(0);
                }

            }

            if (playerBoard.ships == 0) {

                if(checker) {

                    Text text = new Text(90, 420, "You lose!");
                    text.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 60));
                    text.setFill(Color.CHOCOLATE);
                    text.setStroke(Color.BLACK);
                    text.setStrokeWidth(2);
                    //text.setUnderline(true);

                    //root.setCenter(text);
                    root.getChildren().add(text);

                    //System.exit(0);a

                }

            }


            if (enemyTurn)
                enemyMove();
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                if (--shipsToPlace == 0) {
                    startGame();
                }
            }
        });

        VBox vbox = new VBox(70, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();

            if (playerBoard.ships == 0) {
                //System.out.println("You Lose");
                //Make dialog window!!!


                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                VBox vbox = new VBox();
                Button but = new Button("Ok");
                but.setOnAction(event1 ->{
                    dialogStage.close();
                });
                vbox.getChildren().addAll(new Text("You lose!"),but);
                vbox.setAlignment(Pos.CENTER);
                vbox.setPadding(new Insets(5));
                dialogStage.setScene(new Scene(vbox));
                dialogStage.showAndWait();

                checker = true;
            }
        }
    }

    private void startGame() {

        int type = 10;
        shipsToPlace = 10;

        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y)) {
                type--;
            }
        }

        running = true;
    }


    @Override
    public void start(Stage stage) {

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu playMenu = new Menu("Play");
        Menu serversMenu = new Menu("Servers");
        Menu settingsMenu = new Menu("Settings");
        Menu helpMenu = new Menu("Help");

        // Create MenuItems
        MenuItem newItem = new MenuItem("New game");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem complexityItem = new MenuItem("Complexity");

        MenuItem aboutItem = new MenuItem("About");

        // Add menuItems to the Menus
        playMenu.getItems().addAll(newItem, undoItem, exitItem);
        settingsMenu.getItems().addAll(complexityItem);
        helpMenu.getItems().addAll(aboutItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(playMenu, serversMenu, settingsMenu, helpMenu);

        // Set Accelerator for Exit MenuItem.
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        BorderPane root = new BorderPane();
        root.setTop(menuBar);

        Scene scene = new Scene(root, 1000, 850);

        stage.setTitle("BattleShip");
        stage.setScene(scene);
        stage.show();

        aboutItem.setOnAction(event -> {

        });

        undoItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BorderPane root = new BorderPane();
                root.setTop(menuBar);
                Scene scene = new Scene(root, 1000, 850);

                stage.setTitle("BattleShip");
                stage.setScene(scene);
                stage.show();
            }
        });

        newItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double width = screenSize.getWidth();
                double height = screenSize.getHeight();

                BorderPane root = new BorderPane(createContent());
                root.setTop(menuBar);
                Scene scene = new Scene(root, width, height);

                stage.setFullScreen(true);
                stage.setTitle("BattleShip");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();

            }
        });

        aboutItem.setOnAction(event ->{
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            VBox vbox = new VBox();
            Button but = new Button("Ok");
            but.setOnAction(event1 ->{
                dialogStage.close();
            });
            vbox.getChildren().addAll(new Text("This is Game \"BattleShip\" "
                    + "version 1.7\n (c) Copyright Vlad Fliagin - 2019"),but);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(5));
            dialogStage.setScene(new Scene(vbox));
            dialogStage.showAndWait();
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
