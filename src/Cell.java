import javafx.scene.canvas.GraphicsContext;

/**
 * Created by 3943 on 2017/06/10.
 */


public class Cell {
    private MainPane mainpane;
    private GraphicsContext graphicsContext;
    private int x, y, side;
    private boolean selected; // when started
    private boolean alive;

    Cell(int x, int y, int side, MainPane mainpane) {
        this.x = x;
        this.y = y;
        this.side = side;
        selected = false;
        alive = false;

        this.mainpane = mainpane; // should limit as canvas
        graphicsContext = mainpane.getGraphicsContext();
    }

    void eval() {
        int score = mainpane.checkAroundCell(x,y);
        alive = (1<score && score<4);
    }

    void paint() {
        if(selected)    graphicsContext.fillRect(x*side, y*side, side, side);
        else            graphicsContext.strokeRect(x*side, y*side, side, side);

        System.out.print(x+""+y);
    }

    void change() {
        selected = !selected;
    }

    void reset(boolean force) {
        if(force)   alive = false;
        else        alive = selected;
    }

}
