package russianSquare;

public class O extends Tetromino{
    public O() {
        _cells[0] = new Cell(0, 4, Tetris._O);
        _cells[1] = new Cell(0, 5, Tetris._O);
        _cells[2] = new Cell(1, 4, Tetris._O);
        _cells[3] = new Cell(1, 5, Tetris._O);

        /** No rotation state. */
        _states = new State[0];
    }
}
