import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import static java.lang.System.exit;


/**
 * Created by 3943 on 2017/06/10.
 */
/*
 * as: FXML root, FXML controller
 * role as side controll bar
 */

public class SidePane extends AnchorPane{
    private MainPane mainpane;
    @FXML Button reset_button;
    @FXML Button clear_button;
    @FXML private Label turn_label;

    SidePane() {
        // load fxml and set controller this
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("side_pane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        } catch(IOException e) {
            // loaderのエラー時ってどうすれば・・・
            System.err.println(e);
            exit(1);
        }

        turn_label.setText("-");
    }

    public void setMainpane(MainPane mainpane) {
        this.mainpane = mainpane;
    }

    // when game started
    public void disableButtons() {
        reset_button.setDisable(true);
        clear_button.setDisable(true);
    }

    // when game stopped
    public void enableButtons() {
        reset_button.setDisable(false);
        clear_button.setDisable(false);
    }

    // be called from Game
    public void setTurnLabel(String text) {
        turn_label.textProperty().setValue(text);
    }

    @FXML
    protected void runButtonHandler() {
        if(!mainpane.gameActive) {
            mainpane.startGame();
        }
    }
    @FXML
    protected void stopButtonHandler() {
        if(mainpane.gameActive) {
            mainpane.stopGame();
        }
    }
    @FXML
    protected void resetButtonHandler() {
        mainpane.resetGame();
    }
    @FXML
    protected void clearButtonHandler() {
        mainpane.clearGame();
    }



}
