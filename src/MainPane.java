import javafx.concurrent.Worker;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

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

public class MainPane extends AnchorPane{
    private SidePane sidepane;
    private GameTask game;
    private Thread gThread;
    private Canvas canvas;
    public Cell[][] field; // entity
    private int line, column;

    public boolean gameActive;
    public int turn;

    MainPane() {
        // bind (searching bitter way
        setMaxWidth(480);
        setMinWidth(480);

        // initialize canvas
        canvas = new Canvas();
        canvas.setWidth(480);
        canvas.setHeight(480);
        getChildren().add(canvas);

        // initialize cells field
        line = column = 10;
        field = new Cell[line][column];

        for(int l = 0; l < line; l++) {
            for(int c = 0; c < column; c++) {
                field[l][c] = new Cell(l, c, canvas);
            }
        }

        gameActive = false;
        gameReflesh(true);
    }

    public void setSidepane(SidePane sidepane) {
        this.sidepane = sidepane;
    }

    public void gameReflesh(boolean force) {
        turn = 0;
        for(Cell[] line : field) {
            for(Cell cell : line) {
                cell.reset(force);
            }
        }
        // terminate game
        if(gThread!=null) gThread.stop();// deprecated, I know.

        game = new GameTask(this);
        gThread = new Thread(game);
        gThread.setDaemon(true);
    }

    public void proceedTurn() {
        turn += 1;
        sidepane.setTurnLabel(turn+"");
    }

    public void mainRender() {

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

}
