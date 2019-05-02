package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

public class Controller {

    public VBox game;
    public HBox hBox;
    public GridPane field;
    public Label gameOverLabel;
    public Button playAgain;
    public Button resetResults;
    public Label playerXWins;
    public Label playerOWins;

    public static final String ICON_X = "X";
    public static final String ICON_O = "O";
    private int xWins = 0;
    private int oWins = 0;
    public int counter = 1;
    private String id = "";
    private String[][] arrayOfResults = new String[3][3];
    public Boolean twoPlayers;
    public Boolean isGameOver;


    public void createFieldForTwo() {
        twoPlayers = true;
        createField();
    }

    public void createFieldForOne() {
        twoPlayers = false;
        createField();
    }

    public void createField() {
        game.getChildren().remove(hBox);
        isGameOver = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button btn = new Button();
                btn.setId(i + "," + j);
                btn.setOnAction((event) -> {
                    makeMove(btn);
                });
                field.add(btn, i, j);
            }
        }
    }

    private void makeMove(Button btn) {
        btn.setText(determineIcon());
        btn.setDisable(true);

        String[] coords = btn.getId().split(",");
        int coordsX = parseInt(coords[0]);
        int coordsY = parseInt(coords[1]);
        arrayOfResults[coordsX][coordsY] = determineIcon();

        checkIfGameOver();
        counter++;

        if (twoPlayers == false && isGameOver == false) {
            aiMakesMove();

        }
    }

    private void aiDeterminesButton() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arrayOfResults[i][j] == null) {
                    id = "#" + i + "," + j;
                }
            }
        }
    }

    private void aiMakesMove() {
        Runnable task = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        aiDeterminesButton();
                        Button btn = (Button) field.lookup(id);

                        btn.setText(determineIcon());
                        btn.setDisable(true);

                        String[] coords = btn.getId().split(",");
                        int coordsX = parseInt(coords[0]);
                        int coordsY = parseInt(coords[1]);
                        arrayOfResults[coordsX][coordsY] = determineIcon();

                        checkIfGameOver();

                        counter++;
                    }
                });
            }
        };
        new Thread(task).start();

    }

    public String determineIcon() {
        if (counter % 2 == 0) {
            return ICON_O;
        } else return ICON_X;
    }

    public void checkIfGameOver() {
        if (rowFilled() || columnFilled() || diagonalFilled()) {
            isGameOver = true;
            gameOverLabel.setText(determineIcon() + " won");
            addResult();
            createRestartButton();

        } else if (counter == 9) {
            isGameOver = true;
            gameOverLabel.setText("It's a tie!");
            xWins++;
            oWins++;
            createRestartButton();
        }
    }

    private void addResult() {
        if (determineIcon().equals("X")) {
            xWins++;
        } else {
            oWins++;
        }
        playerOWins.setVisible(true);
        playerXWins.setVisible(true);
        resetResults.setVisible(true);
        playerXWins.setText("X wins: " + xWins);
        playerOWins.setText("O wins: " + oWins);
    }

    public boolean rowFilled() {
        for (int i = 0; i < arrayOfResults.length; i++) {
            if (arrayOfResults[i][0] != null) {
                if (arrayOfResults[i][0] == arrayOfResults[i][1] && arrayOfResults[i][0] == arrayOfResults[i][2]) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean columnFilled() {
        for (int i = 0; i < arrayOfResults.length; i++) {
            if (arrayOfResults[0][i] != null) {
                if (arrayOfResults[0][i] == arrayOfResults[1][i] && arrayOfResults[0][i] == arrayOfResults[2][i]) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean diagonalFilled() {
        if (arrayOfResults[1][1] != null) {
            if (arrayOfResults[0][0] == arrayOfResults[1][1] && arrayOfResults[0][0] == arrayOfResults[2][2] ||
                    arrayOfResults[0][2] == arrayOfResults[1][1] && arrayOfResults[0][2] == arrayOfResults[2][0]) {
                return true;
            }
        }
        return false;
    }

    private void createRestartButton() {
        field.setDisable(true);
        playAgain.setVisible(true);
        playAgain.setOnAction((event -> {
            restartGame();
            playAgain.setVisible(false);
        }
        ));
    }

    private void restartGame() {
        game.getChildren().add(hBox);
        field.setDisable(false);
        field.getChildren().clear();
        counter = 1;
        arrayOfResults = new String[3][3];
        gameOverLabel.setText("");
    }

    public void resetResults(ActionEvent actionEvent) {
        xWins = 0;
        oWins = 0;
        playerXWins.setText("X wins: " + xWins);
        playerOWins.setText("O wins: " + oWins);
    }
}