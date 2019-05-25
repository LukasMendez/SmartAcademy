package Application;

import Domain.Employee;
import Persistance.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class ManageEmployeeController implements Openable {

    // Static because we only want one Stage at a time
    private static Stage manageEmployeeStage = new Stage();
    private FXMLLoader fxmlLoader;


    // Instance of itself. Used for regaining access to the instance from the MainController.
    private ManageEmployeeController manageEmployeeController;


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



    // Controller (Window)
    private CourseToEPController courseToEPController = new CourseToEPController();


    public void initialize(){

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

            // Since the FXMLLoader creates a new Controller object in the background, we need regain control
            manageEmployeeController = fxmlLoader.getController();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public boolean isStageOpen() {


        System.out.println("manageEmployeeStage showing: " + manageEmployeeStage);

        return manageEmployeeStage.isShowing();

    }

    public Stage getStage(){

        return manageEmployeeStage;
    }


    @Override
    public Object getController(){

        return manageEmployeeController;
    }


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

        editInfoButton.setDisable(true);
        applyChangesButton.setDisable(false);
        infoLabel.setVisible(false);

    }

    @FXML
    public void saveChangesToEmployee(){

        // This method will try to update the data in the database and return a new fresh employee object if the action succeeded or null if there was an error
        Employee updatedEmployee = DB.updateEmployee(nameTextField.getText(),cprTextField.getText(), emailTextField.getText(),phoneNumTextField.getText(),selectedEmployee.getCompany(),selectedEmployee.getEmployeeID());

        if (updatedEmployee!=null){

            editInfoButton.setDisable(false);
            applyChangesButton.setDisable(true);

            infoLabel.setText("Info was saved successfully");
            infoLabel.setVisible(true);

            nameTextField.setEditable(false);
            cprTextField.setEditable(false);
            emailTextField.setEditable(false);
            phoneNumTextField.setEditable(false);


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


}
