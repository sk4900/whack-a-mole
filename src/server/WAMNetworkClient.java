package server;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Runnable;

import java.net.Socket;

import java.util.Scanner;

import static common.WAMProtocol.*;

/**WAMNetworkClient represents each client connected to a WAM server.
 * WAMNetworkClient -> ServerSocket -> WAMNetworkPlayer
 * @author Kadin Benjamin ktb1193
 * @author Sungmin Kim sk4900*/

public class WAMNetworkClient extends Thread implements Closeable{

    /**a Socket that maintains the connection between the server
     * and a client.*/
    private Socket client;

    /**a Scanner that will read from the client's InputStream.*/
    private Scanner input;

    /**a PrintStream that will write to the client's OutputStream.*/
    private PrintStream output;

    /**an integer that represents the temporal order of this connection
     * relative to the others.*/
    private int connectionOrder;

    /**an integer that represents the net number of moles this
     * client has whacked.*/
    private int score;

    /** instance of the game*/
    private WAMGame game;

    /**...creates a client.
     * @param
     * @param
     * @throws IOException if an error occurs while accessing a
     * client's InputStream and OutputStream.*/
    public WAMNetworkClient(Socket client, int connectionOrder, WAMGame game)
        throws IOException {
        this.client = client;
        this.game = game;
        input = new Scanner(client.getInputStream());
        output = new PrintStream(client.getOutputStream());
        this.connectionOrder = connectionOrder;
        score = 0;
    }

    /**getScore
     * @return an integer representation of this client's score.*/
    public int getScore() { return score; }

    /**addPoints adds x points to this client's score.
     * @param x is a negative or positive integer.*/
    public void addScore(int x) { score += x; }

    /**getConnectionOrder
     * @return the temporal-integer order of this client's connection
     * to a WAM server.*/
    public int getConnectionOrder() { return connectionOrder; }

    /** sends messasge to client to start game */
    public void welcome(int rows, int columns, int players, int playerNumber){
        output.println(WELCOME + " " + rows + " " + columns + " " + players + " " + playerNumber);
    }
    /** called to send a request to server because this player has lost */
    public void gameLost (){
        output.println(GAME_LOST);
    }
    /** called to send a request to server because this player has scored the most points */
    public void gameWon (){
        output.println(GAME_WON);
    }

    /** called to send a request to server because this player tied with another player */
    public void gameTied(){
        output.println(GAME_TIED);
    }
    /**
     * sends message to client to set mole up
     * @param id of mole
     */
    public void moleUp(int id){
        output.println(MOLE_UP + " " + id);
    }

    /**
     * sends scores to each client
     * @param scores array of all scores ordered by player number
     */
    public void sendScore(int ...scores){
        output.println(SCORE + " " + scores);

    }
    /**
     * helper method to parse whack message
     */
    public void moleWhacked(String message){
            String[] whacked = message.split(" ");
            game.moleWhack(Integer.parseInt(whacked[1]), Integer.parseInt(whacked[2]));
    }
    /**
     * sends message to client to set mole down
     * @param id of mole to be set down
     */
    public void moleDown(int id){
        output.println(MOLE_DOWN + " " + id);
        System.out.println("DOWN" + id);
    }

    /***/
    @Override
    public void run(){
        System.out.println("running");
        while(!game.isGameInProgress()){}
        while(input.hasNextLine()){

            System.out.println("hello");
            if(input.nextLine().startsWith(WHACK)){
                String response = input.nextLine();
                moleWhacked(response);


            }
        }
    }

    /**close shutdowns a client's InputStream and OutputStream and
     * terminates its presence on a network.*/
    @Override
    public void close() {
        try { client.close(); }
        catch (IOException ioe) {
            System.out.println("client termination failure...\n");
            ioe.printStackTrace();
        }
    }
}