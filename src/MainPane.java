import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Created by 3943 on 2017/06/10.
 */

/*
 * as: fxml root pane, fxml controller
 * has: canvas, game data
 *
 * bahaves as game board
 * 'game' class thread roles integration on this instance
 */

public class MainPane extends BorderPane{
    private SidePane sidepane;
    private Thread gThread;
    private GraphicsContext graphicsContext;
    public Cell[][] field; // entity

    private boolean gameActive;
    private int turn;

    private int line, column;
    private int span; //(ms)

    MainPane() {
        // bind (searching bitter way
        setMaxWidth(500);
        setMinWidth(500);

        // initialize canvas
        Canvas canvas = new Canvas();
        canvas.setWidth(480);
        canvas.setHeight(480);
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.LIGHTGREEN);
        graphicsContext.setStroke(Color.BLACK);

        // initialize cells field
        line = column = 10;
        span = 1000;

        gameActive = false;
        gameReflesh(true);
    }

    public void setSidepane(SidePane sidepane) {
        this.sidepane = sidepane;
    }

    public void gameReflesh(boolean force) {
        turn = 0;

        // suppose line:column modifiable
        if(force) {
            field = new Cell[line][column];

            for(int l = 0; l < line; l++) {
                for(int c = 0; c < column; c++) {
                    field[l][c] = new Cell(l, c, 480/line,this);
                }
            }
        }
        for(Cell[] line : field) {
            for(Cell cell : line) {
                cell.reset(force);
            }
        }
        canvasReflesh();

        // terminate game
        if(gThread!=null) gThread.stop();// deprecated, I know.

        gThread = new Thread(new GameTask(this));
        gThread.setDaemon(true);
    }

    public void canvasReflesh() {
        graphicsContext.clearRect(0,0, 480, 480);

        // paint each cell with selected state
        for(Cell[] line : field) {
            for(Cell cell : line) {
                cell.paint();
            }
        }
    }

    public void proceedTurn() {
        turn += 1;
        sidepane.setTurnLabel(turn+"");
    }

    public int checkAroundCell(int x, int y) {
        int score = 0;
        int l = (x==0) ? x : x-1;
        int c = (y==0) ? y : y-1;

        for(; l<l+1 && l<line ; l++) {
            for(;c<c+1 && c<column ; c++) {
                score += 1;
            }
        }

        return score;
    }


    /*
     * interfaces for sidepane
     */

    // gen thread and run
    public void startGame() {
        if (turn==0)    System.out.println("game start");
        else            System.out.println("game restart");

        gameActive = true;
        sidepane.disableButtons();

        if(gThread.getState()== Thread.State.NEW) gThread.start();
    }
    public void stopGame() {
        System.out.println("game stop");

        gameActive = false;

        sidepane.enableButtons();
    }

    // called when game inactive
    public void resetGame() {
        System.out.println("game reset");

        sidepane.setTurnLabel("-");
        gameReflesh(false);
    }

    public void clearGame() {
        System.out.println("game clear");

        gameReflesh(true);
    }

    /*
     * setter / getter
     */
    public int getSpan() {
        return span;
    }
    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }
    public boolean isGameActive() {
        return gameActive;
    }
}
