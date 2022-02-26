package russianSquare;

public class J extends Tetromino{
    public J() {
        _cells[0] = new Cell(0, 4, Tetris._J);
        _cells[1] = new Cell(0, 3, Tetris._J);
        _cells[2] = new Cell(0, 5, Tetris._J);
        _cells[3] = new Cell(1, 5, Tetris._J);
        /** Four rotation states. */
        _states = new State[4];
        _states[0] = new State(0, 0, 0, -1, 0, 1, 1, 1);
        _states[1] = new State(0, 0, -1, 0, 1, 0, 1, -1);
        _states[2] = new State(0, 0, 0, 1, 0, -1, -1, -1);
        _states[3] = new State(0, 0, 1, 0, -1, 0, -1, 1);
    }
}
