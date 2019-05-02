package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class Controller {

    @FXML
    private VBox game;
    @FXML
    private HBox hBox;
    @FXML
    private GridPane field;
    @FXML
    private Label gameOverLabel;
    @FXML
    private Button playAgain;
    @FXML
    private Button resetResults;
    @FXML
    private Label playerXWins;
    @FXML
    private Label playerOWins;
    @FXML
    private Button playWithX;
    @FXML
    private Button playWithO;

    private static final String ICON_X = "X";
    private static final String ICON_O = "O";
    private String playerSign = "";
    private int xWins = 0;
    private int oWins = 0;
    private int counter = 1;
    private String id = "";
    private String[][] arrayOfResults = new String[3][3];
    private boolean isComputerPlayer;
    private boolean isGameOver;
    private boolean playerGoes = true;


    public void createFieldForTwo() {
        isComputerPlayer = false;
        game.getChildren().remove(hBox);
        createField();
    }

    public void createFieldForOne() {
        game.getChildren().remove(hBox);
        playWithO.setVisible(true);
        playWithX.setVisible(true);
        isComputerPlayer = true;
    }

    public void createField() {
        playerGoes = true;
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
        if (!isComputerPlayer) {
            personMakesMove(btn);
        } else if (playerGoes) {
            playerGoes = false;
            personMakesMove(btn);
            if (!isGameOver) {
                aiMakesMove();
            }
        }
    }

    private void personMakesMove(Button btn) {
        btn.setText(determineIcon());
        btn.setDisable(true);

        String[] coords = btn.getId().split(",");
        int coordsX = parseInt(coords[0]);
        int coordsY = parseInt(coords[1]);
        arrayOfResults[coordsX][coordsY] = determineIcon();

        checkIfGameOver();
        counter++;

    }

    private void aiDeterminesButton() {
        for (int i = 0; i < arrayOfResults.length; i++) {
            if (Arrays.stream(arrayOfResults[i]).anyMatch(playerSign::equals)) {
                for (int j = 0; j < arrayOfResults[i].length; j++) {
                    if (arrayOfResults[i][j] == null) {
                        id = "#" + i + "," + j;
                        System.out.println("id = " + id);
                    }
                }
            } else {
                id = "#" + 1 + "," + 1;

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
                        System.out.println("computer id = " + id);
                        Button btn = (Button) field.lookup(id);

                        btn.setText(determineIcon());
                        btn.setDisable(true);

                        String[] coords = btn.getId().split(",");
                        int coordsX = parseInt(coords[0]);
                        int coordsY = parseInt(coords[1]);
                        arrayOfResults[coordsX][coordsY] = determineIcon();

                        checkIfGameOver();
                        counter++;
                        playerGoes = true;
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

    public void setPlayerSign(ActionEvent actionEvent) {
        playerSign = ((Button) actionEvent.getSource()).getText();
        playWithO.setVisible(false);
        playWithX.setVisible(false);
        createField();
        if (playerSign.equals("O")) {
            playerGoes = false;
            aiMakesMove();
        }
    }
}