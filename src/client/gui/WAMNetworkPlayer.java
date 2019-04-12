package client.gui;

import java.io.IOException;
import java.io.PrintStream;

import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;
import java.lang.Thread;

import java.net.Socket;

import java.util.Scanner;

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

    /**an integer that represents the count of the game-board's rows.*/
    private int rows;

    /**an integer that represents the count of the game-board's columns.*/
    private int columns;

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

    /**getRows
     * @return an integer count of the game-board's rows.*/
    public int getRows() { return rows; }

    /**getColumns
     * @return an integer count of the game-board's columns.*/
    public int getColumns() { return columns; }

    /**startListening starts an internal Thread of this object's
     * run method, which listens to requests from a server.*/
    public void startListening() { new Thread(this::run).start(); }

    /***/
    private void run() {  }
}