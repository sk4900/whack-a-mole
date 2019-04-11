package server;

import java.lang.Runnable;

import java.net.Socket;

/***/
public class WAMGame implements Runnable {

    /***/
    private WAMNetworkClient[] clients;

    /***/
    //the game
    //unless this becomes the game

    /***/
    public WAMGame(WAMNetworkClient[] clients, int x, int y, int t) {
        this.clients = clients;
    }

    /***/
    @Override
    public void run() {  }

}