package russianSquare;


import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;


public class Tetris extends JPanel {
    /** Generate the current square. */
    private Tetromino curr = Tetromino.random();

    /** Generate the next square. */
    private Tetromino next = Tetromino.random();

    /** Generate a place that the game is played. */
    private Cell[][] wall = new Cell[18][9];

    /**  Instantiate the size of each square to be 48. */
    private static final int CELL_SIZE = 48;

    /** Available stores. */
    int[] _scorePool = {0, 1, 2, 5, 10};

    /** Total score so far. */
    private int _totalScore = 0;

    /** Lines eliminated. */
    private int _totalLinesEliminated = 0;

    /** Possible states of the game. */
    public static final int IN_PLAY = 0;
    public static final int PAUSE = 1;
    public static final int GAME_OVER = 2;

    /** Current state of the game. */
    private int _gameState;

    /** An array containing the state of the game.*/
    String[] _showState = {"P[pause]", "R[resume]", "S[restart]"};

    /** Tracks the history of the state of the game. */
    Stack<Tetris> _history = new Stack<>();

    ArrayList<Integer> _bestMoves = new ArrayList<>();

    int WINNING_VALUE = 1000;

    /** Different shapes of tetris. */
    public static BufferedImage _I;

    public static BufferedImage _J;

    public static BufferedImage _L;

    public static BufferedImage _O;

    public static BufferedImage _S;

    public static BufferedImage _T;

    public static BufferedImage _Z;

    public static BufferedImage _background;

    static {
        try {
            // FIXME: new File("/d/龙奕/Berkeley/大一下学期/cs61b/sp21-s1670");
            // FIXME: new File("/d龙奕/Berkeley/大二上/Russian/beach");
            _I = ImageIO.read(new File("D:\\龙奕\\Berkeley\\大二上\\Russian\\beach1.jpg"));
            _J = ImageIO.read(new File("D:\\龙奕\\Berkeley\\大二上\\Russian\\decree1.jpg"));
            _L = ImageIO.read(new File("D:\\龙奕\\Berkeley\\大二上\\Russian\\big c1.jpg"));
            _O = ImageIO.read(new File("D:\\龙奕\\Berkeley\\大二上\\Russian\\cat1.jpg"));
            _S = ImageIO.read(new File("D:\\龙奕\\Berkeley\\大二上\\Russian\\scene1.jpg"));
            _T = ImageIO.read(new File("D:\\龙奕\\Berkeley\\大二上\\Russian\\internet1.jpg"));
            _Z = ImageIO.read(new File("D:\\龙奕\\Berkeley\\大二上\\Russian\\fear1.jpg"));
            _background = ImageIO.read(new File("D:\\龙奕\\Berkeley\\大二上\\Russian\\big c5.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Use shift + F2 to fast create method. */
    @Override
    public void paint(Graphics g) {
        /** G is the paint brush. */
        g.drawImage(_background, 0, 0, null);

        /** Move axis. */
        g.translate(22, 15);

        /** Paint the interface. */
        paintWall(g);

        /** Paint the current pattern to have a specific image and shape. */
        paintCurrTile(g);

        /** Paint the next pattern to have a specific image and shape. */
        paintNextTile(g);

        /** Paint total score. */
        paintScore(g);

        paintState(g);
    }

    public void start() {
        _gameState = IN_PLAY;
        KeyListener l = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                switch (code) {
                    case KeyEvent.VK_DOWN:
                        dropOnce();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeftAction();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRightAction();
                        break;
                    case KeyEvent.VK_UP:
                        rotateClockwiseAction();
                        break;
                    case KeyEvent.VK_SPACE:
                        dropAllTheWay();
                        break;
                    case KeyEvent.VK_P:
                        if (_gameState == IN_PLAY) {
                            _gameState = PAUSE;
                        }
                        break;
                    case KeyEvent.VK_C:
                        if (_gameState == PAUSE) {
                            _gameState = IN_PLAY;
                        }
                        break;
                    case KeyEvent.VK_S:
                        _gameState = IN_PLAY;
                        wall = new Cell[18][9]; //FIXME
                        curr = Tetromino.random();
                        next = Tetromino.random();
                        _totalScore = 0;
                        _totalLinesEliminated = 0;
                        break;
                    case KeyEvent.VK_A:
                        autoPlayOnce();
                        break;
                    case KeyEvent.VK_B:
                        sophisticatedProcess();
                        break;
                }
            }
        };
        /** Set the window to be the focus. */
        this.addKeyListener(l);
        this.requestFocus();

        /** If in-play, drop the tile every 0.5 second. */
        while (true) {
            if (_gameState == IN_PLAY) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (canFall()) {
                    curr.softDown();
                } else {
                    writeIntoWall();
                    clearLine();
                    nextTurn();
                }
            }
            repaint();
        }
    }

    /** Infer what operation is the best and do the operation,
     * but do not drop the cells. There might be a combination of actions.
     * sometimes one move is better than two.
     */
    private void autoPlayOnce() {
        //_bestMoves = new ArrayList<>();
        searchForMove();
        if (_bestMoves.size() == 0 && false) {
            dropAllTheWay();
        } else {
            for (int i = _bestMoves.size() - 1; i >= 0; i -= 1) {
                numToAction(_bestMoves.get(i));
            }
        }
    }


    private void searchForMove() {
        _bestMoves = new ArrayList<>();
        int val = findMax(this, 5, true);
    }

    private int findMax(Tetris t, int depth, boolean saveMove) {
        if (depth == 0) {
            markUndo();
            dropAllTheWay();
            writeIntoWall();
            clearLine();
            int score = staticEval();
            undo();
            return score;
        }
        int bestSoFar = -100, bestSoFarPos = 0;
        for (int i = 0; i < 4; i += 1) {
            numToAction(i);
            int response = findMax(makeCopy(), depth - 1, true);
            if (response > bestSoFar) {
                bestSoFar = response;
                bestSoFarPos = i;
                //saveMove = true;
            }
            undo();
        }
        if (saveMove) {
            _bestMoves.add(bestSoFarPos);
        }
        return bestSoFar;
    }

    private void numToAction(int num) {
        markUndo();
        if (num == 0) {
            moveLeftAction();
        } else if (num == 1) {
            moveRightAction();
        } else if (num == 2){
            rotateClockwiseAction();
        } else {
            doNothing();
        }
    }

    private void doNothing() {

    }

    private void undo() {
        if (!_history.isEmpty()) {
            internalCopy(_history.pop());
        }
    }

    private void markUndo() {
        Tetris copy = new Tetris();
        copy.internalCopy(this);
        _history.push(copy);
    }

    private void internalCopy(Tetris t){
        for (int i = 0; i < wall.length; i += 1) {
            for (int j = 0; j < wall[0].length; j += 1) {
                wall[i][j] = t.wall[i][j];
            }
        }
        _totalScore = t._totalScore;
        _totalLinesEliminated = t._totalLinesEliminated;
        _gameState = t._gameState;
        curr = t.curr;
        next = t.next;
    }

    private Tetris makeCopy() {
        Tetris copy = new Tetris();
        copy.internalCopy(this);
        return copy;
    }

    private int staticEval() {
        return evaluateFunction(curr);
    }

    public Stack<Tetris> getHistory() {
        return _history;
    }

    public void rotateClockwiseAction() {
        curr.rotateClockwise();
        if (outOfBounds() || overlap()) {
            curr.rotateCounterClockwise();
        }
    }

    public void nextTurn() {
        if (isGameOver()) {
            _gameState = GAME_OVER;
        } else {
            curr = next;
            next = Tetromino.random();
        }
    }

    public void dropAllTheWay() {
        while (canFall()) {
            curr.softDown();
        }
        writeIntoWall();
        clearLine();
        nextTurn();
    }

    public void dropOnce() {
        if (canFall()) {
            curr.softDown();
        } else {
            writeIntoWall();
            clearLine();
            nextTurn();
        }
    }

    private void writeIntoWall() {
        Cell[] cells = curr._cells;
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            wall[row][col] = cell;
        }
    }

    public boolean canFall() {
        Cell[] cells = curr._cells;
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            if (row == wall.length - 1) {
                return false;
            } else if (wall[row + 1][col] != null) {
                return false;
            }
        }
        return true;
    }

    public void clearLine() {
        int line = 0;
        Cell[] cells = curr._cells;
        for (Cell cell : cells) {
            int row = cell.getRow();
            if (isFullLine(row)) {
                line += 1;
                for (int i = row; i > 0; i -= 1) {
                    System.arraycopy(wall[i - 1], 0, wall[i], 0, wall[0].length);
                }
                wall[0] = new Cell[9];
            }
        }
        _totalScore += _scorePool[line];
        _totalLinesEliminated += line;
    }

    public boolean isFullLine(int row) {
        Cell[] cells =wall[row];
        for (Cell cell : cells) {
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        Cell[] cells = next._cells;
        for (Cell cell: cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            if (wall[row][col] != null) {
                return true;
            }
        }
        return false;
    }

    private void paintState(Graphics g) {
        if (_gameState == IN_PLAY) {
            g.drawString(_showState[IN_PLAY], 500, 510);
        } else if (_gameState == PAUSE) {
            g.drawString(_showState[PAUSE], 500, 510);
        } else {
            g.drawString(_showState[GAME_OVER], 500, 510);
            g.setColor(Color.CYAN);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
            g.drawString("GAME OVER :)", 30, 350);
        }
    }

    private void paintScore(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        g.drawString("SCORE: " + _totalScore, 500, 220);
        g.drawString("LINE(S): " + _totalLinesEliminated, 500, 370);
        //g.drawString("best: " + getAllPossiblePos(curr), 500, 500);
    }

    private void paintNextTile(Graphics g) {
        Cell[] cells = next._cells;;
        for (Cell cell: cells) {
            int x = cell.getCol() * CELL_SIZE + 380;
            int y = cell.getRow() * CELL_SIZE + 25;
            g.drawImage(cell.getImage(), x, y, null);
        }
    }

    private void paintCurrTile(Graphics g) {
        Cell[] cells = curr._cells;
        for (Cell cell: cells) {
            int x = cell.getCol() * CELL_SIZE;
            int y = cell.getRow() * CELL_SIZE;
            g.drawImage(cell.getImage(), x, y, null);
        }
    }

    private void paintWall(Graphics g) {
        for (int i = 0; i < wall.length; i += 1) {
            for (int j = 0; j < wall[i].length; j += 1) {
                /** x and y are flipped. */
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;
                Cell cell = wall[i][j];
                if (cell == null) {
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawImage(cell.getImage(), x, y, null);
                }
            }
        }
    }

    /** Check if a tile is out of bound. */
    public boolean outOfBounds() {
        Cell[] cells = curr._cells;
        for (Cell cell: cells) {
            int col = cell.getCol();
            int row = cell.getRow();
            if (row < 0 || row > wall.length - 1 || col < 0 || col > wall[0].length - 1) {
                return true;
            }
        }
        return false;
    }

    public boolean overlap() {
        Cell[] cells = curr._cells;
        for (Cell cell: cells) {
            int col = cell.getCol();
            int row = cell.getRow();
            if (wall[row][col] != null) {
                return true;
            }
        }
        return false;
    }

    public void moveLeftAction() {
        curr.moveLeft();
        if (outOfBounds() || overlap()) {
            curr.moveRight();
        }
    }

    public void moveRightAction() {
        curr.moveRight();
        if (outOfBounds() || overlap()) {
            curr.moveLeft();
        }
    }

    public boolean conflict(Tetromino t, int state) {
        for (int i = 0; i < state; i += 1) {
            t.rotateClockwise();
        }
        Cell[] cells = t._cells;
        for (Cell cell: cells) {
            int col = cell.getCol();
            int row = cell.getRow();
            if (row < 0 || row > wall.length - 1 || col < 0
                    || col > wall[0].length - 1 || wall[row][col] != null) {
                return true;
            }
        }
        return false;
    }

    public static Tetromino copyTetro(Tetromino t) {
        Tetromino copy = new Tetromino();
        copy._states = new Tetromino.State[t._states.length];
        for (int i = 0; i < t._cells.length; i += 1) {
            copy._cells[i] = new Cell(t._cells[i].getRow(), t._cells[i].getCol(), t._cells[i].getImage());
        }
        for (int i = 0; i < t._states.length; i += 1) {
            copy._states[i] = new Tetromino.State(t._states[i].row0, t._states[i].col0,
                    t._states[i].row1, t._states[i].col1, t._states[i].row2,
                    t._states[i].col2, t._states[i].row3, t._states[i].col3);
        }
        return copy;
    }

    /** returns all possible canter position of all states of a tetromino can take. */
    public HashMap<Integer, Integer> getAllPossiblePos(Tetromino t) {
        HashMap<Integer, Integer> result = new HashMap<>();
        for (int k = 0; k < t._states.length; k += 1) {
            for (int j = 0; j < wall[0].length; j += 1) {
                for (int i = 0; i < wall.length - 1; i += 1) {
                    //int row0 = t._cells[0].getRow(), col0 = t._cells[0].getCol();
                    //int row1 = t._cells[1].getRow(), col1 = t._cells[1].getCol();
                    //int row2 = t._cells[2].getRow(), col2 = t._cells[2].getCol();
                    //int row3 = t._cells[3].getRow(), col3 = t._cells[3].getCol();
                    Tetromino copy1 = copyTetro(t);
                    copy1.moveTo(i + 1, j, copy1._states[k]);
                    Tetromino copy2 = copyTetro(t);
                    copy2.moveTo(i, j, copy2._states[k]);
                    if (conflict(copy1, k) == true && conflict(copy2, k) == false) {
                        if (!result.keySet().contains(i * 10 + j)) {
                            int condensedCenter = i * 10 + j;
                            result.put(condensedCenter, k);
                        }
                    }
                }
            }
        }
        return  result;
    }

    public int getLandingHeight(Tetromino t) {
        return wall.length - 1 - t._cells[0].getRow();
    }

    public int getEliminatedLineCellMetric(Tetromino t) {
        int lines = 0;
        int usefulBlocks = 0;
        for (int i = wall.length - 1; i >= 0; i -= 1) {
            int count = 0;
            for (int j = 0; j < wall[0].length; j += 1) {
                if (wall[i][j] != null) {
                    count += 1;
                }
                if (count == wall[0].length) {
                    lines += 1;
                    Cell[] cells = t._cells;
                    for (Cell cell: cells) {
                        if (cell.getRow() == i) {
                            usefulBlocks += 1;
                        }
                    }
                } else if (count == 0) {
                    break;
                }
            }
        }
        return lines * usefulBlocks;
    }

    public int getBoardRowTransitions() {
        int transition = 0;
        for (int i = wall.length - 1; i >= 0; i -= 1) {
            int count = 0;
            for (int j = 0; j < wall[0].length - 1; j += 1) {
                if (wall[i][j] != null) {
                    count += 1;
                } else if (wall[i][j] == null && wall[i][j + 1] != null) {
                    transition += 1;
                } else if (wall[i][j] != null && wall[i][j + 1] == null) {
                    transition += 1;
                }
            }
        }
        return transition;
    }

    public int getBoardColTransition() {
        int transition = 0;
        for (int j = 0; j < wall[0].length; j += 1) {
            for (int i = wall.length - 1; i >= 1; i -= 1) {
                if (wall[i][j] == null && wall[i - 1][j] != null) {
                    transition += 1;
                } else if (wall[i][j] != null && wall[i - 1][j] == null) {
                    transition += 1;
                }
            }
        }
        return transition;
    }

    public int getBoardBuriedHoles() {
        int holes = 0;
        for (int j = 0; j < wall[0].length; j += 1) {
            int colHoles = -1;
            for (int i = 0; i < wall.length; i += 1) {
                if (colHoles == -1 && wall[i][j] != null) {
                    colHoles = 0;
                } else if (colHoles != -1 && wall[i][j] == null) {
                    colHoles += 1;
                }
            }
            if (colHoles != -1) {
                holes += colHoles;
            }
        }
        return holes;
    }

    public int getBoardWells() {
        int[] sum_n = {0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66};
        int wells = 0;
        int sum = 0;

        for (int j = 0; j < wall[0].length; j += 1) {
            for (int i = 0; i < wall.length; i += 1) {
                if (wall[i][j] == null) {
                    if ((j - 1 < 0 || wall[i][j - 1] != null)
                            && (j + 1 >= wall[0].length || wall[i][j + 1] != null)) {
                        wells += 1;
                    } else {
                        sum += sum_n[wells];
                        wells = 0;
                    }
                }
            }
        }
        return sum;
    }

    public int getPrioritySelection(Tetromino t) {
        int targetState = t._numRotate % t._states.length;
        int currState = curr._numRotate % curr._states.length;
        int colNum = Math.abs(((wall[0].length - 1) / 2) - t._cells[0].getCol());
        int rotateTimes;
        if (targetState >= currState) {
            rotateTimes = targetState - currState;
        } else {
            rotateTimes = t._states.length + targetState + currState;
        }
        int result = colNum * 100 + rotateTimes;
        if (t._cells[0].getCol() < (wall[0].length - 1) / 2) {
            result += 10;
        }
        return  result;
    }

    public int evaluateFunction(Tetromino t) {
        int lh = getLandingHeight(t);
        int epcm = getEliminatedLineCellMetric(t);
        int brt = getBoardRowTransitions();
        int bct = getBoardColTransition();
        int bbh = getBoardBuriedHoles();
        int bw = getBoardWells();
        int score = -45 * lh + 34 * epcm - 32 * brt - 98 * bct - 79 * bbh - 34 * bw;
        return score;
    }

    public void sophisticatedProcess() {
        HashMap<Integer, Integer> pos = getAllPossiblePos(curr);
        int bestScore = -999999;
        int bestRow = -1, bestCol = -1, bestState = -1;
        Tetromino bestTetro = null;
        for (int condensedCoordinate: pos.keySet()) {
            int row = (condensedCoordinate / 10) % 100;
            int col = condensedCoordinate % 10;
            int stateNum = pos.get(condensedCoordinate);
            Tetromino attempt = createTetro(curr, row, col, stateNum);
            int score = evaluateFunction(attempt);
            if (score > bestScore) {
                bestScore = score;
                bestRow = row;
                bestCol = col;
                bestState = stateNum;
                bestTetro = attempt;
                _bestScore = bestScore;
            } else if (score == bestScore) {
                if (getPrioritySelection(attempt) < getPrioritySelection(bestTetro)) {
                    bestScore = score;
                    bestRow = row;
                    bestCol = col;
                    bestState = stateNum;
                    bestTetro = attempt;
                    _bestScore = bestScore;
                }
            }
        }
        curr.moveTo(bestRow, bestCol, curr._states[bestState % curr._states.length]);
        writeIntoWall();
        clearLine();
    }


    private int _bestScore;
    public Tetromino createTetro(Tetromino t, int centerRow, int centerCol, int state) {
        Tetromino copy = copyTetro(t);
        Tetromino.State s = copy._states[state % copy._states.length];
        copy.moveTo(centerRow, centerCol, s);
        return copy;
    }

    public static void main(String[] args) {
        /** Creates a frame object. */
        JFrame frame = new JFrame("Russian Square");

        /** Creates the game interface. */
        Tetris panel = new Tetris();

        /** Put the game interface in the frame. */
        frame.add(panel);

        /** Makes the frame visible. */
        frame.setVisible(true);

        /** Set the size of this frame. */
        frame.setSize(810, 940);

        /** Put in the middle. */
        frame.setLocationRelativeTo(null);

        /** Terminates the game after the window is close. */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /** Main logic of the game. */
        panel.start();
    }
}
