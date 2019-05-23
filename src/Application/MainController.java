package Application;

import Domain.Course;
import Persistance.DB;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    // Controllers (Windows)
    private NewCourseController newCourseController = new NewCourseController();
    private DatesToCourseController datesToCourseController = new DatesToCourseController();
    private NewEmployeeController newEmployeeController = new NewEmployeeController();

    // Tab indexes
    private int coursesIndex = 0;
    private int educationMatrixIndex = 1;
    private int employeeIndex = 2;
    private int calenderIndex = 3;
    private int companiesIndex = 4;
    private int providerIndex = 5;





    private DB db = DB.getInstance();
    private CourseToEPController courseToEPController = new CourseToEPController();

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView courseTableView, educationMatrixTableView, employeeTableView, companiesTableView, providerTableView;

    @FXML
    private TableColumn courseNumberColumn, informationColumn, additionalInformationColumn, numberOfDaysColumn, locationIDColumn, CVRNumberColumn;



    private ObservableList<Course> courseList;
    private ObservableList<Course> companyList;


    public void initialize() {


        for (int i = 0; i < 10; i++) {
            educationMatrixTableView.getColumns().addAll(new TableColumn("Test no." + i));
        }

        //Displaying Courses
        //constructing data model
        courseList = DB.testGetCourseList();
        //data binding
        courseTableView.setItems(courseList);
        //splitting out the data in the model
        courseNumberColumn.setCellValueFactory(new PropertyValueFactory("courseNumber"));
        informationColumn.setCellValueFactory(new PropertyValueFactory("information"));
        additionalInformationColumn.setCellValueFactory(new PropertyValueFactory("additionalInformation"));
        numberOfDaysColumn.setCellValueFactory(new PropertyValueFactory("numberOfDays"));
        locationIDColumn.setCellValueFactory(new PropertyValueFactory("locationID"));
        CVRNumberColumn.setCellValueFactory(new PropertyValueFactory("CVRNumber"));
        //representing the data in the columns
        courseTableView.getColumns().setAll(courseNumberColumn, informationColumn, additionalInformationColumn, numberOfDaysColumn, locationIDColumn, CVRNumberColumn);

        //Display Companies
        //constructing data model
        //companyList = DB.testGetCourseList();
        //data binding
        //companyTable.setItems(companyList);
        //splitting out the data in the model
        courseNumberColumn.setCellValueFactory(new PropertyValueFactory("courseNumber"));
        informationColumn.setCellValueFactory(new PropertyValueFactory("information"));
        additionalInformationColumn.setCellValueFactory(new PropertyValueFactory("additionalInformation"));
        numberOfDaysColumn.setCellValueFactory(new PropertyValueFactory("numberOfDays"));
        locationIDColumn.setCellValueFactory(new PropertyValueFactory("locationID"));
        CVRNumberColumn.setCellValueFactory(new PropertyValueFactory("CVRNumber"));
        //representing the data in the columns
        courseTableView.getColumns().setAll(courseNumberColumn, informationColumn, additionalInformationColumn, numberOfDaysColumn, locationIDColumn, CVRNumberColumn);
    }

    //Test method
    @FXML
    public void testAddCourse() {
        courseList.add(new Course(156, "bloop", "bleep", 8, 1, "87564321"));
    }


    //  private Stage addCourseStage = new Stage();


    //---------------------------- OPEN NEW WINDOWS---------------------------------//


    // BUTTON EVENT HANDLERS


    @FXML
    public void leftBottomButtonAction() {


        // TODO IMPLEMENT METHODS THAT HANDLE THE BUTTONS ACTION BASED ON WHAT TAB YOU ARE LOOKING AT

    }

    @FXML
    public void middleBottomButtonAction() {

        // TODO IMPLEMENT METHODS THAT HANDLE THE BUTTONS ACTION BASED ON WHAT TAB YOU ARE LOOKING AT

    }


    @FXML
    public void rightBottomButtonAction() {

        if (tabPane.getSelectionModel().getSelectedIndex()==coursesIndex){

            openAddCourseWindow();

        } else if (tabPane.getSelectionModel().getSelectedIndex()==employeeIndex){

            openNewEmployeeWindow();

        } else if (tabPane.getSelectionModel().getSelectedIndex()==companiesIndex){

            // TODO SOMETHING MUST HAPPEN

        } else if (tabPane.getSelectionModel().getSelectedIndex()==providerIndex){

            // TODO SOMETHING MUST HAPPEN

        }


        // TODO IMPLEMENT METHODS THAT HANDLE THE BUTTONS ACTION BASED ON WHAT TAB YOU ARE LOOKING AT

    }


    // FUNCTIONS


    public void openAddCourseWindow() {


        if (!newCourseController.isStageOpen()) {

            newCourseController.openWindow();

        } else {

            System.out.println("Window already open");
        }

    }

    @FXML
    public void openAddDatesToCourseWindow() {

        if (!datesToCourseController.isStageOpen()) {

            datesToCourseController.openWindow();

        } else {

            System.out.println("Window already open");
        }


    }

    @FXML
    public void openNewEmployeeWindow() {

        if (!newEmployeeController.isStageOpen()) {

            newEmployeeController.openWindow();
        } else {

            System.out.println("Window is already open");
        }

    }


}
