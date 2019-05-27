package Application;

import Domain.Location;
import Persistance.DB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class NewCourseController implements Openable{

    // Static because we want to make sure to always have access to the same (and only) stage
    private static Stage addCourseStage = new Stage();
    private FXMLLoader fxmlLoader;

    // TextFields
    @FXML
    private TextField amuNoTextField, cvrNoTextField, infoTextField, noOfDaysTextField, additionalInfoTextField;

    // ComboBox
    @FXML
    private ComboBox locationDropDown;

    public void initialize(){

        ObservableList<Location> locations = DB.getLocationList();

        // Will make sure to sort the data in the drop down menu by zip code
        Comparator<Location> comparator = Comparator.comparingInt(Location::getZip);
        locations.sort(comparator);

        locationDropDown.setItems(locations);
    }


    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCourseWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            addCourseStage.setTitle("ABC");
            addCourseStage.setScene(new Scene(root));
            addCourseStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean isStageOpen(){
        return addCourseStage.isShowing();
    }

    @Override
    public Stage getStage() {
        return addCourseStage;
    }

    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    @FXML
    public void addNewCourse(){

        Location location = (Location) locationDropDown.getSelectionModel().getSelectedItem();

        DB.insertCourse(Integer.parseInt(amuNoTextField.getText()),infoTextField.getText(),additionalInfoTextField.getText(),Integer.parseInt(noOfDaysTextField.getText()), location.getLocationID(),cvrNoTextField.getText());


        // TODO MAYBE SOME INPUT VALIDATION


    }

}
