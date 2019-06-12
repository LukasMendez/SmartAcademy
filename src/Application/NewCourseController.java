package Application;

import Domain.Location;
import Persistance.DB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Comparator;

public class NewCourseController implements Openable{

    // Static because we want to make sure to always have access to the same (and only) stage
    private static Stage addCourseStage = new Stage();
    private FXMLLoader fxmlLoader;
    private DB db = DB.getInstance();

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    // TextFields
    @FXML
    private TextField amuNoTextField, cvrNoTextField, infoTextField, noOfDaysTextField, additionalInfoTextField;

    // ComboBox
    @FXML
    private ComboBox locationDropDown;

    // Label
    @FXML
    private Label infoLabel;

    public void initialize(){

        ObservableList<Location> locations = db.getLocationList();

        // Will make sure to sort the data in the drop down menu by zip code
        Comparator<Location> comparator = Comparator.comparingInt(Location::getZip);
        locations.sort(comparator);

        locationDropDown.setItems(locations);
    }

    /**
     * Method used for configuring the Window and opening it.
     */
    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCourseWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            addCourseStage.setTitle("Add New Course");
            addCourseStage.getIcons().add(new Image("UI/Images/diploma.png"));
            addCourseStage.setScene(new Scene(root));

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();
            addCourseStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Methods that prevents you from operating other windows before you close the current one.
     */
    @Override
    public void setupModality(){
        if (!isInitialized){
            addCourseStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized=true;
        }
    }

    /**
     * Will check if the stage is showing (open).
     * @return true or false
     */
    @Override
    public boolean isStageOpen(){
        return addCourseStage.isShowing();
    }


    /**
     * Will get you the stage of the instance you are working with.
     * @return a Stage instance.
     */
    @Override
    public Stage getStage() {
        return addCourseStage;
    }

    /**
     * Method used for getting the controller of the FXMLLoader. Since the FXMLLoader creates a new Controller instance, it
     * is important to regain control of the active Controller. The method only
     * returns an Object, but the object can be casted as a different object type (As the controller object needed)
     * @return Object (Controller)
     */
    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    /**
     * Will take the Strings from the text fields and LocationID from the dropdown menu and insert it into the database. If there are any problems with the given
     * arguments it will display an error on a label.
     */
    @FXML
    public void addNewCourse(){

        Location location = (Location) locationDropDown.getSelectionModel().getSelectedItem();

        try{
            db.insertCourse(Integer.parseInt(amuNoTextField.getText()),infoTextField.getText(),additionalInfoTextField.getText(),Integer.parseInt(noOfDaysTextField.getText()), location.getLocationID(),cvrNoTextField.getText());
            infoLabel.setVisible(true);
            infoLabel.setText("Data saved successfully!");

        }catch (NumberFormatException e){

            infoLabel.setVisible(true);
            infoLabel.setText("Error! Invalid input! Please try again");
        }



    }

}
