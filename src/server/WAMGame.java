package server;

import java.io.IOException;

import java.lang.Thread;

import java.net.Socket;

import java.util.Random;

import static common.WAMProtocol.*;
import static java.lang.Thread.State.TIMED_WAITING;

/***/
public class WAMGame {

    /***/
    private final int time;

    /***/
    private final int columns;

    /***/
    private final int rows;

    /***/
    private final Mole[][] moles;

    /***/
    private boolean gameInProgress;

    /***/
    private WAMNetworkClient[] clients;
    /***/
    private class Mole extends Thread {

        /***/
        private final int MAX_TIME = 3;

        /***/
        public Mole() { this.start(); }

        /***/
        @Override
        public void run() {}

        /***/
        public boolean isUp() {
            return this.getState().equals(TIMED_WAITING);
        }
    }

    /***/
    public WAMGame(int columns, int rows, int time, WAMNetworkClient[] clients) {
        this.time = time;
        this.clients = clients;
        this.columns = columns; this.rows = rows;
        moles = new Mole[columns][rows];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) { moles[x][y] = new Mole(); }
        }
        gameInProgress = true;
    }

    /***/
    public void play() {
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) / 1000 < time) {}
        gameInProgress = false;
    }

    /***/
    public synchronized boolean isMoleUp(int id) {
        int[] mole = getColumnRow(id);
        return moles[mole[0]][mole[1]].isUp();
    }
    /***/
    public boolean isGameInProgress() { return gameInProgress; }

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