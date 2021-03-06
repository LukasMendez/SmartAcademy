import BusinessServices.InputValidation;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class InputValidationTest {


    public TextField textField;
    public InputValidation instance;

    @Before
    public void setUp() throws Exception {
        instance = new InputValidation();

        // Easiest way to prevent: 'Toolkit not initialized' exception when unit-testing an JavaFX application
        new JFXPanel();

        textField = new TextField(""){
            {
                // Necessary in order to read data from the text field
                setSkin(createDefaultSkin());
            }
        };
        // To assure that we are typing on the right text field
        textField.requestFocus();

        // Initializing the event handler for the text field
        textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // Order: Name, CPR-number and Phone Number. We are currently testing the CPR Number text field
                instance.checkInputEmployee(new TextField(), textField, new TextField(), event);
            }
        });

    }

    @Test
    public void checkInputEmployee() throws Exception {

        // Amount of times we write the character
        for (int i = 0; i < 20; i++) {
            textField.fireEvent(new KeyEvent(KeyEvent.KEY_TYPED, "5", "5",
                    KeyCode.DIGIT5, false, false, false, false));
        }

        int expected = 10;
        int actual = textField.getLength();

        assertEquals(expected, actual);

    }

}