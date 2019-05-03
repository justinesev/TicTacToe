package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static java.lang.Integer.parseInt;

public class Controller {

    @FXML
    private VBox game;
    @FXML
    private HBox hBox;
    @FXML
    private HBox hBoxXO;
    @FXML
    private GridPane field;
    @FXML
    private Label gameOverLabel;
    @FXML
    private Button playAgain;
    @FXML
    private Button resetResults;
    @FXML
    private Button restartGame;
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
    private static final int COMPUTER_THINKING_TIME = 1000;
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
        game.getChildren().remove(hBoxXO);
        createField();
    }

    public void createFieldForOne() {
        game.getChildren().remove(hBox);
        playWithO.setVisible(true);
        playWithX.setVisible(true);
        if (!game.getChildren().contains(hBoxXO)) {
            game.getChildren().add(hBoxXO);
        }
        isComputerPlayer = true;
    }

    public void createField() {
        restartGame.setVisible(true);
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
        System.out.println("coordsX = " + coordsX);
        System.out.println("coordsY = " + coordsY);
        arrayOfResults[coordsX][coordsY] = determineIcon();
        checkIfGameOver();
        counter++;
    }

    private void aiDeterminesButton() {
        if (!findsTwoEqualSignsInARow() && !findsTwoEqualSignsInAColumn()) {
            id = generateRandomID();
        }
        System.out.println("id is found = " + id);
    }

    private boolean findsTwoEqualSignsInARow() {
        for (int i = 0; i < arrayOfResults.length; i++) {
            if (arrayOfResults[0][i] != null) {
                if (arrayOfResults[0][i] == arrayOfResults[1][i] && arrayOfResults[2][i] == null) {
                    id = "#" + 2 + "," + i;
                    return true;
                } else if (arrayOfResults[0][i] == arrayOfResults[2][i] && arrayOfResults[1][i] == null) {
                    id = "#" + 1 + "," + i;
                    return true;
                }
            } else if (arrayOfResults[1][i] != null) {

                if (arrayOfResults[1][i] == arrayOfResults[2][i] && arrayOfResults[0][i] == null) {
                    id = "#" + 0 + "," + i;
                    return true;
                }
            }
        }
        return false;
    }

//    private boolean findsTwoEqualSignsInDiagonal() {
//        for (int i = 0; i < arrayOfResults.length; i++) {
//            for (int j = 0; j < arrayOfResults.length; j++) {
//                if (i != j) {
//                    if (arrayOfResults[i][i] == arrayOfResults[j][j]) {
//
//                    }
//                }
//            }
//        }
//    }


    private boolean findsTwoEqualSignsInAColumn() {
        for (int i = 0; i < arrayOfResults.length; i++) {
            if (arrayOfResults[i][0] != null) {
                if (arrayOfResults[i][0] == arrayOfResults[i][1] && arrayOfResults[i][2] == null) {
                    id = "#" + i + "," + 2;
                    return true;
                } else if (arrayOfResults[i][0] == arrayOfResults[i][2] && arrayOfResults[i][1] == null) {
                    id = "#" + i + "," + 1;
                    return true;
                }
            } else if (arrayOfResults[i][1] != null) {

                if (arrayOfResults[i][1] == arrayOfResults[i][2] && arrayOfResults[i][0] == null) {
                    id = "#" + i + "," + 0;
                    return true;
                }
            }
        }
        return false;
    }

    private String generateRandomID() {
        while (true) {
            int x = (int) (Math.random() * 3);
            int y = (int) (Math.random() * 3);
            System.out.println("#" + x + "," + y);
            if (arrayOfResults[x][y] == null) {
                return "#" + x + "," + y;
            }
        }
    }

    private void aiMakesMove() {
        Runnable task = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(COMPUTER_THINKING_TIME);
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
            if (determineIcon().equals("X")) {
                xWins++;
            } else {
                oWins++;
            }
            showResultsAndReset();
            createRestartButton();

        } else if (counter == 9) {
            isGameOver = true;
            gameOverLabel.setText("It's a tie!");
            xWins++;
            oWins++;
            showResultsAndReset();
            createRestartButton();
        }
    }

    private void showResultsAndReset() {
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
        restartGame.setVisible(false);
        playAgain.setOnAction((event -> {
            restartGame();
            playAgain.setVisible(false);

        }
        ));
    }

    @FXML
    private void restartGame() {
        game.getChildren().add(hBox);
        field.setDisable(false);
        field.getChildren().clear();
        counter = 1;
        arrayOfResults = new String[3][3];
        gameOverLabel.setText("");
        restartGame.setVisible(false);
    }

    public void resetResults(ActionEvent actionEvent) {
        xWins = 0;
        oWins = 0;
        playerXWins.setText("X wins: " + xWins);
        playerOWins.setText("O wins: " + oWins);
    }

    public void setPlayerSign(ActionEvent actionEvent) {
        playerSign = ((Button) actionEvent.getSource()).getText();
        game.getChildren().remove(hBoxXO);
        createField();
        if (playerSign.equals("O")) {
            playerGoes = false;
            aiMakesMove();
        }
    }
}