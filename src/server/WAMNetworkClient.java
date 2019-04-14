package server;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

import java.lang.Runnable;

import java.net.Socket;

import java.util.Scanner;

/**WAMNetworkClient represents each client connected to a WAM server.
 * WAMNetworkClient -> ServerSocket -> WAMNetworkPlayer
 * @author Kadin Benjamin ktb1193*/
public class WAMNetworkClient implements Closeable, Runnable {

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

    /**...creates a client.
     * @param
     * @param
     * @throws IOException if an error occurs while accessing a
     * client's InputStream and OutputStream.*/
    public WAMNetworkClient(Socket client, int connectionOrder)
        throws IOException {
        this.client = client;
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

    /**
     * @param
     * @throws */
    public void sendMessage(String protocol) throws IOException {
        output.print(protocol);
    }

    /**
     * @return
     * @throws */
    public String getMessage() throws IOException {
        if (input.hasNextLine()) { return input.nextLine(); }
        return null;
    }

    /***/
    @Override
    public void run() {  }

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