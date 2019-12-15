package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("NASA APOD");
        Parent root = FXMLLoader.load(getClass().getResource("APOD.fxml"));
        primaryStage.setScene(new Scene(root, 860, 610));
        primaryStage.setMinWidth(860);
        primaryStage.setMinHeight(710);
        primaryStage.setMaxWidth(860);
        primaryStage.setMaxHeight(710);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("NASA Logo.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
