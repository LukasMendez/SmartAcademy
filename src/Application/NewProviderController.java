package Application;

import BusinessServices.InputValidation;
import Persistance.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.internal.util.xml.impl.Input;


/**
 * Created by Lukas
 * 24-05-2019.
 */
public class NewProviderController implements Openable {

    private static Stage newProviderStage = new Stage();
    private FXMLLoader fxmlLoader;

    // Labels
    @FXML
    private Label infoLabel;

    // TextFields
    @FXML
    private TextField nameTextField, cvrTextField, addressTextField, emailTextField, phoneNoTextField, zipTextField;


    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddProviderWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newProviderStage.setTitle("Add New Provider");
            newProviderStage.getIcons().add(new Image("UI/Images/building.png"));
            newProviderStage.setScene(new Scene(root));
            newProviderStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            newProviderStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupModality() {
        if (!isInitialized) {
            newProviderStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized = true;
        }
    }

    @Override
    public boolean isStageOpen() {
        return newProviderStage.isShowing();
    }

    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    @Override
    public Stage getStage() {
        return newProviderStage;
    }


    @FXML
    @SuppressWarnings("Duplicates")
    public void confirmChanges() {

        String cvrNo = cvrTextField.getText();
        String address = addressTextField.getText();
        String name = nameTextField.getText();
        String mail = emailTextField.getText();
        String phoneNo = phoneNoTextField.getText();
        int zipCode = Integer.parseInt(zipTextField.getText());

        int rowsAffected = DB.insertProvider(cvrNo, name, address, mail, phoneNo, zipCode);

        if (rowsAffected == 1) {

            infoLabel.setVisible(true);
            infoLabel.setText("Saved successfully");
            cvrTextField.setText("");
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
     * Checking the input, what user have entered. The is the other method "information pop up window", then the user pointing with the mouse on TextField.
     */
    @FXML
    public void inputValidator(KeyEvent event) {


        InputValidation inputValidation = new InputValidation();


        inputValidation.checkInputCompany(phoneNoTextField, zipTextField, nameTextField, event);
        smallWindowPopUpProvider();

    }

    /**
     * A small information window, then the user points on TextField inside the window add new provider.
     */
    @FXML
    @SuppressWarnings("Duplicates")
    private void smallWindowPopUpProvider() {

        final Tooltip tooltipName = new Tooltip();
        tooltipName.setText("Enter the name ");

        final Tooltip tooltipCVR = new Tooltip();
        tooltipCVR.setText("Enter the CVR number ");

        final Tooltip tooltipAddress = new Tooltip();
        tooltipAddress.setText("Enter the address ");

        final Tooltip tooltipEmail = new Tooltip();
        tooltipEmail.setText("Enter the Email ");

        final Tooltip tooltipPhone = new Tooltip();
        tooltipPhone.setText("Enter the phoneNo. ");

        final Tooltip tooltipZip = new Tooltip();
        tooltipZip.setText("Enter the zip ");


        nameTextField.setTooltip(tooltipName);
        cvrTextField.setTooltip(tooltipCVR);
        addressTextField.setTooltip(tooltipAddress);
        emailTextField.setTooltip(tooltipEmail);
        phoneNoTextField.setTooltip(tooltipPhone);
        zipTextField.setTooltip(tooltipZip);

    }


}

