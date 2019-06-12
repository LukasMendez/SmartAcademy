package Application;

import Domain.Course;
import Domain.Date;
import Persistance.DB;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatesToCourseController implements Openable {

    private static Stage addDatesToCourseStage = new Stage();
    private FXMLLoader fxmlLoader;
    private DB db = DB.getInstance();

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

    /**
     * Method used for configuring the Window and opening it.
     */
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

    /**
     * Methods that prevents you from operating other windows before you close the current one.
     */
    @Override
    public void setupModality() {
        if (!isInitialized) {
            addDatesToCourseStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized = true;
        }
    }

    /**
     * Will check if the stage is showing (open).
     *
     * @return true or false
     */
    @Override
    public boolean isStageOpen() {
        return addDatesToCourseStage.isShowing();
    }

    /**
     * Will get you the stage of the instance you are working with.
     *
     * @return a Stage instance.
     */
    @Override
    public Stage getStage() {
        return addDatesToCourseStage;
    }

    /**
     * Method used for getting the controller of the FXMLLoader. Since the FXMLLoader creates a new Controller instance, it
     * is important to regain control of the active Controller. The method only
     * returns an Object, but the object can be casted as a different object type (As the controller object needed)
     *
     * @return Object (Controller)
     */
    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    /**
     * In order to add dates to a course, we have to know what course we are talking about. This method is called from the
     * Controller that holds the information regarding the course (MainController). It is used to pass the Course object
     * from one Controller to another.
     *
     * @param course Course object containing the needed information.
     */
    public void setSelectedCourse(Course course) {
        // Will set the course
        selectedCourse = course;
        // and update the drop down list with the corresponding period id's
        periodDropDown.setItems(db.getPeriodList(selectedCourse.getCourseID()));
        System.out.println(course.getCourseID() + " and " + course.getInformation() + " and " + course.getCourseNumber());
        // Will display course name in header
        headerLabel.setText("Add dates to AMU number: " + course.getCourseNumber());
    }

    /**
     * This method will generate a new PeriodID and add it to the selected course. As soon as the new Period is added,
     * the method will refresh the ComboBox, so that the user can select the new Period from the drop down menu.
     */
    @FXML
    public void addNewPeriod() {
        // Will create a new periodID
        db.insertPeriod(selectedCourse.getCourseID());
        // Will update the drop down menu with the latest data from the database
        periodDropDown.setItems(db.getPeriodList(selectedCourse.getCourseID()));
    }

    /**
     * Extracts the Data object from the ListView and deletes it from the database. The ListView is then refreshed.
     */
    @FXML
    public void deleteSelectedDate() {
        Date selectedDate = (Date) dateListView.getSelectionModel().getSelectedItem();
        System.out.println("User wants to delete dateID: " + selectedDate.getDateID());
        db.deleteDate(selectedDate.getDateID());
        updateListView(selectedPeriodID);
    }

    /**
     * This method has the same purpose as when you close the Window in the upper right corner. The reason why it's here
     * is that the window has a confirm button that should close the window when being pressed.
     */
    @FXML
    public void closeWindow() {
        System.out.println("Closing stage");
        addDatesToCourseStage.close();
    }

    /**
     * Updates the ListView based on the periodID
     *
     * @param selectedPeriodID the one that you hand in
     */
    public void updateListView(int selectedPeriodID) {
        dateListView.setItems(db.getDatesList(selectedPeriodID));
    }

    /**
     * Method used for initializing an EventHandler. The purpose of the EventHandler is to get the selected date on the
     * DatePicker node whenever the user double-clicks on a date from the calender. The EventHandler then converts the date
     * into a String in the correct format needed for inserting it into the database. After the SQL insertion the ListView is refreshed.
     * */
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
                        if (periodDropDown.getSelectionModel().getSelectedItem() != null) {
                            int selectedPeriodID = (int) periodDropDown.getSelectionModel().getSelectedItem();
                            System.out.println("The value selected was: " + datePicker.getValue());
                            db.insertDate(newDate, selectedPeriodID);
                            updateListView(selectedPeriodID);
                        } else {
                            System.out.println("You need to select a periodID first");
                        }
                    }
                }
            }
        });
    }

    /**
     * Method used for initializing a Listener that observes actions performed on the periodDropDown (ComboBox). Practically it
     * updates the ListView based on the PeriodID selected.
     */
    public void selectedPeriodHandler() {
        periodDropDown.valueProperty().addListener((obs, oldItem, newItem) -> {

            if (periodDropDown.getSelectionModel().getSelectedItem() != null) {
                selectedPeriodID = (int) periodDropDown.getSelectionModel().getSelectedItem();
                System.out.println("You just casted the object as an int, and the output is: " + selectedPeriodID);
                updateListView(selectedPeriodID);
            }
        });
    }
}

