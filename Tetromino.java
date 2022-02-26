package russianSquare;

import com.google.common.net.*;

public class Tetromino {

    protected Cell[] _cells = new Cell[4];

    protected State[] _states;

    protected int _numRotate = 10000;

    static class State {
        int row0, col0, row1, col1, row2, col2, row3, col3;

        public State() {

        }

        public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3) {
            this.row0 = row0;
            this.col0 = col0;
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
            this.row3 = row3;
            this.col3 = col3;
        }

        public int getRow0() {
            return row0;
        }

        public int getCol0() {
            return col0;
        }

        public int getRow1() {
            return row1;
        }

        public int getCol1() {
            return col1;
        }

        public int getRow2() {
            return row2;
        }

        public int getCol2() {
            return col2;
        }

        public int getRow3() {
            return row3;
        }

        public int getCol3() {
            return col3;
        }

        public void setRow0(int row0) {
            this.row0 = row0;
        }

        public void setCol0(int col0) {
            this.col0 = col0;
        }

        public void setRow1(int row1) {
            this.row1 = row1;
        }

        public void setCol1(int col1) {
            this.col1 = col1;
        }

        public void setRow2(int row2) {
            this.row2 = row2;
        }

        public void setCol2(int col2) {
            this.col2 = col2;
        }

        public void setRow3(int row3) {
            this.row3 = row3;
        }

        public void setCol3(int col3) {
            this.col3 = col3;
        }

        @Override
        public String toString() {
            return "State{" +
                    "row0=" + row0 +
                    ", col0=" + col0 +
                    ", row1=" + row1 +
                    ", col1=" + col1 +
                    ", row2=" + row2 +
                    ", col2=" + col2 +
                    ", row3=" + row3 +
                    ", col3=" + col3 +
                    '}';
        }
    }

    /** Rotate clockwise. */
    public void rotateClockwise() {
        if (_states.length != 0) {
            _numRotate += 1;
            rotateAction();
        }
    }

    public void rotateCounterClockwise() {
        if (_states.length != 0) {
            _numRotate -= 1;
            rotateAction();
        }
    }

    public void rotateAction() {
        State s = _states[_numRotate % _states.length];
        Cell cell = _cells[0];
        int row = cell.getRow();
        int col = cell.getCol();
        _cells[1].setRow(row + s.row1);
        _cells[1].setCol(col + s.col1);
        _cells[2].setRow(row + s.row2);
        _cells[2].setCol(col + s.col2);
        _cells[3].setRow(row + s.row3);
        _cells[3].setCol(col + s.col3);
    }

    public void moveTo(int i, int j, State s) {
        _cells[0].setRow(i);
        _cells[0].setCol(j);
        _cells[1].setRow(i + s.row1);
        _cells[1].setCol(j + s.col1);
        _cells[2].setRow(i + s.row2);
        _cells[2].setCol(j + s.col2);
        _cells[3].setRow(i + s.row3);
        _cells[3].setCol(j + s.col3);
    }

    public void moveLeft() {
        for (Cell cell: _cells) {
            cell.left();
        }
    }

    public void moveRight() {
        for (Cell cell: _cells) {
            cell.right();
        }
    }

    public void softDown() {
        for (Cell cell: _cells) {
            cell.down();
        }
    }

    public static Tetromino random() {
        int rand = (int) (Math.random() * 7);
        Tetromino tetromino = null;
        switch (rand) {
            case 0:
                tetromino = new I();
                break;
            case 1:
                tetromino = new J();
                break;
            case 2:
                tetromino = new L();
                break;
            case 3:
                tetromino = new O();
                break;
            case 4:
                tetromino = new S();
                break;
            case 5:
                tetromino = new T();
                break;
            case 6:
                tetromino = new Z();
                break;
        }
        return tetromino;
    }
}
