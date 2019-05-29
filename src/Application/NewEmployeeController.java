package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class NewEmployeeController implements Openable {

    private static Stage newEmployeeStage = new Stage();

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    @Override
    public void openWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddEmployeeWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newEmployeeStage.setTitle("Add New Employee");
            newEmployeeStage.setScene(new Scene(root));
            newEmployeeStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            newEmployeeStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupModality(){
        if (!isInitialized){
            newEmployeeStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized=true;
        }
    }

    @Override
    public boolean isStageOpen() {
        return newEmployeeStage.isShowing();
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
