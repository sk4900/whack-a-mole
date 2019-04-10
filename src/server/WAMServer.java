package server;
import static common.WAMProtocol.*;

import java.io.IOException;

import java.lang.IllegalArgumentExcpetion;
import java.lang.Runnable;
import java.lang.Thread;

import java.net.ServerSocket;

import java.util.Scanner;

/***/
public class WAMServer implements Runnable {

    /***/
    private ServerSocket serverSocket;

    /***/
    private int[][] board;

    /***/
    private int clients;

    /***/
    private int time;

    /***/
    public WAMServer(int port, int rows, int columns, int clients, int time)
        throws IOExcpetion, IllegalArgumentException {
        serverSocket = new ServerSocket(port);
        board = new int[columns][rows];
        this.clients = clients;
        this.time = time;
    }

    /***/
    public void run() {  }

    /***/
    public static void main(String[] args) {
        while (args.length < 5) {
            System.out.println("Usage: #game-port " +
                "#rows #columns #players #game-time");
            Scanner in = new Scanner(System.in);
            args = in.nextLine().split(" ");
        }
        int[] params = new int[args.length];
        for (int i = 0; i < args.length ; i++) {
            params[i] = Integer.parseInt(args[i]);
        }
        WAMServer server;
        try {
            server = new WAMServer(params[0], params[1],
                params[2], params[3], params[4]);
            new Thread(server).run();
        } catch (IOException ioe) {
            System.out.println("error starting server");
            //send message to clients
        } catch (IllegalArgumentException iae) {
            System.out.println("error starting server");
            //send message to clients
        }
    }
}