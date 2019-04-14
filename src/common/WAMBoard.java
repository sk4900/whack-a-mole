package common;

/**WAMBoard represents the board of a Whack-A-Mole game and includes
 * methods for updating its state.
 * @author Kadin Benjamin ktb1193*/
public class WAMBoard {

    /**an integer that represents the count of the board's columns.*/
    private int columns;

    /**an integer that represents the count of the board's rows.*/
    private int rows;

    /**a two-dimensional matrix that represents the state of each
     * mole, where each mole is located at the intersection of some
     * column c (0 <= c <= columns) with some row r(0 <= r <= rows).*/
    private int[][] board;

    /**...creates a WAMBoard of specifiable rows and specifiable columns.*/
    public WAMBoard(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        board = new int[columns][rows];
    }

    /**getColumns
     * @return an int that is the count of this board's columns.*/
    public int getColumns() { return columns; }

    /**getRows
     * @return an int that is the count of this board's rows.*/
    public int getRows() { return rows; }

    /**setMoleUp sets the integer value at some location of this
     * board equal to one; this represents a surfaced mole.*/
    public void setMoleUp(int x) {
        int[] xy = getColumnRow(x);
        board[xy[0]][xy[1]] = 1;
    }

    /**setMoleDown sets the integer value at some location of this
     * board equal to zero; this represents a submerged mole.*/
    public void setMoleDown(int x) {
        int[] xy = getColumnRow(x);
        board[xy[0]][xy[1]] = 0;
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