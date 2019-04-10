package client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

import static common.WAMProtocol.*;

public class WAMGUI extends Application {

    private List<String> params;

    public static void main(String[] args) {
        launch(args);
    }
    public void init(){
            Parameters parameters = getParameters();
            this.params = parameters.getRaw();

    }
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        int rows = Integer.parseInt(this.params.get(1));
        int columns = Integer.parseInt(this.params.get(2));
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                Button button = new Button();
                button.setMaxHeight(100);
                button.setMaxWidth(100);
                button.setMinHeight(100);
                button.setMinWidth(100);
                button.setText("Mole");
                grid.add(button, j, i);
            }
        }
        BorderPane border = new BorderPane();
        TextArea text = new TextArea();
        text.setEditable(false);
        text.setText("Welcome to Whack-A-Mole");

        text.setMaxWidth(grid.getColumnCount() * 100);
        text.setMaxHeight(100);
        border.setBottom(text);
        border.setCenter(grid);
        Scene scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Whack-A-Mole");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
