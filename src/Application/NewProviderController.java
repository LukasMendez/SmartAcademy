package Application;

import Persistance.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


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


    @FXML @SuppressWarnings("Duplicates")
    public void confirmChanges(){

        String cvrNo = cvrTextField.getText();
        String address = addressTextField.getText();
        String name = nameTextField.getText();
        String mail = emailTextField.getText();
        String phoneNo = phoneNoTextField.getText();
        int zipCode = Integer.parseInt(zipTextField.getText());

        int rowsAffected = DB.insertProvider(cvrNo,name,address,mail,phoneNo,zipCode);

        if (rowsAffected==1){

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




}

