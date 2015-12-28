package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception{


    }


    public static void main(String[] args) {
        ping pingNew = new ping();
        pingNew.pinger();
        launch(args);

    }
}
