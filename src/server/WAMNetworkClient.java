package server;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

import java.net.Socket;

import java.util.Scanner;

/**WAMNetworkClient represents each client connected to a WAM server.
 * @author Kadin Benjamin ktb1193*/
public class WAMNetworkClient implements Closeable {

    /**a Socket that maintains the connection between the server
     * and a client.*/
    private Socket client;

    /**a Scanner that will read from the client's InputStream.*/
    private Scanner input;

    /**a PrintStream that will write to the client's OutputStream.*/
    private PrintStream output;

    /**...creates a client.
     * @throws IOException if an error occurs while accessing a
     * client's InputStream and OutputStream.*/
    public WAMNetworkClient(Socket client) throws IOException {
        this.client = client;
        input = new Scanner(client.getInputStream());
        output = new PrintStream(client.getOutputStream());
    }

    /**close shutdowns a client's InputStream and OutputStream and
     * terminates its presence on a network.*/
    @Override
    public void close() {
        try { client.close(); }
        catch (IOException ioe) {
            System.out.println("client termination failure");
            ioe.printStackTrace();
        }
    }
}