package Application;

import Domain.Employee;
import Domain.Qualification;
import Persistance.DB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public void displayQualifications(){


        // TODO MAKE SURE THAT DB.GetQualifications actually have a functioning body
        qualificationsList = DB.getQualifications(selectedEmployee);

        qualificationsTableView.setItems(qualificationsList);

        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        levelColumn.setCellValueFactory(new PropertyValueFactory("level"));

        qualificationsTableView.getColumns().setAll(typeColumn,descriptionColumn,levelColumn);

    }



    public void onEditChangedType(TableColumn.CellEditEvent cellEditEvent) {
        Qualification qualification;

        //TODO MAKE IT POSSIBLE TO EDIT EXISTING CELLS

    }
}
