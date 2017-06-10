import javafx.scene.canvas.Canvas;

/**
 * Created by 3943 on 2017/06/10.
 */


public class Cell {
    private Canvas canvas;
    private int x, y;
    private boolean selected;
    private boolean alive;

    Cell(int x, int y, Canvas canvas) {
        this.x = x;
        this.y = y;
        selected = false;
        alive = false;

        this.canvas = canvas;
    }

    void reset(boolean force) {
        if(force)   alive = false;
        else        alive = selected;
    }

}
