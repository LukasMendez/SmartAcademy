package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 24-05-2019.
 */
public class NewProviderController implements Openable {

    private static Stage newProviderStage = new Stage();

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    @Override
    public void openWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddProviderWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newProviderStage.setTitle("Add New Provider");
            newProviderStage.setScene(new Scene(root));
            newProviderStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            newProviderStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupModality() {
        if (!isInitialized) {
            newProviderStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized = true;
        }
    }

    @Override
    public boolean isStageOpen() {
        return newProviderStage.isShowing();
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
