package client.gui;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

import java.lang.IllegalArgumentException;
import java.lang.Thread;

import java.net.Socket;

import java.util.Scanner;

import static common.WAMProtocol.*;

/**WAMNetworkPlayer communicates a player's GUI application with a
 * WAM server, and so it represents the controller in the
 * model-view-controller architecture.
 * WAMNetworkPlayer -> ServerSocket -> WAMNetworkClient
 * @author Kadin Benjamin ktb1193
 * @author Sungmin Kim sk4900*/

public class WAMNetworkPlayer implements Closeable {

    /**a Socket that maintains the connection between the player
     * and a server.*/
    private Socket player;

    /**a Scanner that reads from the player's InputStream.*/
    private Scanner input;

    /**a PrintStream that prints to the player's OutputStream.*/
    private PrintStream output;

    /**an integer that represents this player's numerical designation
     * on a WAM server and its order of connection, relative to the other
     * players that are connected to the server.*/
    private int playerNumber;

    /**a String that represents the outcome of this player:
     * GAME_WON, GAME_LOST, GAME_TIED, ERROR, if there is one, or
     * null if neither.*/
    private String playerOutcome;

    /**an integer array of which each index represents a player by its
     * playerNumber and points to that player's score.*/
    private int[] playerScores;

    /**a WAMBoard that represents the state of the WAM game.*/
    private WAMBoard board;

    /**the truth value of this player's readiness*/
    private boolean started = false;

    /**..creates a player.
     * @param host is a String that represents a server's hostname
     * or IP address.
     * @param port is an integer that matches the port number a
     * server is listening for connections on.
     * @throws IOException if an error occurse while connecting to
     * a server or while opening a player's InputStream or OutputStream.
     * @throws IllegalArgumentException if the specified port number
     * transgresses the range for legal port numbers.*/
    public WAMNetworkPlayer(String host, int port, WAMBoard board)
        throws IOException, IllegalArgumentException {
        player = new Socket(host, port);
        input = new Scanner(player.getInputStream());
        output = new PrintStream(player.getOutputStream());
        playerOutcome = null;
        this.board = board;
    }

    /**startListening starts an internal Thread of this object's
     * run method, which listens to requests from a server.*/
    public void startListening() { new Thread(this::run).start(); }

    /**getPlayerNumer
     * @return an integer that represents the order of this player's
     * connection, relative to the other player's connected to the same
     * WAM server.*/
    public int getPlayerNumber() { return playerNumber; }

    /**getPlayerOutcome
     * @return a String that represents the outcome of this player
     * after the completion of WAM or the reception of an ERROR message
     * from the WAM server.*/
    public String getPlayerOutcome() { return playerOutcome; }

    /**getScores
     * @return the score of each player connected to the WAM server that
     * this player is connected to.*/
    public int[] getScores() { return playerScores; }

    /**isPlayerReady
     * @return the truth value of this player's readiness.*/
    public boolean isPlayerReady() { return started; }

    /**sendMessage sends a String object to the WAM server that this player
     * is connected to.
     * @throws IOException if sending or receiving the message fails.*/
    public void sendMessage(String message) throws IOException { output.print(message); }

    /**close shutdowns this player's InputStream and OutputStream and
     * terminates its presence on a network.*/
    @Override
    public void close() {
        try { player.close(); }
        catch (IOException ioe)
        { System.out.println("unsuccessful disconnect"); }
    }

    /**run responds to messages from the WAM server.*/
    private void run() {
        while (input.hasNextLine()) {
            String[] request = input.nextLine().split(" ");
            if (request[0].equals(ERROR)) {
                for (String s : request)
                { playerOutcome = playerOutcome.concat(s); }
                close();
            } else {
                int[] detail = new int[request.length - 1];
                for (int i = 1; i < request.length; i++)
                { detail[i - 1] = Integer.parseInt(request[i]); }
                switch (request[0]) {
                    case WELCOME:
                        board.setBoardSize(detail[1],detail[0]);
                        playerNumber = (detail[3] % detail[2]) + 1;
                        started = true;
                        break;
                    case MOLE_UP:
                        board.setMoleUp(detail[0]);
                        break;
                    case MOLE_DOWN:
                        board.setMoleDown(detail[0]);
                        break;
                    case SCORE:
                        playerScores = detail.clone();
                        break;
                    case GAME_WON:
                        playerOutcome = "VICTORY";
                        close();
                        break;
                    case GAME_LOST:
                        playerOutcome = "DEFEAT";
                        close();
                        break;
                    case GAME_TIED:
                        playerOutcome = "DRAW";
                        close();
                        break;
                }
            }
        }
    }
}