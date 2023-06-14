package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Login Setup Utility");
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public final static double width = 400;
    public final static double height = 400;
}
