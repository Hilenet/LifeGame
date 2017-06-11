import javafx.scene.canvas.GraphicsContext;

/**
 * Created by 3943 on 2017/06/10.
 */


public class Cell {
    private MainPane mainpane;
    private GraphicsContext graphicsContext;
    private int x, y, side;
    private boolean selected; // used in selecting stage, be keep while game
    private boolean alive; // used in evaluating stage
    private boolean next;  // use as tmp state bitween eval and paint

    Cell(int x, int y, int side, MainPane mainpane) {
        this.x = x;
        this.y = y;
        this.side = side;
        selected = false;
        alive = false;
        next = false;

        this.mainpane = mainpane; // should limit as canvas
        graphicsContext = mainpane.getGraphicsContext();
    }

    // eval: bool next turn one alive
    void eval() {
        int score = mainpane.checkAroundCell(x,y);

        if((alive && 1<score && score<4) || (!alive && score==3)) next = true;
        else next = false;
    }

    // update one's alive state, and paint
    void paint(boolean gameActive) {
        boolean flag = gameActive ? alive=next : selected;

        if(flag)    graphicsContext.fillRect(x*side, y*side, side, side);
        else        graphicsContext.clearRect(x*side, y*side, side, side);
        graphicsContext.strokeRect(x*side, y*side, side, side);
    }

    // in selecting stage: change 'selected' state, and repaint oneself
    void change() {
        selected = !selected;
        alive = selected;
        paint(false);
    }

    // reset one's state as: force to false all, else selected
    void reset(boolean force) {
        if(force)   alive = next = false;
        else        alive = selected;
    }

    boolean isAlive() {
        return alive;
    }

}
