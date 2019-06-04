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


    @FXML
    @SuppressWarnings("Duplicate")
    public void redFieldName (KeyEvent event){
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                nameTextField.setText(newValue.replaceAll("[^\\sa-zA-Z]", "")); // Allows only wirte a letters!
            }
        });
    }


    @FXML
    @SuppressWarnings("Duplicate")
    public void redFieldPHoneNumber(KeyEvent event) { // Allows you write only numbers !

        char asciiTableNumber = 43;
        char firstLetterCheck = 0;

        if (((int) event.getCharacter().charAt(firstLetterCheck)) != asciiTableNumber && !Character.isDigit(event.getCharacter().charAt(firstLetterCheck))) {

            event.consume();
            phoneNoTextField.setStyle("-fx-text-box-border:#ff2000;-fx-control-inner-background:red;-fx-faint-focus-color:red;");

        } else {
            phoneNoTextField.setStyle("-fx-text-box-border:#fff5fa;-fx-control-inner-background:white;-fx-faint-focus-color:white;");

        }

    }


    @FXML
    public void redFieldZipCode(KeyEvent event) { // Allows you write only numbers !

        char asciiTableNumber = 9;
        char firstLetterCheck = 0;

        if (((int) event.getCharacter().charAt(firstLetterCheck)) != asciiTableNumber && !Character.isDigit(event.getCharacter().charAt(firstLetterCheck))) {

            event.consume();
            zipTextField.setStyle("-fx-text-box-border:#ff2000;-fx-control-inner-background:red;-fx-faint-focus-color:red;");

        } else {
            zipTextField.setStyle("-fx-text-box-border:#feefff;-fx-control-inner-background:white;-fx-faint-focus-color:#fbffff;");

        }

    }


    @FXML
    public void inputValidator(KeyEvent event) {

        InputValidation inputValidation = new InputValidation();

        inputValidation.checkInputCompany(phoneNoTextField,zipTextField,event);


    }






}

