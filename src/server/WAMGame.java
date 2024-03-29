package server;

import java.io.IOException;

import java.lang.Thread;

import java.net.Socket;

import static common.WAMProtocol.*;
import static java.lang.Thread.State.TIMED_WAITING;

/**WAMGame represents a game of Whack-A-Mole.
 * @author Kadin Benjamin
 * @author Sungmin Kim sk4900*/

public class WAMGame implements Runnable{

    /**an integer that defines the length of this game in seconds.*/
    private final int time;

    /**an integer that counts the columns of this game.*/
    private final int columns;

    /**an integer that counts the rows of this game.*/
    private final int rows;

    /**a matrix of Mole objects that represents the state of this game.*/
    private final Mole[][] moles;

    /**the truth value of this WAM game's playability.*/
    private boolean gameInProgress;

    private int[] scores;

    private WAMNetworkClient[] clients;
    /**...creates a WAMGame.
     * @param columns is an integer that counts the columns of the board
     * of this game.
     * @param rows is an integer that counts the rows of the board of this
     * game.
     * @param time is an integer that defines the length of this game in
     * seconds.*/
    public WAMGame(int columns, int rows, int time, WAMNetworkClient[] clients ) {
        gameInProgress = true;
        scores = new int[clients.length];
        this.clients = clients;
        this.time = time;
        this.columns = columns; this.rows = rows;
        moles = new Mole[columns][rows];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int id = x + (y * columns);
                moles[x][y] = new Mole(this, id);
            }
        }
    }
    /** adds a list of clients to this game */
    public void setClients(WAMNetworkClient[] clients){
        this.clients = clients;
    }
    /**play starts this game.*/
    public void play() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) { moles[x][y].start(); }
        }
    }

    /**isMoleUp
     * @return the truth value of a mole's accessibility.*/
    public synchronized boolean isMoleUp(int id) {
        int[] mole = getColumnRow(id);
        return moles[mole[0]][mole[1]].isUp();
    }

    /** sends a message to all clients to put a mole up
     * @param id id of mole */
    public synchronized void moleUp(int id){
        for(WAMNetworkClient client: clients){
            client.moleUp(id);
        }
    }
    public synchronized void moleWhack(int id, int playerNumber){
        if(isMoleUp(id)){
            findClient(playerNumber).addScore(2);
        }
        else{
            findClient(playerNumber).addScore(-1);
        }
        displayScores(getScores());
    }

    /** sends message to all clients to set mole down
     *  @param id id of mole */
    public synchronized void moleDown(int id){
        for(WAMNetworkClient client : clients){
            System.out.println(client.getConnectionOrder());
            client.moleDown(id);
        }
    }

    /**
     * finds a client with the given player number
     * @param playerID
     * @return
     */
    public WAMNetworkClient findClient(int playerID) {
        for (WAMNetworkClient client : clients) {
            if (playerID == client.getConnectionOrder()) {
                return client;
            }
        }
        return null;
    }

    public int[] getScores(){
        for(int i = 0; i < clients.length; i++){
            this.scores[i] = findClient(i).getScore();
        }
        return this.scores;
    }

    /**
     * displays current score to all clients. Called after score is changed for any client.
     */
    public void displayScores(int[] scores){
        for(WAMNetworkClient client : clients){
            client.sendScore(scores);
        }

    }
    /**isGameInProgress
     * @reutrn the truth value of this game's playability.*/
    public boolean isGameInProgress() { return gameInProgress; }

    /** return number of rows in the current game */
    public int getRows() { return rows; }

    /** returns number of columns in the current game */
    public int getColumns() { return columns; }

    @Override
    public void run(){
        play();
        while(isGameInProgress()){
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) / 1000 < time) {}
            gameInProgress = false;

        }
    }
    /**toString
     * @return a String that represents the state of this game.*/
    @Override
    public String toString() {
        String game = "";
        for (int y = rows - 1; y > -1; y--) {
            for (int x = 0; x < columns; x++) {
                game = game.concat("[" + moles[x][y].toString() + "]");
            }
            game = game.concat("\n");
        }
        return game;
    }

    /**getColumnRow represents the x-y-coordinate output of the parametric
     * equations C(x) = x % columns and R(x) = floor( x / columns ), where
     * ( C(x), R(x) ) is any location in a two-dimensional matrix of c columns
     * and r rows; 0 <= C(x) <= c, 0 <= R(x) <= r, and 0 <= x <= (c * r) - 1.
     * @param id is an integer that represents the intersection of a column and
     * a row at which an object exists.
     * @return an int[] that represents a location of a two-dimensional matrix.*/
    private int[] getColumnRow(int id) {
        return new int[] { (id % columns), (int) (Math.floor(id / columns))};
    }
}