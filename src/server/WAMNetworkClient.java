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
     *
     * @param id
     */
    public void moleUp(int id){
        output.println(MOLE_UP + " " + id);
    }
    public boolean moleWhacked(int id){
        String response = input.nextLine();

        if(response.startsWith(WHACK)){
            String[] whacked = response.split(" ");
            if(game.isMoleUp(Integer.parseInt(whacked[1]))){
                return true;
            }


        }
        return false;
    }
    /**
     *
     * @param id
     */
    public void moleDown(int id){
        output.println(MOLE_DOWN + " " + id);
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