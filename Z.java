package russianSquare;

public class Z extends Tetromino{
    /** Z shaped Tetromino. */
    public Z() {
        _cells[0] = new Cell(1, 4, Tetris._Z);
        _cells[1] = new Cell(0, 3, Tetris._Z);
        _cells[2] = new Cell(0, 4, Tetris._Z);
        _cells[3] = new Cell(1, 5, Tetris._Z);

        /** Two rotation states. */
        _states = new State[2];
        _states[0] = new State(0, 0, -1, -1, -1, 0, 0, 1);
        _states[1] = new State(0, 0, -1, 1, 0, 1, 1, 0);
    }
}
