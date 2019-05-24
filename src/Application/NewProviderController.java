package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 24-05-2019.
 */
public class NewProviderController implements Openable {

    private Stage newProviderStage = new Stage();

    @Override
    public void openWindow() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddProviderWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newProviderStage.setTitle("Add New Provider");
            newProviderStage.setScene(new Scene(root));
            newProviderStage.setResizable(false);
            newProviderStage.show();


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public boolean isStageOpen() {
        return newProviderStage.isShowing();
    }
}
