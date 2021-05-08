package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private ImageView btnCloseSettings;

    @FXML
    private CheckBox checkBoxStream;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            if (UtilStreamOrRest.isStream){
                checkBoxStream.setSelected(true);
            } else {
                checkBoxStream.setSelected(false);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void closeSettings(MouseEvent event) {
        try {
            closeStage();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void recordStream(ActionEvent event) {
        try {
            if (checkBoxStream.isSelected()){
                UtilStreamOrRest.isStream = true;
            } else {
                UtilStreamOrRest.isStream = false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void closeStage() {
        ((Stage) checkBoxStream.getScene().getWindow()).close();
    }
}
