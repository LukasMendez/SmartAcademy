package Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class NewCourseController implements Openable{

    // Static because we want to make sure to always have access to the same (and only) stage
    private static Stage addCourseStage = new Stage();
    private FXMLLoader fxmlLoader;

    // TextFields
    @FXML
    private TextField amuNoTextField, cvrNoTextField, infoTextField, locationTextField, noOfDaysTextField, additionalInfoTextField;




    @Override
    public void openWindow() {


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCourseWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            addCourseStage.setTitle("ABC");
            addCourseStage.setScene(new Scene(root));
            addCourseStage.show();
        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    @Override
    public boolean isStageOpen(){

        return addCourseStage.isShowing();
    }

    @Override
    public Stage getStage() {
        return addCourseStage;
    }

    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }


    public void addNewCourse(){


        // TODO Add new course to database

    }






}
