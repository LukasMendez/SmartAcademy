package Application;

import BusinessServices.InputValidation;
import Persistance.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class NewCompanyController implements Openable {

    private static Stage newCompanyStage = new Stage();
    private FXMLLoader fxmlLoader;
    private DB db = DB.getInstance();

    // Labels
    @FXML
    private Label infoLabel;

    // TextFields
    @FXML
    private TextField nameTextField, cvrNoTextField, addressTextField, emailTextField, phoneNoTextField, zipTextField;

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    /**
     * Method used for configuring the Window and opening it.
     */
    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCompanyWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newCompanyStage.setTitle("Add New Company");
            newCompanyStage.getIcons().add(new Image("UI/Images/factory.png"));
            newCompanyStage.setScene(new Scene(root));
            newCompanyStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();
            newCompanyStage.show();
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
            newCompanyStage.initModality(Modality.APPLICATION_MODAL);
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
        return newCompanyStage.isShowing();
    }

    /**
     * Will get you the stage of the instance you are working with.
     *
     * @return a Stage instance.
     */
    @Override
    public Stage getStage() {
        return newCompanyStage;
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
     * This method saves the data from the text fields in the database. We proceed the information using a method from the DB instance.
     * The method returns a number indicating the amount of rows affected. If 1 or more rows were affected it will let the user know, that the data was saved
     * successfully (It should only be possible to affect 1 row though, and not more). Else it will display an error message.
     */
    @FXML
    @SuppressWarnings("Duplicates")
    public void confirmChanges() {
        String cvrNo = cvrNoTextField.getText();
        String address = addressTextField.getText();
        String name = nameTextField.getText();
        String mail = emailTextField.getText();
        String phoneNo = phoneNoTextField.getText();
        int zipCode = Integer.parseInt(zipTextField.getText());

        int rowsAffected = db.insertCompany(cvrNo, address, name, mail, phoneNo, zipCode);
        if (rowsAffected > 0) {
            infoLabel.setVisible(true);
            infoLabel.setText("Saved successfully");
            cvrNoTextField.setText("");
            addressTextField.setText("");
            nameTextField.setText("");
            emailTextField.setText("");
            phoneNoTextField.setText("");
            zipTextField.setText("");
        } else {
            infoLabel.setText("Invalid info! Please check your entered data again!");
        }
    }

    /**
     * Object from the Business Service layer used for running input validation on the TextFields.
     *
     * @param event every single time a user types something on the keyboard
     */
    @FXML
    public void inputValidator(KeyEvent event) {
        InputValidation inputValidation = new InputValidation();
        inputValidation.checkInputCompany(phoneNoTextField, zipTextField, event);
    }
}
