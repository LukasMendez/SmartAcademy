package Application;

import Domain.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class ManageEmployeeController implements Openable {

    private Stage manageEmployeeStage = new Stage();
    private Employee selectedEmployee;

    //TextFields

    @FXML
    private TextField nameTextField, cprTextField, emailTextField, phoneNumTextField;

    // Controller (Window)
    private CourseToEPController courseToEPController = new CourseToEPController();

    public void initialize() {

    }


    @Override
    public void openWindow() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\ManageEmployeeWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            manageEmployeeStage.setTitle("Manage Selected Employee");
            manageEmployeeStage.setScene(new Scene(root));
            manageEmployeeStage.setResizable(false);
            manageEmployeeStage.show();


            // TESTING AREA TODO PLEASE REMOVE THIS SOON!!!
            if (nameTextField==null){

                System.out.println("TextField is not initialized sorry");
            } else {

                System.out.println("Should not be null! Inserting text");
                nameTextField.setText("heii mom");

            }



        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public boolean isStageOpen() {
        return manageEmployeeStage.isShowing();
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
    private synchronized void setTheText() {


        nameTextField.setText("Nigga");
        cprTextField.setText("Dude wtf");
        emailTextField.setText("I have aids@gmail. gotcha");
        phoneNumTextField.setText("31939191");


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
