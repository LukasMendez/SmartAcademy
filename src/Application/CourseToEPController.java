package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class CourseToEPController implements Openable {

    private Stage courseToEducationPlanStage = new Stage();
    private FXMLLoader fxmlLoader;

    // Instance of itself. Used for regaining access to the instance from the MainController.
    private CourseToEPController courseToEPController;

    @Override
    public void openWindow() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCourseToEPWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            courseToEducationPlanStage.setTitle("Add Course To Education Plan");
            courseToEducationPlanStage.setScene(new Scene(root));
            courseToEducationPlanStage.setResizable(false);
            courseToEducationPlanStage.show();
        } catch (Exception e) {

            e.printStackTrace();
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
