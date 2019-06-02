package Application;

import Domain.Course;
import Domain.Date;
import Persistance.DB;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class DatesToCourseController implements Openable {

    private static Stage addDatesToCourseStage = new Stage();
    private FXMLLoader fxmlLoader;

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private Node popupContent;
    private DatePickerSkin datePickerSkin;

    // Will update itself every time the user selects a new periodID (Due to the listener further down)
    private int selectedPeriodID;


    // Labels
    @FXML
    private Label headerLabel;

    // The course which is sent from the Main Controller
    private Course selectedCourse;

    @FXML
    private VBox calenderVBox;


    // ListView
    @FXML
    private ListView dateListView;

    // Drop Drop/ComboBox
    @FXML
    private ComboBox periodDropDown;


    public void initialize() {


        // Will create a mini calender
        datePickerSkin = new DatePickerSkin(datePicker);
        popupContent = datePickerSkin.getPopupContent();
        calenderVBox.getChildren().addAll(popupContent);

        datePickerHandler();

        selectedPeriodHandler();

    }


    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\DatesToCourseWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            addDatesToCourseStage.setTitle("Add dates to selected source");
            addDatesToCourseStage.setScene(new Scene(root));
            addDatesToCourseStage.setResizable(false);
            addDatesToCourseStage.getIcons().add(new Image("UI/Images/calendar.png"));

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            addDatesToCourseStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupModality(){
        if (!isInitialized){
            addDatesToCourseStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized=true;
        }
    }

    @Override
    public boolean isStageOpen() {
        return addDatesToCourseStage.isShowing();
    }

    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    @Override
    public Stage getStage() {
        return addDatesToCourseStage;
    }


    public void setSelectedCourse(Course course){

        // Will set the course
        selectedCourse=course;
        // and update the drop down list with the corresponding period id's
        periodDropDown.setItems(DB.getPeriodList(selectedCourse.getCourseID()));


        System.out.println(course.getCourseID() + " and " + course.getInformation() + " and " + course.getCourseNumber());

        // Will display course name in header
        headerLabel.setText("Add dates to AMU number: " + course.getCourseNumber());


    }




    @FXML
    public void addNewPeriod(){

        // Will create a new periodID
        DB.insertPeriod(selectedCourse.getCourseID());
        // Will update the drop down menu with the latest data from the database
        periodDropDown.setItems(DB.getPeriodList(selectedCourse.getCourseID()));


    }

    @FXML
    public void deleteSelectedDate(){

        Date selectedDate = (Date) dateListView.getSelectionModel().getSelectedItem();

        System.out.println("User wants to delete dateID: " + selectedDate.getDateID());

        DB.deleteDate(selectedDate.getDateID());

        updateListView(selectedPeriodID);

    }

    @FXML
    public void closeWindow(){

        System.out.println("Closing stage");

        addDatesToCourseStage.close();

    }


    /**
     *
     * Updates the ListView based on the periodID
     * @param selectedPeriodID the one that you hand in
     */

    public void updateListView(int selectedPeriodID){


        dateListView.setItems(DB.getDatesList(selectedPeriodID));


    }



    public void datePickerHandler() {

        popupContent.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {


                        // Used for converting the date into a String
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        // The date but in the datatype String
                        String newDate = formatter.format(datePicker.getValue());

                        // Will only add a date if a periodID is selected
                        if (periodDropDown.getSelectionModel().getSelectedItem()!=null){

                            int selectedPeriodID = (int) periodDropDown.getSelectionModel().getSelectedItem();

                            System.out.println("The value selected was: " + datePicker.getValue());


                            DB.insertDate(newDate, selectedPeriodID);

                            updateListView(selectedPeriodID);

                        } else {

                            System.out.println("You need to select a periodID first");
                        }


                    }


                }

            }
        });

    }


    public void selectedPeriodHandler(){

        periodDropDown.valueProperty().addListener((obs, oldItem, newItem) -> {

            // System.out.println("User clicked on: " + newItem);

            if (periodDropDown.getSelectionModel().getSelectedItem()!=null){

                selectedPeriodID = (int) periodDropDown.getSelectionModel().getSelectedItem();

                System.out.println("You just casted the object as an int, and the output is: " + selectedPeriodID);

                updateListView(selectedPeriodID);

            }


        });

    }

}

