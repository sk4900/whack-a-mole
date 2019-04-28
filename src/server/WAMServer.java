package server;

import java.io.Closeable;
import java.io.IOException;

import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;
import java.lang.Runnable;
import java.lang.Thread;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;

/**WAMServer is responsible for constructing and maintaining a Wack-A-Mole server.
 * If the server is prompted to start with less than one clients, then the server will
 * automatically make room for a maximum of one client.
 * @author Kadin Benjamin ktb1193
 * @author Sungmin Kim sk4900*/
public class WAMServer implements Closeable, Runnable {

    /**a ServerSocket that constructs a point of potential connection on a
     * specifiable port.*/
    private ServerSocket serverSocket;

    /**a WAMGame that represents the WAM game that is playable on this server.*/
    private WAMGame game;

    /**a list of the clients of this server.*/
    private WAMNetworkClient[] clients;

    /**...creates a WAM server.
     * @param port is an integer that represents the port through which this
     * server listens for connections.
     * @param columns is an integer that represents the width of a WAM board.
     * @param rows is an integer that represents the height of a WAM board.
     * @param connections is an integer that represents the requisite amount
     * of client connections to start the WAM game.
     * @param time is an integer that represents the game's length in seconds.
     * @throws IOException if there is an error while opening this socket.
     * @throws IllegalArgumentException if an illicit argument is provided.*/
    public WAMServer(int port, int columns, int rows, int connections, int time)
        throws IOException, IllegalArgumentException {
        serverSocket = new ServerSocket(port);
        clients = new WAMNetworkClient[connections];
        game = new WAMGame(columns, rows, time, clients);
        for (int i = 0; i < clients.length; i++) {
            System.out.println("listening for client " + i);
            Socket client = serverSocket.accept();
            clients[i] = new WAMNetworkClient(client, i, game);
        }
        game.setClients(clients);
        System.out.println("all connections satisfied");
    }

    /**run is responsible for maintaining the Whack-A-Mole server and providing
     * an environment that connected clients may interact with.*/
    @Override
    public void run() {
        for (WAMNetworkClient client : clients) {
            client.start();
            client.welcome(game.getRows(), game.getColumns(),
                clients.length, client.getConnectionOrder());
        }
        game.run();
        this.close();
    }

    /**close shutdowns this server after doing the same for its clients.*/
    @Override
    public void close() {
        try {
            for (WAMNetworkClient client : clients) {
                client.close();
            }
            serverSocket.close();
        }
        catch (IOException ioe) {  }
    }

    /**main is responsible for starting a Wack-A-Mole server and detailing
     * any issues in that process.
     * @param args is a String[] of command line arguments.*/
    public static void main(String[] args) {
        int[] params = WAMServer.formatArgs(args);
        WAMServer server;
        try { server = new WAMServer(params[0], params[2],
                params[1], params[3], params[4]);
            server.run();
        } catch (IOException ioe) {
            System.out.println("server failure...\n");
            ioe.printStackTrace();
        } catch (IllegalArgumentException iae) {
            System.out.println("server failure...\n");
            iae.printStackTrace();
        }
    }

    /**formatArgs is a helper method of WAMServer's main method that
     * formats the initial arguments given to the program.
     * @return int[] that represents the initial arguments.*/
    private static int[] formatArgs(String [] args) {
        int[] params = new int[5];
        boolean formatted = false;
        while (!formatted) {
            while (args.length < 5) {
                System.out.println("usage-> #port #rows " +
                        "#columns #players #game-time" +
                        "\nenter arguments: ");
                Scanner in = new Scanner(System.in);
                args = in.nextLine().split(" ");
            }
            try { for (int i = 0; i < args.length ; i++) {
                    params[i] = Integer.parseInt(args[i]);
                    if (i == 3 && params[i] < 1) { params[i] = 1; }
                }
                formatted = true;
            } catch (NumberFormatException nfe) {
                System.out.println("illicit arguments...\n");
                args = new String[0];
            }
        }
        return params;
    }
}