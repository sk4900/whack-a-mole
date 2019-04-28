package client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

import static common.WAMProtocol.WHACK;

/**WAMGUI is responsible for creating the board as GUI application.
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

    /**a Scene that stores the arrangement of this application's
     * GUI components.*/
    private Scene window;

    /**a TextArea that displays information of this GUI application's
     * WAMNetworkPlayer. If the WAM game is single player, then this
     * is the only existing TextArea.*/
    private TextArea text;

    /**a TextArea that displays the scores of other players, if the
     * WAM game is multiplayer.*/
    private TextArea rightText;

    /**the background of each button before a WAM game is started.*/
    private final Image background = new Image(
            getClass().getResourceAsStream("background.png"),
            90, 90, true, true);

    /**the background of a button if the mole it represents is up.*/
    private final Image moleUp = new Image(
            getClass().getResourceAsStream("mole_up.png"),
            90, 90, true, true);

    /**the background of a button if the mole it represents is down.*/
    private final Image moleDown = new Image(
            getClass().getResourceAsStream("mole_down.png"),
            90, 90, true, true);

    /**Creates the client socket and connects to the server
     * @param args command-line arguments*/
    public static void main(String[] args) { launch(args); }

    /**init processes command-line arguments and initialize GUI
     * components.*/
    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[2]);
        processArgs(args);
        GridPane gridPane = buildBoard();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(new HBox(createTextFields(
            controller.getScores().length,
            gridPane.getColumnCount())
        ));
        window = new Scene(borderPane);
    }

    /**start creates the board for whack-a-mole
     * Grid pane with buttons and a leftText area for displaying updates
     * @param primaryStage The stage*/
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(window);
        primaryStage.setTitle("Whack-A-Mole");
        primaryStage.setResizable(false);
        board.addObserver(this);
        primaryStage.show();
    }

    /**processArgs interprets this player's command-line arguments.
     * @param args String[] of command-line arguments.*/
    public void processArgs(String[] args) {
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
        }{

        }
    }

    /**buildBoard creates a gridded GUI component for representing each
     * mole of this WAM game.
     * @return a GridPane of Buttons, where each button represents a mole.*/
    private GridPane buildBoard() {
        GridPane gridPane = new GridPane();
        moles = new Button[board.getColumns()][board.getRows()];
        for (int y = 0; y < board.getRows(); y++) {
            for (int x = 0; x < board.getColumns(); x++) {
                int id = x + (y * board.getColumns());
                Button button = createButton(id);
                gridPane.add(button, x, board.getRows() - y - 1);
                moles[x][y] = button;
            }
        }
        return gridPane;
    }

    /**createButton creates a button that represents a mole.
     * @param id an integer that represents a mole's position on a WAM board.
     * @return a Button that represents a mole.*/
    private Button createButton(int id) {
        ImageView image = new ImageView(background);
        Button b = new Button("", image);
        b.setMaxHeight(100);
        b.setMaxWidth(100);
        b.setMinHeight(100);
        b.setMinWidth(100);
        b.setOnAction(event -> {
            try {
                controller.sendMessage(
                    WHACK + " " +
                    id + " " +
                    controller.getPlayerNumber());
            } catch (IOException ioe) {  }
        });
        return b;
    }

    /**createTextFields is responsible for formating the TextAreas of this
     * WAM game.
     * @param players is an integer that counts this game's players.
     * @param columns is an integer that count the columns of this game's
     * WAMBoard.
     * @return a TextArea[] with one TextArea, if there is a single player,
     * or two TextAreas, if the game is multiplayer. The first TextArea
     * represents this player's status in the WAM game, and the second TextArea
     * represents the status of this game's other players.*/
    private TextArea[] createTextFields(int players, int columns) {
        text = new TextArea();
        if (players > 1) {
            text.setMaxWidth(columns * 50);
            text.setMaxHeight(100);
            text.setEditable(false);
            rightText = new TextArea();
            rightText.setMaxWidth(columns * 50);
            rightText.setMaxHeight(100);
            rightText.setEditable(false);
            return new TextArea[]{ text, rightText };
        }
        text.setMaxWidth(columns * 100);
        text.setMaxHeight(100);
        text.setEditable(false);
        rightText = null;
        return new TextArea[]{ text };
    }

    /**refresh updates the GUI*/
    private void refresh() {
        for (int y = 0; y < board.getRows(); y++){
            for (int x = 0; x < board.getColumns(); x++) {
                if (board.getMoleStatus(x, y) == 1){
                    moles[x][y].setGraphic(new ImageView(moleUp));
                }
                else if (board.getMoleStatus(x, y) == 0){
                    moles[x][y].setGraphic(new ImageView(moleDown));
                }
            }
        }
    }

    /**refreshText is responsible for updating the information displayed
     * by this game's TextArea(s).*/
    private void refreshText() {
        int[] scores = controller.getScores();
        int player = controller.getPlayerNumber();
        String outcome = controller.getPlayerOutcome();
        for (int i = 0; i < scores.length; i++) {
            if (i == player - 1) {
                text.setText(
                    "PLAYER " + player + "\n" +
                    "SCORE : " + scores[i] + "\n" +
                    ((outcome == null)? "" : outcome)
                );
            } else {
                rightText.appendText(
                    "PLAYER " + (i + 1) +
                    " : " + scores[i] + "\n"
                );
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