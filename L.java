package russianSquare;

public class L extends Tetromino{
    public L() {
        _cells[0] = new Cell(0, 4, Tetris._L);
        _cells[1] = new Cell(0, 3, Tetris._L);
        _cells[2] = new Cell(0, 5, Tetris._L);
        _cells[3] = new Cell(1, 3, Tetris._L);

        /** Four rotation states. */
        _states = new State[4];
        _states[0] = new State(0, 0, 0, -1, 0, 1, 1, -1);
        _states[1] = new State(0, 0, -1, 0, 1, 0, -1, -1);
        _states[2] = new State(0, 0, 0, 1, 0, -1, -1, 1);
        _states[3] = new State(0, 0, 1, 0, -1, 0, 1, 1);
    }
}
