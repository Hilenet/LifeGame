import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import static java.lang.Math.min;

/**
 * Created by 3943 on 2017/06/10.
 */

/*
 * as: fxml root pane, fxml controller
 * has: canvas, game data
 *
 * will bahave as game board
 * 'game' class thread roles integration on this instance
 */

public class MainPane extends BorderPane{
    private SidePane sidepane;
    private Thread gThread;
    private GraphicsContext graphicsContext;
    private Cell[][] field; // entity

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
        canvas.setOnMouseClicked((event)->canvasClicked(event));
        setCenter(canvas);
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.LIGHTGREEN);
        graphicsContext.setStroke(Color.BLACK);

        // initialize cells field
        line = column = 10;
        span = 1000;

        gameActive = false;
        gameCleanup(true);

    }

    public void setSidePane(SidePane sidepane) {
        this.sidepane = sidepane;
    }

    /*
     * reload, and repaint cell data
     * if force, regenerate cell field
     */
    private void reloadCellField(boolean force) {
        // suppose line:column modifiable
        if(force) {
            field = new Cell[column][line];
            for(int c = 0; c < column; c++) {
                for(int l = 0; l < line; l++) {
                    field[c][l] = new Cell(c, l, 480/line,this);
                }
            }
        }
        for(Cell[] line : field) {
            for(Cell cell : line) {
                cell.reset(force);
            }
        }
        flash(false);
    }

    /*
     * reflesh cell, thread, canvas when game fin
     */
    private void gameCleanup(boolean force) {
        turn = 0;

        reloadCellField(force);

        // terminate game
        if(gThread!=null) gThread.stop();// deprecated, I know.

        gThread = new Thread(new GameTask(this));
        gThread.setDaemon(true);
    }

    /* repaint all cell
     * if gameActive, check with "alive"
     */
    public void flash(boolean gameActive) {
        graphicsContext.clearRect(0,0, 480, 480);
        for(Cell[] line : field) {
            for(Cell cell : line) {
                cell.paint(gameActive);
            }
        }
    }
    public void proceedTurn() {
        turn += 1;
        sidepane.setTurnLabel(turn+"");
    }


    // ...maybe, this will be quickly as if write each condition
    public int checkAroundCell(int x, int y) {
        int score = 0;
        int c_pre = ((x==0) ? x : x-1);
        int l_pre = ((y==0) ? y : y-1);

        for(int c = c_pre; c<=min(x+1, line-1) ; c++) {
            for(int l = l_pre; l<=min(y+1, column-1) ; l++) {
                if(field[c][l].isAlive()) {
                    score += 1;
                }
            }
        }
        if(field[x][y].isAlive()) score-=1;

        return score;
    }

    /*
     * canvas listener
     */
    void canvasClicked(MouseEvent event) {
        int c = (int)(event.getX()*column/480);
        int l = (int)(event.getY()*line/480);

        if(!gameActive) field[c][l].change();
    }


    /*
     * interfaces for sidepane
     */

    // gen thread and run
    public void startGame() {
        gameActive = true;

        if (turn==0) {
            gThread.start();
            reloadCellField(false);

            System.out.println("game start");
        }
        else
            System.out.println("game restart");

        sidepane.disableButtons();
    }
    public void stopGame() {
        gameActive = false;

        System.out.println("game stop");

        sidepane.enableButtons();
    }

    // called when game inactive
    public void resetGame() {
        System.out.println("game reset");

        sidepane.setTurnLabel("-");
        gameCleanup(false);
    }
    public void clearGame() {
        System.out.println("game clear");

        sidepane.setTurnLabel("-");
        gameCleanup(true);
    }

    /*
     * setter / getter
     */
    public int getSpan() {
        return span;
    }
    public Cell[][] getField() { return field; }
    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }
    public boolean isGameActive() {
        return gameActive;
    }
}
