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
        field = mainpane.field;
    }

    @Override
    protected Object call() throws Exception {
        System.out.println("thread run");
        while(true) { // would change to game aliving condition
            while (mainpane.gameActive) {
                System.out.print("turn start");
                Platform.runLater(() -> mainpane.proceedTurn());

                // game process
                // fps adjusting
                sleep(1000);

                System.out.print("turn end");
            }
        }

        //return null;
    }
}

