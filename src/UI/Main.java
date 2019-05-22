package UI;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import Application.MainController;

import java.time.LocalDate;


public class Main extends Application {

    MainController c = new MainController();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();

        MainController c = new MainController();

        c.start();


        // TESTING CALENDAR
/*
        Stage testStage = new Stage();
        BorderPane testBorderPane = new BorderPane();
        testStage.setTitle("Test Window");
        testStage.setScene(new Scene(testBorderPane,500,400));

        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();

        testBorderPane.setCenter(popupContent);

        testStage.show();
*/

    }


}
