package russianSquare;

public class I extends Tetromino{
    public I() {
        _cells[0] = new Cell(0, 4, Tetris._I);
        _cells[1] = new Cell(0, 3, Tetris._I);
        _cells[2] = new Cell(0, 5, Tetris._I);
        _cells[3] = new Cell(0, 6, Tetris._I);

        /** Two rotation states. */
        _states = new State[2];
        _states[0] = new State(0, 0, 0, -1, 0, 1, 0, 2);
        _states[1] = new State(0, 0, -1, 0, 1, 0, 2, 0);
    }
}
