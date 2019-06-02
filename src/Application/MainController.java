package Application;

import Domain.Company;
import Domain.Course;
import Domain.Employee;
import Domain.Provider;
import Persistance.DB;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

    // Company selected
    private Company selectedCompany;

    // Company deleted (Used for comparison)
    private Company deletedCompany;

    // ComboBox
    @FXML
    private ComboBox companyDropDown;


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

        companyDropDown.setItems(DB.getCompanyList());
        companyDropDown.getSelectionModel().selectFirst();
        selectedCompany = (Company) companyDropDown.getSelectionModel().getSelectedItem();



        selectedCompanyHandler();
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
        courseList.add(new Course(1, "156", "bloop", "bleep", 8, "locName", "providerName"));
    }

    //---------------------------- OPEN NEW WINDOWS---------------------------------//

    // BUTTON EVENT HANDLERS


    /**
     * In this method we handle the button actions differently depending on which tab is selected. We do that by
     * comparing the selected index numbers with each other.
     */
    @FXML
    public void leftBottomButtonAction() {

        if (tabPane.getSelectionModel().getSelectedIndex() == coursesIndex) {

            deleteSelectedCourse();

        } else if (tabPane.getSelectionModel().getSelectedIndex() == companiesIndex) {

            deleteSelectedCompany();

            companyDropDown.setItems(DB.getCompanyList());

        } else if (tabPane.getSelectionModel().getSelectedIndex() == providerIndex) {

            deleteSelectedProvider();

        } else if (tabPane.getSelectionModel().getSelectedIndex() == employeeIndex) {

            deleteSelectedEmployee();
        }

    }

    /**
     * In this method we handle the button actions differently depending on which tab is selected. We do that by
     * comparing the selected index numbers with each other.
     */
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

    /**
     * In this method we check if the stage is already initialized and open. This way we can prevent the program from
     * opening more than one window at a time. Since the FXMLLoader automatically creates a new controller we use .getController() to
     * regain control over the window. A closeStage EventHandler is also applied, so that the table concerned is updated automatically
     * when the user closes the window.
     */
    public void openAddCourseWindow() {
        if (!newCourseController.isStageOpen()) {
            newCourseController.openWindow();
            newCourseController = (NewCourseController) newCourseController.getController();
            closeStageHandler(newCourseController.getStage(), newCourseController);


        } else {
            System.out.println("Window already open");
        }
    }


    /**
     * In this method we check if the stage is already initialized and open. This way we can prevent the program from
     * opening more than one window at a time. Since the FXMLLoader automatically creates a new controller we use .getController() to
     * regain control over the window.
     * We pass the object of type Course to the new Window. In this way we can assure, that the dates are added to the right course.
     * A closeStage EventHandler is also applied, so that the table concerned is updated automatically
     * when the user closes the window.
     */
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


    /**
     * In this method we check if the stage is already initialized and open. This way we can prevent the program from
     * opening more than one window at a time. Since the FXMLLoader automatically creates a new controller we use .getController() to
     * regain control over the window.
     * We pass the object of type Company to the new Window. In this way we can assure, that the employee is attached to the right company.
     * The company that we add it to will be the one that the user selected from the ComboBox.
     * A closeStage EventHandler is also applied, so that the table concerned is updated automatically
     * when the user closes the window.
     */
    @FXML
    public void openNewEmployeeWindow() {
        if (!newEmployeeController.isStageOpen() && selectedCompany!=null) {

            if (companyDropDown.getSelectionModel().getSelectedItem() != null) {
                newEmployeeController.openWindow();
                newEmployeeController = (NewEmployeeController) newEmployeeController.getController();
                newEmployeeController.setSelectedCompany(selectedCompany);
                closeStageHandler(newEmployeeController.getStage(), newEmployeeController);

            }
        } else {
            System.out.println("Window is already open");
        }
    }

    /**
     * In this method we check if the stage is already initialized and open. This way we can prevent the program from
     * opening more than one window at a time. Since the FXMLLoader automatically creates a new controller we use .getController() to
     * regain control over the window. A closeStage EventHandler is also applied, so that the table concerned is updated automatically
     * when the user closes the window.
     */
    @FXML
    public void openNewCompanyWindow() {
        if (!newCompanyController.isStageOpen()) {
            newCompanyController.openWindow();
            newCompanyController = (NewCompanyController) newCompanyController.getController();

            closeStageHandler(newCompanyController.getStage(), newCompanyController);

        } else {
            System.out.println("Window is already open");
        }
    }

    /**
     * In this method we check if the stage is already initialized and open. This way we can prevent the program from
     * opening more than one window at a time. Since the FXMLLoader automatically creates a new controller we use .getController() to
     * regain control over the window. A closeStage EventHandler is also applied, so that the table concerned is updated automatically
     * when the user closes the window.
     */
    @FXML
    public void openNewProviderWindow() {
        if (!newProviderController.isStageOpen()) {
            newProviderController.openWindow();
            newProviderController = (NewProviderController) newProviderController.getController();
            closeStageHandler(newProviderController.getStage(), newProviderController);

        } else {
            System.out.println("Window is already open");
        }
    }


    /**
     * This method will configure everything needed for the ManageEmployeeWindow. This includes opening of the ManageEmployeeWindow as well
     * as regaining control over the controller class, passing the employee object to the controller, displaying the qualifications based on
     * the employee ID and applying the closeEventHandler that refreshes the Employee table when the window is closed.
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


    /**
     * This helper-method refreshes the combobox and retains the selected company when a new company has been added
     * to the database.
     */
    private void newCompanyRefresher() {


        companyDropDown.setItems(DB.getCompanyList());

        //will make sure to reselect the item you were already looking at
        companyDropDown.getSelectionModel().select(selectedCompany);

        System.out.println("Executed: newCompanyRefresher()");


    }

    /**
     * The company selected should under no circumstances stay selected if it has just been removed. This helper-method
     * makes sure to handle situations like that, as well as it makes sure to refresh the combobox and keep the company selected if another company
     * was been removed.
     */
    private void removalOfCompanyRefresher() {

        System.out.println("Selected company CVR: " + selectedCompany.getCVRNumber());
        System.out.println("Deleted company CVR: " + deletedCompany.getCVRNumber());

        if ((selectedCompany.getCVRNumber().equals(deletedCompany.getCVRNumber()))){
            selectedCompany = null;
            companyDropDown.setItems(DB.getCompanyList());
            companyDropDown.getSelectionModel().select(selectedCompany);

            System.out.println("Company deleted was the same as selected");
        } else {
            System.out.println("Company selected and deleted company was not the same");
            companyDropDown.setItems(DB.getCompanyList());
            companyDropDown.getSelectionModel().select(selectedCompany);

        }
    }



    //---------------------Event handlers--------------------------//

    /**
     * This event handler is dedicated to the configureManageEmployee method. It makes the user able to double-click on a
     * particular employee record and manage his profile.
     */
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

                if (controller instanceof NewCourseController) {
                    System.out.println("Updated course table view");
                    updateCourseTableView();
                }
                if (controller instanceof ManageEmployeeController) {
                    System.out.println("Updated employee table view");
                    updateEmployeeTableView();
                }
                if (controller instanceof NewCompanyController) {
                    System.out.println("Updated company table view");
                    updateCompanyTableView();
                    newCompanyRefresher();
                }
                if (controller instanceof NewProviderController) {
                    System.out.println("Updated provider table view");
                    updateProviderTableView();
                }
                if (controller instanceof NewEmployeeController) {
                    System.out.println("Update employee table view");
                    updateEmployeeTableView();

                }


                // TODO CONTINUE


            }
        });
    }

    private void selectedCompanyHandler() {

        companyDropDown.valueProperty().addListener((obs, oldItem, newItem) -> {

            // System.out.println("User clicked on: " + newItem);

            if (companyDropDown.getSelectionModel().getSelectedItem() != null) {

                selectedCompany = (Company) companyDropDown.getSelectionModel().getSelectedItem();

                System.out.println("You just casted the object as a Company, and the output is: " + selectedCompany.getName());

                updateProviderTableView();
                updateCompanyTableView();
                updateCourseTableView();

                updateEmployeeTableView();

            }


        });

    }


    //------DELETE ELEMENT FROM TABLEVIEW----/(

    private void deleteSelectedCourse() {


        Course course = (Course) courseTableView.getSelectionModel().getSelectedItem();

        DB.deleteCourse(course);

        updateCourseTableView();


    }


    private void deleteSelectedCompany() {

        Company company = (Company) companyTableView.getSelectionModel().getSelectedItem();

        DB.deleteCompany(company);

        updateCompanyTableView();

        deletedCompany = company;

        removalOfCompanyRefresher();



    }

    private void deleteSelectedProvider() {

        Provider provider = (Provider) providerTableView.getSelectionModel().getSelectedItem();

        DB.deleteProvider(provider);

        updateProviderTableView();
        updateCourseTableView();


    }

    private void deleteSelectedEmployee() {

        Employee employee = (Employee) employeeTableView.getSelectionModel().getSelectedItem();

        DB.deleteEmployee(employee);

        updateEmployeeTableView();


    }


    //------REFRESH THE TABLEVIEWS--------//
    private void updateCourseTableView() {
        //constructing data model
        courseList = DB.getCourseList();
        //data binding
        courseTableView.setItems(courseList);
        System.out.println("Updated Course TableView");
    }

    private void updateEmployeeTableView() {
        //constructing data model
        employeeList = DB.getEmployeeList(selectedCompany.getCVRNumber());
        //data binding
        employeeTableView.setItems(employeeList);
        System.out.println("Updated Employee TableView");
    }

    private void updateCompanyTableView() {
        //constructing data model
        companyList = DB.getCompanyList();
        //data binding
        companyTableView.setItems(companyList);
        System.out.println("Updated Company TableView");

       // companyDropDown.getSelectionModel().selectFirst();

    }

    private void updateProviderTableView() {
        //constructing data model
        providerList = DB.getProviderList();
        //data binding
        providerTableView.setItems(providerList);
        System.out.println("Updated Provider TableView");

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



