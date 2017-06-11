import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

/**
 * Created by 3943 on 2017/06/10.
 */
public class LifeGame extends Application {
    private MainPane mainpane;
    private SidePane sidepane;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // setup stage(window)
        primaryStage.setTitle("LifeGame");
        primaryStage.setWidth(640+20);
        primaryStage.setHeight(480+40+20);
        primaryStage.setResizable(false);

        // setup root scene
        SplitPane root = new SplitPane();
        root.setOrientation(Orientation.HORIZONTAL);
        primaryStage.setScene(new Scene(root, 640, 480));

        // bad circular reference...
        mainpane = new MainPane();
        sidepane = new SidePane();
        mainpane.setSidePane(sidepane);
        sidepane.setMainpane(mainpane);

        // add both panel to root
        root.getItems().add(mainpane);
        root.getItems().add(sidepane);

        primaryStage.show();
    }
}
