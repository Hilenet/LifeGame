import javafx.application.Platform;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

/**
 * Created by 3943 on 2017/06/10.
 */

/*
 * Game integration thread
 * use resources on mainpane
 */
public class GameTask extends Task {

    private MainPane mainpane;
    private Cell[][] field;

    GameTask(MainPane mainpane) {
        this.mainpane = mainpane;
        field = mainpane.getField();
    }

    @Override
    protected Object call() throws Exception {
        while(true) { // would change to game aliving condition
            while (mainpane.isGameActive()) {
                Platform.runLater(() -> mainpane.proceedTurn());

                // game process
                evalCell();
                Platform.runLater(() -> mainpane.flash(true));

                // fps adjusting
                sleep(mainpane.getSpan());
            }
        }

        //return null;
    }

    // eval each cell
    private void evalCell() {
        for(Cell[] line : field) {
            for(Cell cell : line) {
                cell.eval();
            }
        }
    }
}

