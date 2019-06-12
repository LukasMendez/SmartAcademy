package BusinessServices;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;


public class InputValidation {


    /**
     * This method checks the input in Employee window, its checking what its get, like eg. in nameTF, cprNo, phoneNo.
     * Its connected to the other method, and its checking if is number or its a letters. Every TextFields connected to buttons on Key Typed.
     */
    public void checkInputEmployee(TextField nameTF, TextField cprNo, TextField phoneNo, KeyEvent event) {

        if (event.getSource() == nameTF) {
            checkIfRedName(nameTF, event);

        } else if (event.getSource() == phoneNo) {
            redFieldNumber(phoneNo, event);

        } else if (event.getSource() == cprNo) {
            redFieldCPRNoAndZip(cprNo, event);

        }
    }

    /**
     * In this method we checking input in textfield Phone number, Zip code. User can write only number and plus in this TextField.
     */
    public void checkInputCompany(TextField phoneNo, TextField zip, TextField name, KeyEvent event) {
        if (event.getSource() == phoneNo) {
            redFieldNumber(phoneNo, event);
        } else if (event.getSource() == zip) {
            redFieldCPRNoAndZip(zip, event);
        } else if (event.getSource() == name) {
            checkIfRedName(name, event);
        }
    }

    /**
     * In this method we checking only letters in TextField, here is available only letters otherwise will be TextField change color to red.
     * Regular expressions looking for string input and its gives permission only for letters, from a to z.
     */
    private void checkIfRedName(TextField nameTextField, KeyEvent event) {

        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Enter the name ");
        nameTextField.setTooltip(tooltip);

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                nameTextField.setText(newValue.replaceAll("[^\\sa-zA-Z]", "")); // Allows only wirte a letters!
            }
        });


    }


    /**
     * In this method we checking input only numbers available from ascii table only and plus and numbers from 0 to 9. If input not a number
     * will it be red TextField and user cant write in. An event which indicates that a keystroke occurred in a component. hen a key is pressed, released, or typed.
     */
    private void redFieldNumber(TextField phoneNumTextField, KeyEvent event) {

        char firstLetterCheck = 0;

        char plusSymbolASCII = 43;
        char backSlashASCII = 8;
        char deleteASCII = 127;

        boolean backSlash = (int) event.getCharacter().charAt(firstLetterCheck) != backSlashASCII;
        boolean deleteButton = (int) event.getCharacter().charAt(firstLetterCheck) != deleteASCII;

        if (phoneNumTextField.getLength()==0 && event.getCharacter().charAt(firstLetterCheck)!=plusSymbolASCII){
            event.consume();
        }

        if (phoneNumTextField.getLength() <= plusSymbolASCII) {
            if (backSlash && deleteButton && !Character.isDigit(event.getCharacter().charAt(firstLetterCheck)) && phoneNumTextField.getLength() > 0) {
                event.consume();
                phoneNumTextField.setStyle("-fx-text-box-border:#ff2000;-fx-control-inner-background:red;-fx-faint-focus-color:red;");
            } else {
                phoneNumTextField.setStyle("-fx-text-box-border:#feefff;-fx-control-inner-background:white;-fx-faint-focus-color:white;");
            }
        }
    }

    /**
     * This method do, its check rang of length and users input number or letter, if its letter the TextField will be red, if input right its number it will normal color.
     */

    private void redFieldCPRNoAndZip(TextField cprTextField, KeyEvent event) {

        char firstLetterCheck = 0;

        char backSlashASCII = 8;
        char deleteASCII = 127;

        int maxLength = 9;

        boolean backSlash = (int) event.getCharacter().charAt(firstLetterCheck) != backSlashASCII;
        boolean deleteButton = (int) event.getCharacter().charAt(firstLetterCheck) != deleteASCII;


        if (cprTextField.getLength() <= maxLength) {

            if (backSlash && deleteButton && !Character.isDigit(event.getCharacter().charAt(firstLetterCheck)) && cprTextField.getLength() >= 0) {

                event.consume();

                cprTextField.setStyle("-fx-text-box-border:red;-fx-control-inner-background:red;-fx-faint-focus-color:red;");
            } else {
                cprTextField.setStyle("-fx-text-box-border:#feefff;-fx-control-inner-background:white;-fx-faint-focus-color:white;");
            }

        } else if (cprTextField.getLength() > maxLength) {
            event.consume();
            cprTextField.setStyle("-fx-text-box-border:#feefff;-fx-control-inner-background:white;-fx-faint-focus-color:white;");
        }
    }


}
