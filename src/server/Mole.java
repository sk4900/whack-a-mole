package server;

import java.util.Random;

import static java.lang.Thread.State.TIMED_WAITING;

/**Mole is an class whose instances compose the board of a WAM game.*/
public class Mole extends Thread {

    /**an integer that defines the maximum time in seconds during which
     * a mole may hide.*/
    private final int MAX_SLEEP_TIME = 3;

    /**a pseudorandom number generator of this Mole.*/
    private final Random random = new Random();

    /**a reference to a preexisting WAMgame.*/
    private final WAMGame game;

    /**...creates a Mole.
     * @param
     * @param */
    public Mole(WAMGame game) { this.game = game; }

    /**run determines a mole's behavior while this game is playable.
     * If a mole is sleeping, then it is represented as inaccessible.
     * If a mole is not sleeping, then it is represented as accessible.
     * Hitting an accessible mole awards positive points to a client's
     * score, and hitting an inaccessible mole awards the opposite.*/
    @Override
    public void run() {
        while (game.isGameInProgress()) {
            if (random.nextInt(3) > 1) {
                long sleepTime = random.nextInt(MAX_SLEEP_TIME) + 1;
                try { this.sleep(sleepTime * 500); }
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
    public boolean isUp() { return !this.getState().equals(TIMED_WAITING); }
}