package Application;

import Domain.Company;
import Persistance.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class NewEmployeeController implements Openable {

    private static Stage newEmployeeStage = new Stage();
    private FXMLLoader fxmlLoader;

    // The company which is sent from the Main Controller
    private Company selectedCompany;

    // Labels
    @FXML
    private Label infoLabel;

    // TextFields
    @FXML
    private TextField nameTextField, cprTextField, emailTextField, phoneNoTextField;

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddEmployeeWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newEmployeeStage.setTitle("Add New Employee");
            newEmployeeStage.setScene(new Scene(root));
            newEmployeeStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            newEmployeeStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupModality() {
        if (!isInitialized) {
            newEmployeeStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized = true;
        }
    }

    @Override
    public boolean isStageOpen() {
        return newEmployeeStage.isShowing();
    }

    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    @Override
    public Stage getStage() {
        return newEmployeeStage;
    }


    public void setSelectedCompany(Company company) {
        this.selectedCompany = company;

    }



    @FXML
    @SuppressWarnings("Duplicates")
    public void saveEmployee() {


        String cprNo = cprTextField.getText();
        String name = nameTextField.getText();
        String mail = emailTextField.getText();
        String phoneNo = phoneNoTextField.getText();


        int rowsAffected = DB.insertEmployee(cprNo,name,mail,phoneNo,selectedCompany.getCVRNumber());

        if (rowsAffected == 1) {

            infoLabel.setVisible(true);
            infoLabel.setText("Saved successfully");

            cprTextField.setText("");
            nameTextField.setText("");
            emailTextField.setText("");
            phoneNoTextField.setText("");

        } else {

            infoLabel.setText("Invalid info! Please check your entered data again!");
        }

    }


}
