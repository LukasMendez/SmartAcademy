package Application;

import Domain.Course;
import Persistance.DB;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    // Controllers (Windows)
    private NewCourseController newCourseController = new NewCourseController();
    private DatesToCourseController datesToCourseController = new DatesToCourseController();
    private NewEmployeeController newEmployeeController = new NewEmployeeController();
    private ManageEmployeeController manageEmployeeController = new ManageEmployeeController();


    // Tab indexes
    private int coursesIndex = 0;
    private int educationMatrixIndex = 1;
    private int employeeIndex = 2;
    private int calenderIndex = 3;
    private int companiesIndex = 4;
    private int providerIndex = 5;

    // Buttons

    @FXML
    private Button buttonLeft, buttonMiddle, buttonRight;



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


        mouseClickEmployeeHandler();
        tabHandler();


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

        if (tabPane.getSelectionModel().getSelectedIndex() == coursesIndex) {

            openAddCourseWindow();

        } else if (tabPane.getSelectionModel().getSelectedIndex() == employeeIndex) {

            openNewEmployeeWindow();

        } else if (tabPane.getSelectionModel().getSelectedIndex() == companiesIndex) {

            // TODO SOMETHING MUST HAPPEN

            // TODO HAVEN'T EVEN MADE AN FXML FILE FOR THIS SCENARIO :I

        } else if (tabPane.getSelectionModel().getSelectedIndex() == providerIndex) {

            // TODO SOMETHING MUST HAPPEN

            // TODO HAVEN'T MADE AN FXML FILE FOR THIS SCENARIO EITHER :/

        }

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

        /// TODO CHECK IF YOU SELECTED A RECORD ON THE TABLE FIRST


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


    //---------------------Event handlers--------------------------//

    private void mouseClickEmployeeHandler(){

        employeeTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {

                        if (!manageEmployeeController.isStageOpen()){


                            // TODO GET SELECTED ELEMENT AND OPEN WINDOW WITH THE EMPLOYEE

                            manageEmployeeController.openWindow();
                        } else {

                            System.out.println("Please close the first window before opening a new one");
                        }
                    }
                }
            }
        });

    }


    private void tabHandler(){

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {

            System.out.println("The tab selected now has the index: "+ newTab.getTabPane().getSelectionModel().getSelectedIndex());

            if (newTab.getTabPane().getSelectionModel().getSelectedIndex()==coursesIndex){

                buttonLeft.setText("Delete Course");
                buttonLeft.setVisible(true);

                buttonMiddle.setText("Add dates to selected course");
                buttonMiddle.setVisible(true);

                buttonRight.setText("Add New Course");
                buttonRight.setVisible(true);


            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex()==educationMatrixIndex){

                buttonLeft.setVisible(false);
                buttonMiddle.setVisible(false);
                buttonRight.setVisible(false);

            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex()==employeeIndex){

                buttonLeft.setText("Delete Employee");
                buttonLeft.setVisible(true);

                buttonMiddle.setVisible(false);

                buttonRight.setText("Add New Employee");
                buttonRight.setVisible(true);

            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex()==calenderIndex){

                buttonLeft.setVisible(false);
                buttonMiddle.setVisible(false);
                buttonRight.setVisible(false);

            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex()==companiesIndex){

                buttonLeft.setText("Delete Company");
                buttonLeft.setVisible(true);

                buttonMiddle.setVisible(false);

                buttonRight.setText("Add New Company");
                buttonRight.setVisible(true);

                // TODO If this turns out to be the company that you are currently working on, it should handle the situation without issues

            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex()==providerIndex){


                buttonLeft.setText("Delete Provider");
                buttonLeft.setVisible(true);

                buttonMiddle.setVisible(false);

                buttonRight.setText("Add New Provider");
                buttonRight.setVisible(true);

            }

            // TODO REFACTOR CODE

        });





    }


}
