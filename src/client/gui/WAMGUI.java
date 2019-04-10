package client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

import static common.WAMProtocol.*;

public class WAMGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    public void init(){
            Parameters parameters = getParameters();
            List<String> params = parameters.getRaw();

    }
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        BorderPane border = new BorderPane();
        border.setCenter(grid);
        Scene scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Whack-A-Mole");

        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
