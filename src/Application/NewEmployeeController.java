package Application;

import BusinessServices.InputValidation;
import Domain.Company;
import Persistance.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class NewEmployeeController implements Openable {

    private static Stage newEmployeeStage = new Stage();
    private FXMLLoader fxmlLoader;
    private DB db = DB.getInstance();

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

    /**
     * Method used for configuring the Window and opening it.
     */
    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddEmployeeWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newEmployeeStage.setTitle("Add New Employee");
            newEmployeeStage.getIcons().add(new Image("UI/Images/users.png"));
            newEmployeeStage.setScene(new Scene(root));
            newEmployeeStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();
            newEmployeeStage.show();
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
            newEmployeeStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized = true;
        }
    }

    /**
     * Will check if the stage is showing (open).
     *
     * @return true or false
     */
    @Override
    public boolean isStageOpen() {
        return newEmployeeStage.isShowing();
    }

    /**
     * Will get you the stage of the instance you are working with.
     *
     * @return a Stage instance.
     */
    @Override
    public Stage getStage() {
        return newEmployeeStage;
    }

    /**
     * Method used for getting the controller of the FXMLLoader. Since the FXMLLoader creates a new Controller instance, it
     * is important to regain control of the active Controller. The method only
     * returns an Object, but the object can be casted as a different object type (As the controller object needed)
     *
     * @return Object (Controller)
     */
    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    /**
     * Whenever you add a new employee it is important to know which company he/she is working for. Therefore we use
     * this method to tell the controller what company he/she should be added to.
     *
     * @param company
     */
    public void setSelectedCompany(Company company) {
        this.selectedCompany = company;

    }

    /**
     * This method saves the data from the text fields in the database. We proceed the information using a method from the DB instance.
     * The method returns a number indicating the amount of rows affected. If 1 or more rows were affected it will let the user know, that the data was saved
     * successfully (It should only be possible to affect 1 row though, and not more). Else it will display an error message.
     */
    @FXML
    @SuppressWarnings("Duplicates")
    public void saveEmployee() {
        String cprNo = cprTextField.getText();
        String name = nameTextField.getText();
        String mail = emailTextField.getText();
        String phoneNo = phoneNoTextField.getText();

        int rowsAffected = DB.insertEmployee(cprNo, name, mail, phoneNo, selectedCompany.getCVRNumber());

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

    /**
     * Checking the input, what user have entered. The is the other method "information pop up window", then the user pointing with the mouse on TextField.
     */
    @FXML
    public void inputValidator(KeyEvent event) {
        InputValidation inputValidation = new InputValidation();

        inputValidation.checkInputEmployee(nameTextField, cprTextField, phoneNoTextField, emailTextField, event);
        smallWindowPopUpProvider();
    }
    /**
     * A small information window, then the user points on TextField inside the window add new employee.
     */
    @FXML
    private void smallWindowPopUpProvider() {

        final Tooltip tooltipName = new Tooltip();
        tooltipName.setText("Enter the name ");

        final Tooltip tooltipCPR = new Tooltip();
        tooltipCPR.setText("Enter the CPR number ");

        final Tooltip tooltipPhone = new Tooltip();
        tooltipPhone.setText("Enter the phoneNo. ");

        final Tooltip tooltipEmail = new Tooltip();
        tooltipEmail.setText("Enter the Email ");

        nameTextField.setTooltip(tooltipName);
        cprTextField.setTooltip(tooltipCPR);
        phoneNoTextField.setTooltip(tooltipPhone);
        emailTextField.setTooltip(tooltipEmail);

    }
}
