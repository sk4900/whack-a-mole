package server;

import static common.WAMProtocol.*;

import java.io.IOException;

import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;
import java.lang.Runnable;
import java.lang.Thread;

import java.net.ServerSocket;

import java.util.Scanner;

/**WAMServer is responsible for constructing and maintaining a Wack-A-Mole-server
 * @author Kadin Benjamin ktb1193*/
public class WAMServer implements Runnable {

    /**a ServerSocket that constructs a point of potential connection on a
     * specifiable port.*/
    private ServerSocket serverSocket;

    /**a two-dimensional-integer array that represents a Wack-A-Mole-board.*/
    private int[][] board;  //may change

    /**an integer that represents the requisite number of connected clients
     * to start a game.*/
    private int clients;    //may change to something like a clienthandler

    /**an integer that represents the length of a game in seconds.*/
    private int time;       //may change to another type

    /**...creates a server.
     * @throws IOException if there is an error while opening this socket.
     * @throws IllegalArgumentException if an illicit argument is provided.*/
    public WAMServer(int port, int rows, int columns, int clients, int time)
        throws IOException, IllegalArgumentException {
        serverSocket = new ServerSocket(port);
        board = new int[columns][rows];
        this.clients = clients;
        this.time = time;
    }

    /**run is responsible for maintaining the Wack-A-Mole-server and providing
     * an environment that connected clients may interact with.*/
    public void run() {  }

    /**main is responsible for starting a Wack-A-Mole-server and detailing
     * any issues in that process.*/
    public static void main(String[] args) {
        while (args.length < 5) { //check arguments
            System.out.println("Usage: #game-port " +
                "#rows #columns #players #game-time");
            Scanner in = new Scanner(System.in);
            args = in.nextLine().split(" ");
            try { int[] params = new int[args.length];
                for (int i = 0; i < args.length ; i++) {
                    params[i] = Integer.parseInt(args[i]);
                }
            } catch (NumberFormatException nfe) {  }
        }
        WAMServer server; //try making the server
        try { server = new WAMServer(params[0], params[1],
                params[2], params[3], params[4]);
            new Thread(server).run();
        } catch (IOException ioe) {
            System.out.println("server failure:\n" + ioe);
        } catch (IllegalArgumentException iae) {
            System.out.println("server failure:\n" + iae);
        }
    }
}