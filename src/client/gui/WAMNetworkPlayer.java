package client.gui;

import java.io.IOException;
import java.io.PrintStream;

import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;
import java.lang.Thread;

import java.net.Socket;

import java.util.Scanner;

import common.WAMBoard;
import static common.WAMProtocol.*;

/**WAMNetworkPlayer communicates a player's GUI application with a
 * WAM server, and so it represents the controller in the
 * model-view-controller architecture.
 * WAMNetworkPlayer -> ServerSocket -> WAMNetworkClient
 * @author Kadin Benjamin ktb1193*/
public class WAMNetworkPlayer {

    /**a Socket that maintains the connection between the player
     * and a server.*/
    private Socket player;

    /**a Scanner that reads from the player's InputStream.*/
    private Scanner input;

    /**a PrintStream that prints to the player's OutputStream.*/
    private PrintStream output;

    /**an integer that represents this player's numerical designation
     * on a WAM server.*/
    private int playerNumber;

    /**WAMBoard represents the state of the WAM game.*/
    private WAMBoard board;

    /**..creates a player.
     * @param host is a String that represents a server's hostname
     * or IP address.
     * @param port is an integer that matches the port number a
     * server is listening for connections on.
     * @throws IOException if an error occurse while connecting to
     * a server or while opening a player's InputStream or OutputStream.
     * @throws IllegalArgumentException if the specified port number
     * transgresses the range for legal port numbers.*/
    public WAMNetworkPlayer(String host, int port)
        throws IOException, IllegalArgumentException {
        player = new Socket(host, port);
        input = new Scanner(player.getInputStream());
        output = new PrintStream(player.getOutputStream());
    }

    /**startListening starts an internal Thread of this object's
     * run method, which listens to requests from a server.*/
    public void startListening() { new Thread(this::run).start(); }

    /**getColumns
     * @return an integer count of the game-board's columns.*/
    public int getColumns() { return board.getColumns(); }

    /**getRows
     * @return an integer count of the game-board's rows.*/
    public int getRows() { return board.getRows(); }

    /**sendMessage sends a String object to the WAM server that this player
     * is connected to.
     * @throws IOException if sending or receiving the message fails.*/
    public void sendMessage(String message) throws IOException { output.print(message); }

    /**run responds to requests from the WAM server.*/
    private void run() {
        while (input.hasNextLine()) {
            String[] request = input.nextLine().split(" ");
            int[] detail = new int[request.length - 1];
            for (int i = 1; i < request.length; i++) {
                detail[i - 1] = Integer.parseInt(request[i]);
            }
            switch (request[0]) {
                case WELCOME:
                    board = new WAMBoard(detail[1], detail[0]);
                    playerNumber = (detail[3] % detail[2]) + 1;
                    break;
                case MOLE_UP:
                    board.setMoleUp(detail[0]);
                    break;
                case MOLE_DOWN:
                    board.setMoleDown(detail[0]);
                    break;
                case SCORE:
                    break;
                case GAME_WON:
                    break;
                case GAME_LOST:
                    break;
                case GAME_TIED:
                    break;
                case ERROR:
                    break;
            }
        }
    }
}