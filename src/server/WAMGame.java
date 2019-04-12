package server;

import java.io.IOException;

import java.lang.Runnable;
import java.lang.Thread;

import java.net.Socket;

import static common.WAMProtocol.*;

/***/
public class WAMGame implements Runnable {

    /***/
    private WAMNetworkClient[] clients;

    /***/
    //the game
    //unless this becomes the game

    /***/
    public WAMGame(WAMNetworkClient[] clients, int rows, int columns, int t) {
        this.clients = clients;
    }

    /***/
    @Override
    public void run() {  }

}