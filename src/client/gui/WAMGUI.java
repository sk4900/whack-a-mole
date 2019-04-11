package client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import static common.WAMProtocol.*;

/** responsible for creating the board
 *
 */
public class WAMGUI extends Application {

    /** List of command line arguments */
    private List<String> params;

    /**
     * Creates the client socket and connects to the server
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        boolean start = false;
        while (!start) {
            while (args.length < 2) {
                System.out.println("usage-> hostname #port" +
                        "\nenter arguments: ");
                Scanner in = new Scanner(System.in);
                args = in.nextLine().split(" ");
            }
            try {
                int port = Integer.parseInt(args[1]);
                Socket socket = new Socket(args[0], port);
                //give socket to player
                start = true;
            } catch (NumberFormatException nfe) {
                System.out.println("");
                args = new String[0];
            } catch (IOException ioe) {
                System.out.println("");
                args = new String[0];
            }
            launch(args);
        }
    }

    /**
     * processes command-line arguments
     */
    @Override
    public void init(){
            Parameters parameters = getParameters();
            this.params = parameters.getRaw();
            if (this.params.size() != 5){
                System.err.println("Exiting, needs 5 arguments to start");
            }
            System.exit(1);
    }

    /**
     * creates the board for whack-a-mole
     * Grid pane with buttons and a text area for displaying updates
     * @param primaryStage The stage
     */
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
                grid.add(button, j, i);
            }
        }
        grid.setGridLinesVisible(true);
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
