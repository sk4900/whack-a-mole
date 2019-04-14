package client.gui;

import common.WAMBoard;
import javafx.application.Application;
import javafx.application.Platform;
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
 * @author you
 * @author Kadin Benjamin ktb1193
 */
public class WAMGUI extends Application implements Observer<WAMBoard>{

    /**the WAMNetworkPlayer is the controller that notifies the GUI
     * application of the server's requests and the server of the GUI
     * application's response, if required.*/
    private WAMNetworkPlayer controller;

    /***/
    private WAMBoard board;

    /***/
    private Scene scene;
    /**
     * Creates the client socket and connects to the server
     * @param args command-line arguments
     */
    public static void main(String[] args) { launch(args); }

    /**
     * processes command-line arguments
     */
    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[2]);
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
                int rows = Integer.parseInt(args[2]);
                int columns = Integer.parseInt(args[3]);
                controller = new WAMNetworkPlayer(args[0], port);
                this.board = new WAMBoard(rows, columns);
                board.addObserver(new WAMGUI());
                start = true;
            } catch (NumberFormatException nfe) {
                System.out.println("illicit arguments...");
                args = new String[0];
            } catch (IOException ioe) {
                System.out.println("failed connecting...");
                args = new String[0];
            }
        }
        controller.startListening();
    }

    /**
     * creates the board for whack-a-mole
     * Grid pane with buttons and a text area for displaying updates
     * @param primaryStage The stage
     */
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        int rows = board.getRows();
        int columns = board.getColumns();
        for (int i = 0; i < columns; i++){
            for (int j = 0; j < rows; j++){
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
    /**
     * updates GUI
     */
    private void refresh() {

        for (int i = 0; i < board.getRows(); i++){
            for (int j = 0; j < board.getColumns(); j++) {
                if( board.getMoleStatus(i, j) == 1){


                }
            }
        }
    }

    /**
     * Called by the model, WAMBoard whenever there is a state change
     * that needs to be updated by the GUI.
     *
     * @param WAMboard
     */
    @Override
    public void update(WAMBoard WAMboard) {
        if ( Platform.isFxApplicationThread() ) {
            this.refresh();
        }
        else {
            Platform.runLater( () -> this.refresh() );
        }
    }
    /***/
    @Override
    public void stop() {  }
}