package common;

import java.util.LinkedList;
import java.util.List;
import client.gui.Observer;
/**WAMBoard represents the board of a Whack-A-Mole game and includes
 * methods for updating its state.
 * @author Kadin Benjamin ktb1193
 * @author Sungmin Kim  sk4900*/

public class WAMBoard {

    /**an integer that represents the count of the board's columns.*/
    private int columns;

    /**an integer that represents the count of the board's rows.*/
    private int rows;

    /**a two-dimensional matrix that represents the state of each
     * mole, where each mole is located at the intersection of some
     * column c (0 <= c <= columns) with some row r(0 <= r <= rows).*/
    private int[][] board;

    /** the observers of this model */
    private List<Observer<WAMBoard>> observers;

    /**...creates a WAMBoard of specifiable rows and specifiable columns.*/
    public WAMBoard(int columns, int rows) {
        this.observers =new LinkedList<>();
        this.columns = columns;
        this.rows = rows;
        board = new int[columns][rows];
    }

    /**
     * The view calls this method to add themselves as an observer of the model.
     *
     * @param observer the observer
     */
    public void addObserver(Observer<WAMBoard> observer) {
        this.observers.add(observer);
    }

    /** when the model changes, the observers are notified via their update() method */
    private void alertObservers() {
        for (Observer<WAMBoard> obs: this.observers ) {
            obs.update(this);
        }
    }
    /**getColumns
     * @return an int that is the count of this board's columns.*/
    public int getColumns() { return columns; }

    /**getRows
     * @return an int that is the count of this board's rows.*/
    public int getRows() { return rows; }

    /**
     * returns 1 or 0 if mole is up or down respectively
     * @param row
     * @param column
     * @return
     */
    public int getMoleStatus(int row, int column) {return board[row][column];}

    /**setMoleUp sets the integer value at some location of this
     * board equal to one; this represents a surfaced mole.*/
    public void setMoleUp(int x) {
        int[] xy = getColumnRow(x);
        board[xy[0]][xy[1]] = 1;
        alertObservers();

    }

    /**setMoleDown sets the integer value at some location of this
     * board equal to zero; this represents a submerged mole.*/
    public void setMoleDown(int x) {
        int[] xy = getColumnRow(x);
        board[xy[0]][xy[1]] = 0;
        alertObservers();
    }

    /**toString
     * @return a String representation of the board's state.*/
    @Override
    public String toString() {
        String s = "";
        for (int y = rows - 1; y > -1; y--) {
            for (int x = 0; x < columns; x++) {
                s = s.concat("[" + String.valueOf(board[x][y]) + "]");
            }
            s = s.concat("\n");
        }
        return s;
    }

    /**getColumnRow represents the x-y-coordinate output of the parametric
     * equations C(x) = x % columns and R(x) = floor( x / columns ), where
     * ( C(x), R(x) ) is any location in a two-dimensional matrix of c columns
     * and r rows; 0 <= C(x) <= c, 0 <= R(x) <= r, and 0 <= x <= (c * r) - 1.
     * @return an int[] that represents a location of a two-dimensional matrix.*/
    private int[] getColumnRow(int x) {
        return new int[] { (x % columns), (int) Math.floor(x / columns) };
    }
}