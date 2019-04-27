package server;

import java.io.IOException;

import java.lang.InterruptedException;
import java.lang.Thread;

import java.net.Socket;

import java.util.Random;

import static common.WAMProtocol.*;
import static java.lang.Thread.State.TIMED_WAITING;

/**WAMGame represents a game of Whack-A-Mole.
 * @author Kadin Benjamin ktb1193*/
public class WAMGame {

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

    /**Mole is an inner class whose instances compose the representation
     * of the board of this WAM game.*/
    private class Mole extends Thread {

        /**an integer that defines the maximum time in seconds during which
         * a mole may hide.*/
        private final int MAX_SLEEP_TIME = 3;

        /**a pseudorandom number generator of this Mole.*/
        private final Random random = new Random();

        /**...creates a Mole.*/
        public Mole() { this.start(); }

        /**run determines a mole's behavior while this game is playable.
         * If a mole is sleeping, then it is represented as inaccessible.
         * If a mole is not sleeping, then it is represented as accessible.
         * Hiting an accessible mole awards positive points to a client's
         * score, and hiting an inaccessible mole awards the opposite.*/
        @Override
        public void run() {
            while (gameInProgress) {
                if (random.nextInt(2) > 1) {
                    long sleepTime = random.nextInt(MAX_SLEEP_TIME);
                    try { this.sleep(sleepTime * 1000); }
                    catch (InterruptedException ie) {  }
                }
            }
        }

        /**toString
         * @return a String that represents a mole's state.*/
        @Override
        public String toString() {
            if (isUp()) { return "1"; }
            return "0";
        }

        /**isUp
         * @return the truth value of mole's accessibility.*/
        public boolean isUp() {
            return !this.getState().equals(TIMED_WAITING);
        }
    }

    /**...creates a WAMGame.
     * @param columns is an integer that counts the columns of the board
     * of this game.
     * @param rows is an integer that counts the rows of the board of this
     * game.
     * @param time is an integer that defines the length of this game in
     * seconds.*/
    public WAMGame(int columns, int rows, int time) {
        this.time = time;
        this.columns = columns; this.rows = rows;
        moles = new Mole[columns][rows];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) { moles[x][y] = new Mole(); }
        }
        gameInProgress = true;
    }

    /**play starts this game.*/
    public void play() {
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) / 1000 < time) {}
        gameInProgress = false;
    }

    /**isMoleUp
     * @return the truth value of a mole's accessibility.*/
    public synchronized boolean isMoleUp(int id) {
        int[] mole = getColumnRow(id);
        return moles[mole[0]][mole[1]].isUp();
    }

    /**isGameInProgress
     * @reutrn the truth value of this game's playability.*/
    public boolean isGameInProgress() { return gameInProgress; }

    /**toString
     * @return a String that represents the state of this game.*/
    @Override
    public String toString() {
        String game = "";
        for (int y = rows - 1; y > -1; y--) {
            for (int x = 0; x < columns; x++) {
                game = game.concat("[" + moles[x][y].toString() + "]");
            }
        }
        return game;
    }

    /***/
    public void moleWhacked(int id, WAMNetworkClient player){
        player.addScore(2);
        if(player.moleWhacked(id)){
            player.moleDown(id);
        }
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