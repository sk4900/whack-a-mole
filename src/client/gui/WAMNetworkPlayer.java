package client.gui;

import java.io.IOException;
import java.io.PrintStream;

import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;

import java.net.Socket;

import java.util.Scanner;

/**WAMNetworkPlayer communicates a player's GUI application with a
 * WAM server, and so it represents the controller in the
 * model-view-controller architecture.
 * @author Kadin Benjamin ktb1193*/
public class WAMNetworkPlayer {

    /**a Socket that maintains the connection between the player
     * and a server.*/
    private Socket player;

    /**a Scanner that reads from the player's InputStream.*/
    private Scanner input;

    /**a PrintStream that prints to the player's OutputStream.*/
    private PrintStream output;

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
        throws IOException, IllecgalArgumentException {
        player = new Socket(host, port);
        input = new Scanner(player.getInputStream());
        output = new PrintStream(player.getOutputStream());
    }

    /***/
    /*FOR WAMGUI CLASS
    public static void main(String[] args) {
        boolean start = false;
        while (!start) {
            while (args.length < 2) {
                System.out.println("usage-> hostname #port" +
                    "\nenter arguments: ");
                Scanner in = new Scanner(System.in);
                args = in.nextLine().split(" ");
            }
            try {
                int port = Integer.parseInt(args[1]);
                Socket socket = new Socket(args[0], port);
                //give socket to player
                start = true;
            }
            catch (NumberFormatException nfe) {
                System.out.println("");
                args = new String[0];
            }
            catch (IOException ioe) {
                System.out.println("");
                args = new String[0];
            }
        }
    }
    */
}