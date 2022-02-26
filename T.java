package russianSquare;

public class T extends Tetromino{
    public T() {
        _cells[0] = new Cell(0, 4, Tetris._T);
        _cells[1] = new Cell(0, 3, Tetris._T);
        _cells[2] = new Cell(0, 5, Tetris._T);
        _cells[3] = new Cell(1, 4, Tetris._T);

        /** Four rotation states. */
        _states = new State[4];
        /** Coordinates. */
        _states[0] = new State(0, 0, 0, -1, 0, 1, 1, 0);
        _states[1] = new State(0, 0, -1, 0, 1, 0, 0, -1);
        _states[2] = new State(0, 0, 0, 1, 0, -1, -1, 0);
        _states[3] = new State(0, 0, 1, 0, -1, 0, 0, 1);
    }
}
