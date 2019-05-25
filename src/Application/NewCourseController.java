package Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class NewCourseController implements Openable{


    private Stage addCourseStage = new Stage();


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


    public boolean isStageOpen(){

        return addCourseStage.isShowing();
    }

    @Override
    public Object getController() {
        return null;
    }


}
