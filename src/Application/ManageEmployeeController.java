package Application;

import Domain.Employee;
import Domain.Level;
import Domain.Qualification;
import Domain.Type;
import Persistance.DB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class ManageEmployeeController implements Openable {

    // Static because we want to make sure to always have access to the same (and only) stage
    private static Stage manageEmployeeStage = new Stage();
    private FXMLLoader fxmlLoader;

    // The employee which is sent from the Main Controller
    private Employee selectedEmployee;

    //TextFields
    @FXML
    private TextField nameTextField, cprTextField, emailTextField, phoneNumTextField;

    //Buttons
    @FXML
    private Button editInfoButton, applyChangesButton;

    //Labels
    @FXML
    private Label infoLabel;

    //TableView
    @FXML
    private TableView qualificationsTableView;

    //TableColumns
    @FXML
    private TableColumn typeColumn, descriptionColumn, levelColumn;

    //ObservableList
    private ObservableList<Qualification> qualificationsList;

    // Controller (Window)
    private CourseToEPController courseToEPController = new CourseToEPController();

    public void initialize() {

        // Will retrieve an observable list from the database of all possible qualification types and display it in the dropdown menu
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(DB.getQualificationTypes()));
        // Make the table cells editable
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // Will retrieve an observable list from the database of all possible qualification levels and display it in the dropdown menu
        levelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(DB.getQualificationLevel()));

    }

    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\ManageEmployeeWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            manageEmployeeStage.setTitle("Manage Selected Employee");
            manageEmployeeStage.setScene(new Scene(root));
            manageEmployeeStage.setResizable(false);
            manageEmployeeStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStageOpen() {
        System.out.println("manageEmployeeStage showing: " + manageEmployeeStage);
        return manageEmployeeStage.isShowing();
    }

    @Override
    public Stage getStage() {
        return manageEmployeeStage;
    }


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


    @FXML
    public void editMode() {
        nameTextField.setEditable(true);
        cprTextField.setEditable(true);
        emailTextField.setEditable(true);
        phoneNumTextField.setEditable(true);

        nameTextField.setDisable(false);
        cprTextField.setDisable(false);
        emailTextField.setDisable(false);
        phoneNumTextField.setDisable(false);

        editInfoButton.setDisable(true);
        applyChangesButton.setDisable(false);
        infoLabel.setVisible(false);
    }

    @FXML
    public void saveChangesToEmployee() {
        // This method will try to update the data in the database and return a new fresh employee object if the action succeeded or null if there was an error
        Employee updatedEmployee = DB.updateEmployee(nameTextField.getText(), cprTextField.getText(), emailTextField.getText(), phoneNumTextField.getText(), selectedEmployee.getCompany(), selectedEmployee.getEmployeeID());

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


    @FXML
    public void addCourseToEducationPlan() {
        if (!courseToEPController.isStageOpen()) {
            courseToEPController.openWindow();
        } else {
            System.out.println("Window is already open");
        }
    }

    // QUALIFICATION SECTION

    public void displayQualifications() {

        qualificationsList = DB.getQualifications(selectedEmployee);

        qualificationsTableView.setItems(qualificationsList);

        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        levelColumn.setCellValueFactory(new PropertyValueFactory("level"));

        qualificationsTableView.getColumns().setAll(typeColumn, descriptionColumn, levelColumn);
    }


    /**
     * This method makes you able to update the values of an existing qualification by editing directly from the table cell.
     *
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
            DB.updateQualification(qualification.getQualificationID(), newType.getType(), qualification.getDescription(), qualification.getLevel(), qualification.getEmployeeID(), newType.getTypeID(), qualification.getLevelID());

        } else if (cellEditEvent.getTablePosition().getColumn() == 1) {

            System.out.println("Changed info in description");
            String newDescription = cellEditEvent.getNewValue().toString();
            // Will update the object as well, so that new data wont get overwritten with old data from the instance
            qualification.setDescription(newDescription);
            // Will update the qualification table with the new description written
            DB.updateQualification(qualification.getQualificationID(), qualification.getType(), newDescription, qualification.getLevel(), qualification.getEmployeeID(), qualification.getTypeID(), qualification.getLevelID());

        } else if (cellEditEvent.getTablePosition().getColumn() == 2) {

            System.out.println("Changed info in level");
            Level newLevel = (Level) cellEditEvent.getNewValue();
            // Will update the object as well, so that new data wont get overwritten with old data from the instance
            qualification.setLevel(newLevel.getLevel());
            qualification.setLevelID(newLevel.getLevelID());
            // Will update the qualification table with the new type selected
            DB.updateQualification(qualification.getQualificationID(), qualification.getType(), qualification.getDescription(), newLevel.getLevel(), qualification.getEmployeeID(), qualification.getTypeID(), newLevel.getLevelID());
        }

    }

    @FXML
    public void addNewQualification() {


        DB.insertQualification(selectedEmployee);

        displayQualifications();

    }

    @FXML
    public void deleteQualification() {

        Qualification qualification = (Qualification) qualificationsTableView.getSelectionModel().getSelectedItem();

        System.out.println("You selected: " + qualification.getQualificationID());

        DB.deleteQualification(qualification);

        displayQualifications();
    }


}
