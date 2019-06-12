package Application;

import BusinessServices.InputValidation;
import Domain.*;
import Domain.Employee;
import Domain.Level;
import Domain.Qualification;
import Domain.Type;
import Persistance.DB;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ManageEmployeeController implements Openable {

    private DB db = DB.getInstance();

    private boolean history;
    private int consultantID = 1; //hardcoded since consultant handling has not been implemented

    // Static because we want to make sure to always have access to the same (and only) stage
    private static Stage manageEmployeeStage = new Stage();
    private FXMLLoader fxmlLoader;

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    // The employee which is sent from the Main Controller
    private Employee selectedEmployee;

    //TextFields
    @FXML
    private TextField nameTextField, cprTextField, emailTextField, phoneNumTextField;

    //Buttons
    @FXML
    private Button editInfoButton, applyChangesButton, toggleHistoryButton, deleteButton, completedButton, addButton;

    //Labels
    @FXML
    private Label infoLabel;

    //TableView
    @FXML
    private TableView qualificationsTableView, educationPlanTableView;

    //TableColumns
    @FXML
    private TableColumn typeColumn, descriptionColumn, levelColumn, //qualification
            dateColumn, informationColumn, providerColumn, locationColumn, priorityColumn, planIDColumn, activeColumn, completedColumn; //educationPlan

    //ObservableList
    private ObservableList<Qualification> qualificationsList;
    private ObservableList<EducationPlan> educationPlansList;

    EducationPlan activePlan;

    // Controller (Window)
    private CourseToEPController courseToEPController = new CourseToEPController();

    public void initialize() {
        // Will retrieve an observable list from the database of all possible qualification types and display it in the dropdown menu
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(db.getQualificationTypes()));
        // Make the table cells editable
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // Will retrieve an observable list from the database of all possible qualification levels and display it in the dropdown menu
        levelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(db.getQualificationLevel()));
    }

    /**
     * setting up databinding for tableView
     */
    public void start(){
        history = false; //used for toggling between history and active courses

        //EducationPlan
        //constructing data model + data binding
        updateEducationPlanTableView(true);
        //splitting out the data in the model
        dateColumn.setCellValueFactory(new PropertyValueFactory("date"));
        informationColumn.setCellValueFactory(new PropertyValueFactory("information"));
        providerColumn.setCellValueFactory(new PropertyValueFactory("provider"));
        locationColumn.setCellValueFactory(new PropertyValueFactory("location"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory("priority"));
        planIDColumn.setCellValueFactory(new PropertyValueFactory("planID"));
        activeColumn.setCellValueFactory(new PropertyValueFactory("isActiveWrapper"));
        completedColumn.setCellValueFactory(new PropertyValueFactory("isCompletedWrapper"));
        //representing the data in the columns
        educationPlanTableView.getColumns().setAll(dateColumn, informationColumn, providerColumn, locationColumn, priorityColumn, planIDColumn, activeColumn, completedColumn);
    }

    /**
     * Updates the educationPlan table by retrieving a fresh ObservableList from the DB class
     */
    private void updateEducationPlanTableView(boolean isActive){
        //constructing data model
        educationPlansList = db.getEducationPlanList(selectedEmployee.getEmployeeID(), isActive);
        //data binding
        if (educationPlansList != null) {
            educationPlanTableView.setItems(educationPlansList);
        }
    }


    //  manageEmployeeStage.setOnCloseRequest(event -> manageEmployeeStage.initModality(null));

    /**
     * Used for toggling between history/active courses in educationPlanTableView when pressing toggleHistoryButton
     */
    @FXML
    private void toggleHistory() {
        if (history == false) {
            updateEducationPlanTableView(false);
            toggleHistoryButton.setText("Show Active");
            deleteButton.setVisible(false);
            completedButton.setVisible(false);
            addButton.setVisible(false);
            history = true;
        } else {
            updateEducationPlanTableView(true);
            toggleHistoryButton.setText("Show History");
            deleteButton.setVisible(true);
            completedButton.setVisible(true);
            addButton.setVisible(true);
            history = false;
        }
    }

    /**
     * Method used for configuring the Window and opening it.
     */
    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\ManageEmployeeWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            manageEmployeeStage.setTitle("Manage Selected Employee");
            manageEmployeeStage.getIcons().add(new Image("UI/Images/passport.png"));
            manageEmployeeStage.setScene(new Scene(root));
            manageEmployeeStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            manageEmployeeStage.show();
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
            manageEmployeeStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized = true;
        }
    }

    /**
     * Will check if the stage is showing (open).
     * @return true or false
     */
    @Override
    public boolean isStageOpen() {
        System.out.println("manageEmployeeStage showing: " + manageEmployeeStage);
        return manageEmployeeStage.isShowing();
    }

    /**
     * Will get you the stage of the instance you are working with.
     * @return a Stage instance.
     */
    @Override
    public Stage getStage() {
        return manageEmployeeStage;
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
     * Used for retrieving the employee from the Main Controller.
     * Usually the one that the user has selected/clicked on.
     * The metadata will be displayed on the TextFields.
     *
     * @param employee employee object
     */
    public void setSelectedEmployee(Employee employee) {
        selectedEmployee = employee;
        nameTextField.setText(selectedEmployee.getName());
        cprTextField.setText(selectedEmployee.getCPRNumber());
        emailTextField.setText(selectedEmployee.getEmail());
        phoneNumTextField.setText(selectedEmployee.getPhoneNumber());
        System.out.println("The record belongs to: " + selectedEmployee.getName() + " and the ID is: " + selectedEmployee.getEmployeeID());
    }


    /**
     * This method is used when you want to edit the selected employee. It basically sets the text fields to editable, and
     * enables the save button.
     */
    @FXML
    public void editMode() {
        // MAKE THE TEXT FIELDS EDITABLE
        nameTextField.setEditable(true);
        cprTextField.setEditable(true);
        emailTextField.setEditable(true);
        phoneNumTextField.setEditable(true);
        // RE-ENABLE THE TEXT FIELDS
        nameTextField.setDisable(false);
        cprTextField.setDisable(false);
        emailTextField.setDisable(false);
        phoneNumTextField.setDisable(false);
        // DISABLE THE EDIT BUTTON AND ENABLE THE SAVE BUTTON
        editInfoButton.setDisable(true);
        applyChangesButton.setDisable(false);
        infoLabel.setVisible(false);
    }

    /**
     * This method is connected with the save button ("Apply changes") which creates an Employee object and calls the db.updateEmployee method from the DB class.
     * The DB method will either return a complete Employee object or null. If it returns a usable object it indicates, that the changes were saved successfully.
     * If it returns null it means that there was some kind of error, and the user will have to check the fields to see if he wrote something invalid.
     */
    @FXML
    public void saveChangesToEmployee() {
        // This method will try to update the data in the database and return a new fresh employee object if the action succeeded or null if there was an error
        Employee updatedEmployee = db.updateEmployee(nameTextField.getText(), cprTextField.getText(), emailTextField.getText(), phoneNumTextField.getText(), selectedEmployee.getCompany(), selectedEmployee.getEmployeeID());

        if (updatedEmployee != null) {
            editInfoButton.setDisable(false);
            applyChangesButton.setDisable(true);

            infoLabel.setText("Info was saved successfully");
            infoLabel.setVisible(true);

            nameTextField.setEditable(false);
            cprTextField.setEditable(false);
            emailTextField.setEditable(false);
            phoneNumTextField.setEditable(false);

            nameTextField.setDisable(true);
            cprTextField.setDisable(true);
            emailTextField.setDisable(true);
            phoneNumTextField.setDisable(true);

            System.out.println("Updated record successfully (DEBUGGING)");
        } else {
            infoLabel.setText("Invalid information. Please try again");
            infoLabel.setVisible(true);
        }
    }

    /**
     * Opening the AddCourseToEP window when pressing the addButton.
     * Sets up the controller and stageHandler and runs the start method from courseToEPController
     */
    @FXML
    public void addCourseToEducationPlan() {
        if (!courseToEPController.isStageOpen()) {
            courseToEPController.openWindow();
            courseToEPController = (CourseToEPController) courseToEPController.getController();
            closeStageHandler(courseToEPController.getStage(), courseToEPController);
            courseToEPController.start();

        } else {
            System.out.println("Window is already open");
        }
    }

    /**
     * This method initializes the Window EventHandler. It's purpose is to execute some code after you close the window chosen.
     * Calls the method that adds the selected course to the education plan (addCourseToEP())
     *
     * @param stage      the stage object. Typically given from a controller
     * @param controller the controller object used together with instanceof to check which class it belongs to
     */
    public void closeStageHandler(Stage stage, Object controller) { //TODO WILL WE USE THE CONTROLLER??
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                //doing stuff when the window was closed
                System.out.println("THE WINDOW WAS CLOSED");
                addCourseToEP(courseToEPController.getSelectedCourse());
            }
        });
    }

    /**
     * Method for adding the selected course to the active education plan.
     * Creates a new record in tblCoursePlan in the database.
     *
     * @param selectedCourse the course to be added (that was selected in the CourseToEPController)
     */
    private void addCourseToEP(CourseByPeriod selectedCourse){
        //create new active education plan if there is none
        createNewEP();
        //search for an active plan and assign to activePlan variable
        setActivePlan();
        //adding the selected course to the active plan by inserting record in DB
        db.addCoursePlan(activePlan, selectedCourse);
        //updating the table view to reflect the change
        updateEducationPlanTableView(true); //updating tableView
    }

    /**
     * Creates a new active education plan if there is none
     */
    private void createNewEP(){
        //if getPlanID db method returns sentinel value(is null)
        if(db.getActivePlanID(selectedEmployee.getEmployeeID(),consultantID) == 0){
            //create new active education plan
            db.createNewEducationPlan(selectedEmployee.getEmployeeID(), consultantID);
        }
    }

    /**
     * Iterating the educationPlansList to find a plan that is active.
     * If no active plan is found a placeholder will be set.
     */
    private void setActivePlan(){
        //searching for active plan
        for (int i = 0; i < educationPlansList.size(); i++) {
            //will only get a hit if the list is not empty
            if(educationPlansList.get(i).getIsActive() == 1 && educationPlansList.get(i).getEmployeeID() == selectedEmployee.getEmployeeID()){
                //assigning the found active plan to activePlan variable
                activePlan = educationPlansList.get(i);
            }
        }
        //set placeholder education plan if activePlan is still null (prevents error if adding course and plan list is empty)
        if(activePlan == null){
            activePlan = new EducationPlan(0,null, null, null, null, 0,
                    db.getActivePlanID(selectedEmployee.getEmployeeID(), 1),1,0,
                    selectedEmployee.getEmployeeID());
        }
    }

    /**
     * Removes a course plan record from the database using coursePlanID
     */
    @FXML
    private void removeCourseFromEp() {
        //get coursePlanID
        int coursePlanID = getCoursePlanID();
        //remove course plan from db
        db.removeCoursePlan(coursePlanID);
        //update tableView
        updateEducationPlanTableView(true);
    }

    /**
     * Sets the selected course as completed in the database and updates the table view to reflect the change.
     */
    @FXML
    private void setCoursePlanAsCompleted() {
        //get coursePlanID
        int coursePlanID = getCoursePlanID();
        //update completed field in db
        db.toggleCoursePlanCompletion(coursePlanID);
        //update tableView
        updateEducationPlanTableView(true);
    }

    /**
     * Looks at the selected item in the tableView to get the associated coursePlanID from the database
     * @return the coursePlanID of the selected item
     */
    private int getCoursePlanID(){
        //assigns the selected coursePlan to activePlan global variable
        activePlan = (EducationPlan)educationPlanTableView.getSelectionModel().getSelectedItem();
        //queries database for coursePlanID by looking at dateID and planID of activePlan
        return db.getCoursePlanID(activePlan.getDateID(), activePlan.getPlanID());
    }

    // QUALIFICATION SECTION

    /**
     * Will display all the qualifications of the selected employee in a TableView. This method is called at the very beginning to make sure, that
     * these information are visible as soon as the window is presented.
     */
    public void displayQualifications() {
        qualificationsList = db.getQualifications(selectedEmployee);

//        System.out.println("First description of qualification: " + qualificationsList.get(0).getDescription());


        qualificationsTableView.setItems(qualificationsList);
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        levelColumn.setCellValueFactory(new PropertyValueFactory("level"));
        qualificationsTableView.getColumns().setAll(typeColumn, descriptionColumn, levelColumn);
    }


    /**
     * This method makes you able to update the values of an existing qualification by editing directly from the table cell.
     * @param cellEditEvent the new value that is entered
     */
    @FXML
    public void onEditChangedType(TableColumn.CellEditEvent cellEditEvent) {
        Qualification qualification = (Qualification) qualificationsTableView.getSelectionModel().getSelectedItem();

        if (cellEditEvent.getTablePosition().getColumn() == 0) {
            System.out.println("Changed info in type");
            Type newType = (Type) cellEditEvent.getNewValue();
            // Will update the object as well, so that new data wont get overwritten with old data from the instance
            qualification.setType(newType.getType());
            qualification.setTypeID(newType.getTypeID());
            // Will update the qualification table with the new type selected
            db.updateQualification(qualification.getQualificationID(), newType.getType(), qualification.getDescription(), qualification.getLevel(), qualification.getEmployeeID(), newType.getTypeID(), qualification.getLevelID());
        } else if (cellEditEvent.getTablePosition().getColumn() == 1) {
            System.out.println("Changed info in description");
            String newDescription = cellEditEvent.getNewValue().toString();
            // Will update the object as well, so that new data wont get overwritten with old data from the instance
            qualification.setDescription(newDescription);
            // Will update the qualification table with the new description written
            db.updateQualification(qualification.getQualificationID(), qualification.getType(), newDescription, qualification.getLevel(), qualification.getEmployeeID(), qualification.getTypeID(), qualification.getLevelID());
        } else if (cellEditEvent.getTablePosition().getColumn() == 2) {
            System.out.println("Changed info in level");
            Level newLevel = (Level) cellEditEvent.getNewValue();
            // Will update the object as well, so that new data wont get overwritten with old data from the instance
            qualification.setLevel(newLevel.getLevel());
            qualification.setLevelID(newLevel.getLevelID());
            // Will update the qualification table with the new type selected
            db.updateQualification(qualification.getQualificationID(), qualification.getType(), qualification.getDescription(), newLevel.getLevel(), qualification.getEmployeeID(), qualification.getTypeID(), newLevel.getLevelID());
        }
    }

    /**
     * This method adds a new qualification. The default value on all fields will typically just "Unspecified". The user can then modify the information so that is suits the qualification of the employee.
     */
    @FXML
    public void addNewQualification() {
        db.insertQualification(selectedEmployee);
        displayQualifications();
    }

    /**
     * This method deletes the qualification selected in the TableView. It will start by executing the delete statement in the DB class and then refresh the TableView.
     */
    @FXML
    public void deleteQualification() {
        Qualification qualification = (Qualification) qualificationsTableView.getSelectionModel().getSelectedItem();
        System.out.println("You selected: " + qualification.getQualificationID());
        db.deleteQualification(qualification);
        displayQualifications();
    }

    /**
     * Checking the input, what user have entered. The is the other method "information pop up window", then the user pointing with the mouse on TextField.
     */
    @FXML
    public void inputValidator(KeyEvent event) {
        InputValidation inputValidation = new InputValidation();
        smallWindowPopUpProvider();
        inputValidation.checkInputEmployee(nameTextField, cprTextField, phoneNumTextField, event);
    }

    /**
     * The small window pop up then user putting mouse on the TextField in manage employee window (where the user want to edit employee).
     */
    @FXML
    private void smallWindowPopUpProvider() {
        Tooltip tooltipName = new Tooltip();
        tooltipName.setText("Enter the name");
        Tooltip tooltipCPR = new Tooltip();
        tooltipCPR.setText("Enter the CPR number");
        Tooltip tooltipPhoneNo = new Tooltip();
        tooltipPhoneNo.setText("Enter the phone number");
        Tooltip tooltipMail = new Tooltip();
        tooltipMail.setText("Enter the email");

        nameTextField.setTooltip(tooltipName);
        cprTextField.setTooltip(tooltipCPR);
        phoneNumTextField.setTooltip(tooltipPhoneNo);
        emailTextField.setTooltip(tooltipMail);
    }

}
