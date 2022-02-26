package russianSquare;

public class S extends Tetromino{
    public S() {
        _cells[0] = new Cell(0, 4, Tetris._S);
        _cells[1] = new Cell(0, 5, Tetris._S);
        _cells[2] = new Cell(1, 3, Tetris._S);
        _cells[3] = new Cell(1, 4, Tetris._S);

        /** Two Rotation states. */
        _states = new State[2];
        _states[0] = new State(0, 0, 0, 1, 1, -1, 1, 0);
        _states[1] = new State(0, 0, 1, 0, -1, -1, 0, -1);
    }
}
