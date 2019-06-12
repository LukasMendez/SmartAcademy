package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXML/MainWindow.fxml"));
        primaryStage.setTitle("SmartAcademy Manager");
        primaryStage.getIcons().add(new Image("UI/Images/iconPen.png"));
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();

    }




}
