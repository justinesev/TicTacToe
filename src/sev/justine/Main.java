package sev.justine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gameLayout.fxml"));
        primaryStage.setTitle("Tic Tac Toe - Justīne");
        primaryStage.setScene(new Scene(root, 500, 600));
        root.getStylesheets().add(getClass().getResource("Style.css").toString());
        primaryStage.show();
    }
        public static void main(String[] args) {
        launch(args);
    }
}
