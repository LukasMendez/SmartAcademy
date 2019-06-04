package BusinessServices;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Created by Alex 03-06-2019 time: 12:09 .
 */
public class InputValidation {


    public void checkInputEmployee(TextField nameTF, TextField cprNo, TextField phoneNo, KeyEvent event) {


        if (event.getSource() == nameTF) {

            checkIfRedName(nameTF);

        } else if (event.getSource() == phoneNo) {

            redFieldNumber(phoneNo, event);

        } else if (event.getSource() == cprNo) {

            redFieldCPRNoAndZip(cprNo, event);

        }

    }

    public void checkInputCompany(TextField phoneNo, TextField zip, KeyEvent event) {

        if (event.getSource() == phoneNo) {
            redFieldNumber(phoneNo, event);
        } else if (event.getSource() == zip) {
            redFieldCPRNoAndZip(zip, event);
        }
    }


    private void checkIfRedName(TextField nameTextField) {

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                nameTextField.setText(newValue.replaceAll("[^\\sa-zA-Z]", "")); // Allows only wirte a letters!
            }
        });

    }

@SuppressWarnings("Duplicates")
    private void redFieldNumber(TextField phoneNumTextField, KeyEvent event) {

        char firstLetterCheck = 0;

        char asciiTableNumber = 43;


        if (((int) event.getCharacter().charAt(firstLetterCheck)) != asciiTableNumber && !Character.isDigit(event.getCharacter().charAt(firstLetterCheck))) {

            event.consume();
            phoneNumTextField.setStyle("-fx-text-box-border:red;-fx-control-inner-background:red;-fx-faint-focus-color:red;");

        } else {
            phoneNumTextField.setStyle("-fx-text-box-border:#feefff;-fx-control-inner-background:white;-fx-faint-focus-color:white;");

        }

    }

    @SuppressWarnings("Duplicates")
    private void redFieldCPRNoAndZip(TextField cprTextField, KeyEvent event) {

        char firstLetterCheck = 0;

        char cPRNAndZipumberLenght = 9;


        if (cprTextField.getLength() <= cPRNAndZipumberLenght) {

            if (((int) event.getCharacter().charAt(firstLetterCheck)) != cPRNAndZipumberLenght && !Character.isDigit(event.getCharacter().charAt(firstLetterCheck))) {

                event.consume();

                cprTextField.setStyle("-fx-text-box-border:red;-fx-control-inner-background:red;-fx-faint-focus-color:red;");
            } else {
                cprTextField.setStyle("-fx-text-box-border:#feefff;-fx-control-inner-background:white;-fx-faint-focus-color:white;");
            }


        } else if (cprTextField.getLength() > cPRNAndZipumberLenght) {


            event.consume();

            cprTextField.setStyle("-fx-text-box-border:#feefff;-fx-control-inner-background:white;-fx-faint-focus-color:white;");
        }
    }


}
