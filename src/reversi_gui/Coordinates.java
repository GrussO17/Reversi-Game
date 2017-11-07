package reversi_gui;

public class Coordinates {

    /**
     * Stores the row and col of the specific coordinate
     */
    private int row;
    private int col;

    /**
     * constructs the coordinate
     * @param row
     * @param col
     */
    public Coordinates(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * row accessor method
     * @return row
     */
    public int getRow(){
        return row;
    }

    /**
     * col accessor method
     * @return col
     */
    public int getCol(){
        return col;
    }
}
