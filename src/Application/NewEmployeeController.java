package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class NewEmployeeController implements Openable {

    private Stage newEmployeeStage = new Stage();

    @Override
    public void openWindow() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddEmployeeWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newEmployeeStage.setTitle("Add New Employee");
            newEmployeeStage.setScene(new Scene(root));
            newEmployeeStage.setResizable(false);
            newEmployeeStage.show();
        } catch (Exception e) {

            e.printStackTrace();
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
}
