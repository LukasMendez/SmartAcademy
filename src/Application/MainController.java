package Application;

import Domain.Company;
import Domain.Course;
import Domain.Employee;
import Domain.Provider;
import Persistance.DB;

import javafx.application.Platform;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainController {

    // Controllers (Windows)
    @FXML
    private NewCourseController newCourseController = new NewCourseController();
    @FXML
    private DatesToCourseController datesToCourseController = new DatesToCourseController();
    @FXML
    private NewEmployeeController newEmployeeController = new NewEmployeeController();
    @FXML
    private ManageEmployeeController manageEmployeeController = new ManageEmployeeController();
    @FXML
    private NewCompanyController newCompanyController = new NewCompanyController();
    @FXML
    private NewProviderController newProviderController = new NewProviderController();

    // Tab indexes

    private int educationMatrixIndex = 0;
    private int employeeIndex = 1;
    private int calenderIndex = 2;
    private int coursesIndex = 3;
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
    private TableView courseTableView, educationMatrixTableView, employeeTableView, companyTableView, providerTableView;

    @FXML
    private TableColumn courseNumberColumn, informationColumn, additionalInformationColumn, numberOfDaysColumn, //Courses
            locationIDColumn, CVRNumberColumn, //Courses
            employeeNameColumn, employeeCPRColumn, employeeEmailColumn, employeePhoneColumn, employeeCompanyColumn, //Employees
            companyNameColumn, companyAddressColumn, companyZipColumn, companyEmailColumn, companyPhoneColumn, companyCVRColumn, //Companies
            providerNameColumn, providerAddressColumn, providerZipColumn, providerEmailColumn, providerPhoneColumn, providerCVRColumn; //Providers

    private ObservableList<Course> courseList;
    private ObservableList<Employee> employeeList;
    private ObservableList<Company> companyList;
    private ObservableList<Provider> providerList;

    public void initialize() {

        mouseClickEmployeeHandler();
        tabHandler();

        for (int i = 0; i < 10; i++) {
            educationMatrixTableView.getColumns().addAll(new TableColumn("Test no." + i));
        }

        //Courses
        //constructing data model + data binding
        updateCourseTableView();
        //splitting out the data in the model
        courseNumberColumn.setCellValueFactory(new PropertyValueFactory("courseNumber"));
        informationColumn.setCellValueFactory(new PropertyValueFactory("information"));
        additionalInformationColumn.setCellValueFactory(new PropertyValueFactory("additionalInformation"));
        numberOfDaysColumn.setCellValueFactory(new PropertyValueFactory("numberOfDays"));
        locationIDColumn.setCellValueFactory(new PropertyValueFactory("location"));
        CVRNumberColumn.setCellValueFactory(new PropertyValueFactory("CVRNumber"));
        //representing the data in the columns
        courseTableView.getColumns().setAll(courseNumberColumn, informationColumn, additionalInformationColumn, numberOfDaysColumn, locationIDColumn, CVRNumberColumn);

        //Employees
        //constructing data model + data binding
        updateEmployeeTableView();
        //splitting out the data in the model
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        employeeCPRColumn.setCellValueFactory(new PropertyValueFactory("CPRNumber"));
        employeeEmailColumn.setCellValueFactory(new PropertyValueFactory("email"));
        employeePhoneColumn.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        employeeCompanyColumn.setCellValueFactory(new PropertyValueFactory("company"));
        //representing the data in the columns
        employeeTableView.getColumns().setAll(employeeNameColumn, employeeCPRColumn, employeeEmailColumn, employeePhoneColumn, employeeCompanyColumn);

        //Companies
        //constructing data model + data binding
        updateCompanyTableView();
        //splitting out the data in the model
        companyNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        companyAddressColumn.setCellValueFactory(new PropertyValueFactory("address"));
        companyZipColumn.setCellValueFactory(new PropertyValueFactory("zip"));
        companyEmailColumn.setCellValueFactory(new PropertyValueFactory("email"));
        companyPhoneColumn.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        companyCVRColumn.setCellValueFactory(new PropertyValueFactory("CVRNumber"));
        //representing the data in the columns
        companyTableView.getColumns().setAll(companyNameColumn, companyAddressColumn, companyZipColumn, companyEmailColumn, companyPhoneColumn, companyCVRColumn);

        //Providers
        //constructing data model + data binding
        updateProviderTableView();
        //splitting out the data in the model
        providerNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        providerAddressColumn.setCellValueFactory(new PropertyValueFactory("address"));
        providerZipColumn.setCellValueFactory(new PropertyValueFactory("zip"));
        providerEmailColumn.setCellValueFactory(new PropertyValueFactory("email"));
        providerPhoneColumn.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        providerCVRColumn.setCellValueFactory(new PropertyValueFactory("CVRNumber"));
        //representing the data in the columns
        providerTableView.getColumns().setAll(providerNameColumn, providerAddressColumn, providerZipColumn, providerEmailColumn, providerPhoneColumn, providerCVRColumn);
    }

    //Test method
    @FXML
    public void testAddCourse() {
        courseList.add(new Course(1,"156", "bloop", "bleep", 8, "locName", "providerName"));
    }

    //---------------------------- OPEN NEW WINDOWS---------------------------------//

    // BUTTON EVENT HANDLERS

    @FXML
    public void leftBottomButtonAction() {

        if (tabPane.getSelectionModel().getSelectedIndex()== coursesIndex){

            deleteSelectedCourse();

        }


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
            openNewCompanyWindow();
        } else if (tabPane.getSelectionModel().getSelectedIndex() == providerIndex) {
            openNewProviderWindow();
        }
    }

    // FUNCTIONS

    public void openAddCourseWindow() {
        if (!newCourseController.isStageOpen()) {
            newCourseController.openWindow();
            newCourseController = (NewCourseController) newCourseController.getController();
            closeStageHandler(newCourseController.getStage(), newCourseController);


        } else {
            System.out.println("Window already open");
        }
    }

    @FXML
    public void openAddDatesToCourseWindow() {

        if (!datesToCourseController.isStageOpen() && courseTableView.getSelectionModel().getSelectedItem() != null) {
            datesToCourseController.openWindow();
            datesToCourseController = (DatesToCourseController) datesToCourseController.getController();

            // Will cast the object as Course
            Course course = (Course) courseTableView.getSelectionModel().getSelectedItem();

            // Will give the Course object to the controller so that it can display the corresponding periodID's
            datesToCourseController.setSelectedCourse(course);

        } else {
            System.out.println("Window already open or nothing is selected");
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

    @FXML
    public void openNewCompanyWindow() {
        if (!newCompanyController.isStageOpen()) {
            newCompanyController.openWindow();
        } else {
            System.out.println("Window is already open");
        }
    }

    @FXML
    public void openNewProviderWindow() {
        if (!newProviderController.isStageOpen()) {
            newProviderController.openWindow();
        } else {
            System.out.println("Window is already open");
        }
    }


    /**
     * This method will configure everything needed for the ManageEmployeeWindow
     */
    private void configureManageEmployee() {

        // Will get the employee object that the user selects from the table view
        Employee employee = (Employee) employeeTableView.getSelectionModel().getSelectedItem();
        // 1) Open the window
        manageEmployeeController.openWindow();
        // 2) Get the right controller instance
        manageEmployeeController = (ManageEmployeeController) manageEmployeeController.getController();
        // Will hand in the employee object to the next controller
        manageEmployeeController.setSelectedEmployee(employee);

        // Will display the qualifications when the window is opened
        manageEmployeeController.displayQualifications();
        // Will activate the eventHandler to check if the stage is closed

        closeStageHandler(manageEmployeeController.getStage(), manageEmployeeController);

        //Start method to use as entry point
        manageEmployeeController.start();


    }

    //---------------------Event handlers--------------------------//

    private void mouseClickEmployeeHandler() {
        employeeTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2 && employeeTableView.getSelectionModel().getSelectedItem() != null) {
                        if (!manageEmployeeController.isStageOpen()) {


                            configureManageEmployee();

                        } else {
                            System.out.println("Please close the first window before opening a new one");
                        }
                    }
                }
            }
        });
    }

    public void closeStageHandler(Stage stage, Object controller) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                System.out.println("THE WINDOW WAS CLOSED");


                if (controller instanceof NewCourseController){

                    System.out.println("Updated course table view");
                    updateCourseTableView();


                }
                if (controller instanceof ManageEmployeeController) {

                    System.out.println("Updated employee table view");
                    updateEmployeeTableView();
                }


                // TODO CONTINUE


            }
        });
    }

    //------DELETE ELEMENT FROM TABLEVIEW----/(

    public void deleteSelectedCourse() {


        Course course = (Course) courseTableView.getSelectionModel().getSelectedItem();

        DB.deleteCourse(course);

        updateCourseTableView();

    }





    //------REFRESH THE TABLEVIEWS--------//
    private void updateCourseTableView() {
        //constructing data model
        courseList = DB.getCourseList();
        //data binding
        courseTableView.setItems(courseList);
    }

    private void updateEmployeeTableView() {
        //constructing data model
        employeeList = DB.getEmployeeList();
        //data binding
        employeeTableView.setItems(employeeList);
    }

    private void updateCompanyTableView() {
        //constructing data model
        companyList = DB.getCompanyList();
        //data binding
        companyTableView.setItems(companyList);
    }

    private void updateProviderTableView() {
        //constructing data model
        providerList = DB.getProviderList();
        //data binding
        providerTableView.setItems(providerList);
    }

    private void tabHandler() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            System.out.println("The tab selected now has the index: " + newTab.getTabPane().getSelectionModel().getSelectedIndex());
            if (newTab.getTabPane().getSelectionModel().getSelectedIndex() == coursesIndex) {
                buttonLeft.setText("Delete Course");
                buttonLeft.setVisible(true);
                buttonMiddle.setText("Add dates to selected course");
                buttonMiddle.setVisible(true);
                buttonRight.setText("Add New Course");
                buttonRight.setVisible(true);
            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex() == educationMatrixIndex) {
                buttonLeft.setVisible(false);
                buttonMiddle.setVisible(false);
                buttonRight.setVisible(false);
            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex() == employeeIndex) {
                buttonLeft.setText("Delete Employee");
                buttonLeft.setVisible(true);
                buttonMiddle.setVisible(false);
                buttonRight.setText("Add New Employee");
                buttonRight.setVisible(true);
            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex() == calenderIndex) {
                buttonLeft.setVisible(false);
                buttonMiddle.setVisible(false);
                buttonRight.setVisible(false);
            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex() == companiesIndex) {
                buttonLeft.setText("Delete Company");
                buttonLeft.setVisible(true);
                buttonMiddle.setVisible(false);
                buttonRight.setText("Add New Company");
                buttonRight.setVisible(true);
                // TODO If this turns out to be the company that you are currently working on, it should handle the situation without issues
            } else if (newTab.getTabPane().getSelectionModel().getSelectedIndex() == providerIndex) {
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



