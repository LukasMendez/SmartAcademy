package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 24-05-2019.
 */
public class NewCompanyController implements Openable {

    private Stage newCompanyStage = new Stage();

    @Override
    public void openWindow() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCompanyWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newCompanyStage.setTitle("Add New Company");
            newCompanyStage.setScene(new Scene(root));
            newCompanyStage.setResizable(false);
            newCompanyStage.show();
        } catch (Exception e) {

            e.printStackTrace();
        }



    }

    @Override
    public boolean isStageOpen() {
        return newCompanyStage.isShowing();
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
