package client.gui;

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
 * @author Sungmin Kim sk4900
 * @author Kadin Benjamin ktb1193
 */
public class WAMGUI extends Application implements Observer<WAMBoard>{

    /**the WAMNetworkPlayer is the controller that notifies the GUI
     * application of the server's requests and the server of the GUI
     * application's response, if required.*/
    private WAMNetworkPlayer controller;
    /***/
    private Button[][] moles;

    private WAMBoard board = new WAMBoard();

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
                board.setRows(3);
                board.setColumns(4);
                controller = new WAMNetworkPlayer(args[0], port, this.board);
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
        int columns = controller.getColumns();
        int rows = controller.getRows();
        this.moles = new Button[columns][rows];
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Button button = new Button();
                button.setMaxHeight(100);
                button.setMaxWidth(100);
                button.setMinHeight(100);
                button.setMinWidth(100);
                this.moles[x][y] = button;
                grid.add(button, x, y);
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
        board.addObserver(this);
    }
    /**
     * updates GUI
     */
    private void refresh() {
        for (int x = 0; x < controller.getColumns(); x++){
            for (int y = 0; y < controller.getRows(); y++) {
                System.out.println(board.toString());
                if( controller.getMoleStatus(x, y) == 1){
                    moles[x][y].setStyle("-fx-background-color: #654321");
                }
                else if(controller.getMoleStatus(x, y) == 0){
                    moles[x][y].setStyle("-fx-background-color: #000000");

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