package russianSquare;

import java.awt.image.BufferedImage;
import java.util.*;

public class Cell {
    /** Row number of this cell. */
    private int _row;

    /** Column number of this cell. */
    private int _col;

    /** Image. */
    private BufferedImage _image;

    /** Default constructor. */
    public Cell(){

    }

    /** Main constructor. */
    public Cell(int row, int col, BufferedImage image) {
        _row = row;
        _col = col;
        _image = image;
    }

    public int getRow() {
        return _row;
    }

    public int getCol() {
        return _col;
    }

    public BufferedImage getImage() {
        return _image;
    }

    public void setRow(int row) {
        _row = row;
    }

    public void setCol(int col) {
        _col = col;
    }

    public void setImage(BufferedImage image) {
        _image = image;
    }

    @Override
    public String toString() {
        String str = "Cell {" + "row = " + _row +
                ", col = " + _col + ", image = " + _image + '}';
        return str;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null
                || getClass() != other.getClass()) {
            return false;
        }
        Cell obj = (Cell) other;
        return _row == obj.getRow()
                && _col == obj.getCol()
                && Objects.equals(_image, obj.getImage());
    }

    public void left() {
        _col -= 1;
    }

    public void right() {
        _col += 1;
    }
    public void down() {
        _row += 1;
    }
}
