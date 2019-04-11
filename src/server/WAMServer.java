package server;

import static common.WAMProtocol.*;

import java.io.IOException;

import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;
import java.lang.Runnable;
import java.lang.Thread;

import java.net.ServerSocket;

import java.util.Scanner;

/**WAMServer is responsible for constructing and maintaining a Wack-A-Mole server
 * @author Kadin Benjamin ktb1193*/
public class WAMServer implements Runnable {

    /**a ServerSocket that constructs a point of potential connection on a
     * specifiable port.*/
    private ServerSocket serverSocket;

    /**an integer that represents the width of a Wack-A-Mole board.*/
    private int gameWidth;

    /**an integer that represents the height of a Wack-A-Mole board.*/
    private int gameHeight;

    /**an integer that represents the requisite number of connected clients
     * to start a game.*/
    private int connections;

    /**an integer that represents the length of a game in seconds.*/
    private int time;

    /**...creates a WAM server.
     * @throws IOException if there is an error while opening this socket.
     * @throws IllegalArgumentException if an illicit argument is provided.*/
    public WAMServer(int port, int rows, int columns, int connections, int time)
        throws IOException, IllegalArgumentException {
        serverSocket = new ServerSocket(port);
        gameWidth = columns;
        gameHeight = rows;
        this.connections = connections;
        this.time = time;
    }

    /**run is responsible for maintaining the Wack-A-Mole server and providing
     * an environment that connected clients may interact with.*/
    @Override
    public void run() {
        WAMNetworkClient[] clients = new WAMNetworkClient[connections];
        try { for (int i = 0; i < clients.length; i++) { //establish connections
                clients[i] = new WAMNetworkClient(serverSocket.accept());
            }
        } catch (IOException ioe) {
            System.out.println("client connection failure:\n");
            ioe.printStackTrace();
        }
        WAMGame game = new WAMGame(clients, gameWidth, gameHeight, time);
        new Thread(game).run(); //start the game
    }

    /**main is responsible for starting a Wack-A-Mole server and detailing
     * any issues in that process.*/
    public static void main(String[] args) {
        int[] params = new int[5];
        boolean start = false;
        while (!start) { //check arguments
            while (args.length < 5) {
                System.out.println("usage-> #port #rows " +
                    "#columns #players #game-time" +
                    "\nenter arguments: ");
                Scanner in = new Scanner(System.in);
                args = in.nextLine().split(" ");
            }
            try { for (int i = 0; i < args.length ; i++) {
                    params[i] = Integer.parseInt(args[i]);
                }
                start = true;
            } catch (NumberFormatException nfe) {
                System.out.println("illicit arguments:\n");
                args = new String[0];
            }
        }
        WAMServer server; //try making the server
        try { server = new WAMServer(params[0], params[1],
                params[2], params[3], params[4]);
            server.run(); //start server
        } catch (IOException ioe) {
            System.out.println("server failure:\n");
            ioe.printStackTrace();
        } catch (IllegalArgumentException iae) {
            System.out.println("server failure:\n");
            iae.printStackTrace();
        }
    }
}