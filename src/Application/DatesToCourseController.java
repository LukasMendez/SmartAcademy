package Application;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class DatesToCourseController {

    @FXML
    private VBox calenderVBox;

    public void initialize() {


        // Will create a mini calender
        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();

        calenderVBox.getChildren().addAll(popupContent);


    }


}
