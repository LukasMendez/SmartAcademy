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
public class NewCompanyController implements Openable {

    private static Stage newCompanyStage = new Stage();
    private FXMLLoader fxmlLoader;

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCompanyWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            newCompanyStage.setTitle("Add New Company");
            newCompanyStage.setScene(new Scene(root));
            newCompanyStage.setResizable(false);

            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            newCompanyStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupModality(){
        if (!isInitialized){
            newCompanyStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized=true;
        }
    }



    @Override
    public boolean isStageOpen() {
        return newCompanyStage.isShowing();
    }

    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    @Override
    public Stage getStage() {
        return newCompanyStage;
    }





}
