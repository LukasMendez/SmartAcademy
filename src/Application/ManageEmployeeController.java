package Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class ManageEmployeeController implements Openable{

    private Stage manageEmployeeStage = new Stage();

    // Controller (Window)
    private CourseToEPController courseToEPController = new CourseToEPController();

    @Override
    public void openWindow() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\ManageEmployeeWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            manageEmployeeStage.setTitle("Manage Selected Employee");
            manageEmployeeStage.setScene(new Scene(root));
            manageEmployeeStage.setResizable(false);
            manageEmployeeStage.show();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public boolean isStageOpen() {
        return manageEmployeeStage.isShowing();
    }


    @FXML
    public void addCourseToEducationPlan(){

        if (!courseToEPController.isStageOpen()){

            courseToEPController.openWindow();

        } else {

            System.out.println("Window is already open");

        }


    }



}
