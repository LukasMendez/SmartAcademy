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

    @Override
    public void setupModality(){
        if (!isInitialized){
            newCompanyStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized=true;
        }
    }



    @Override
    public boolean isStageOpen() {
        return newCompanyStage.isShowing();
    }

    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    @Override
    public Stage getStage() {
        return newCompanyStage;
    }


    @FXML @SuppressWarnings("Duplicates")
    public void confirmChanges(){

        String cvrNo = cvrNoTextField.getText();
        String address = addressTextField.getText();
        String name = nameTextField.getText();
        String mail = emailTextField.getText();
        String phoneNo = phoneNoTextField.getText();
        int zipCode = Integer.parseInt(zipTextField.getText());

        int rowsAffected = db.insertCompany(cvrNo,address,name,mail,phoneNo,zipCode);

        if (rowsAffected==1){

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

    @FXML
    public void inputValidator(KeyEvent event) {

        InputValidation inputValidation = new InputValidation();

        inputValidation.checkInputCompany(phoneNoTextField, zipTextField, event);


    }





}
