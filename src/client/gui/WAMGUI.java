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
import java.util.Scanner;

import static common.WAMProtocol.WHACK;

/** responsible for creating the board
 * @author Sungmin Kim sk4900
 * @author Kadin Benjamin ktb1193*/
public class WAMGUI extends Application implements Observer<WAMBoard>{

    /**the WAMNetworkPlayer is the controller that notifies the GUI
     * application of the server's requests and the server of the GUI
     * application's response, if required.*/
    private WAMNetworkPlayer controller;

    /**a two-dimenstional array of Buttons, where each button represents
     * a mole.*/
    private Button[][] moles;

    /**a WAM board.*/
    private WAMBoard board;

    /**a BorderPane that represents the arrangement of this application's
     * GUI components.*/
    private BorderPane window;

    private TextArea text;

    /**Creates the client socket and connects to the server
     * @param args command-line arguments*/
    public static void main(String[] args) { launch(args); }

    /**init processes command-line arguments and initialize GUI
     * components.*/
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
                board = new WAMBoard();
                controller = new WAMNetworkPlayer(args[0], port, board);
                controller.startListening();
                start = true;
            } catch (NumberFormatException nfe) {
                System.out.println("illicit arguments...");
                args = new String[0];
            } catch (IOException ioe) {
                System.out.println("failed connecting...");
                args = new String[0];
            }
        } while (!controller.isPlayerReady()) {  }
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        moles = new Button[board.getColumns()][board.getRows()];
        for (int y = 0; y < board.getRows(); y++) {
            for (int x = 0; x < board.getColumns(); x++) {
                int id = x + (y * board.getColumns());
                Button button = createButton(id);
                gridPane.add(button, x, board.getRows() - y - 1);
                moles[x][y] = button;
            }
        }
        window = new BorderPane();
        TextArea text = new TextArea();
        this.text = text;
        text.setEditable(false);
        text.setText("Welcome to Whack-A-Mole");
        text.setMaxWidth(gridPane.getColumnCount() * 100);
        text.setMaxHeight(100);
        window.setBottom(text);
        window.setCenter(gridPane);
        board.addObserver(this);
    }

    /**start creates the board for whack-a-mole
     * Grid pane with buttons and a text area for displaying updates
     * @param primaryStage The stage*/
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(window));
        primaryStage.setTitle("Whack-A-Mole");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**createButton creates a button that represents a mole.
     * @param id an integer that represents a mole's position on a WAM board.
     * @return a Button that represents a mole.*/
    private Button createButton(int id) {
        Button b = new Button();
        b.setMaxHeight(100);
        b.setMaxWidth(100);
        b.setMinHeight(100);
        b.setMinWidth(100);
        b.setOnAction(event -> {
            try { controller.sendMessage(
                    WHACK + " " +
                    id + " " +
                    controller.getPlayerNumber());
                this.text.appendText("hi");
            } catch (IOException ioe) {  }
        });
        return b;
    }

    /**refresh updates the GUI*/
    private void refresh() {
        for (int y = 0; y < board.getRows(); y++){
            for (int x = 0; x < board.getColumns(); x++) {
                System.out.println(board.toString());
                if (board.getMoleStatus(x, y) == 1){
                    moles[x][y].setStyle("-fx-background-color: #654321");
                }
                else if (board.getMoleStatus(x, y) == 0){
                    moles[x][y].setStyle("-fx-background-color: #000000");

                }
            }
        }
    }

    /**update is called by the model, WAMBoard, whenever there is a state change
     * that needs to be updated by the GUI.
     *@param WAMboard*/
    @Override
    public void update(WAMBoard WAMboard) {
        if ( Platform.isFxApplicationThread() ) {
            this.refresh();
        }
        else {
            Platform.runLater( () -> this.refresh() );
        }
    }

    /**stop closes the process after the GUI is notified to shutdown.*/
    @Override
    public void stop() { controller.close(); }
}