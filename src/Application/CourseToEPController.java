package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class CourseToEPController implements Openable {

    private Stage courseToEducationPlanStage = new Stage();
    private FXMLLoader fxmlLoader;

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    // Instance of itself. Used for regaining access to the instance from the MainController.
    private CourseToEPController courseToEPController;

    @Override
    public void openWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCourseToEPWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            courseToEducationPlanStage.setTitle("Add Course To Education Plan");
            courseToEducationPlanStage.getIcons().add(new Image("UI/Images/clipboard.png"));
            courseToEducationPlanStage.setScene(new Scene(root));
            courseToEducationPlanStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            courseToEducationPlanStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupModality(){
        if (!isInitialized){
            courseToEducationPlanStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized=true;
        }
    }

    @Override
    public boolean isStageOpen() {
        return courseToEducationPlanStage.isShowing();
    }

    @Override
    public Object getController() {
        return null;
    }

    @Override
    public Stage getStage() {
        return null;
    }
}
