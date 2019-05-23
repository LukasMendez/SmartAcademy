package Application;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class DatesToCourseController implements Openable {

    private Stage addDatesToCourseStage = new Stage();

    @FXML
    private VBox calenderVBox;

    public void initialize() {

        // Will create a mini calender
        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();
        calenderVBox.getChildren().addAll(popupContent);

    }


    @Override
    public void openWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\DatesToCourseWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            addDatesToCourseStage.setTitle("Add dates to selected source");
            addDatesToCourseStage.setScene(new Scene(root));
            addDatesToCourseStage.setResizable(false);
            addDatesToCourseStage.show();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public boolean isStageOpen() {
        return addDatesToCourseStage.isShowing();
    }
}
