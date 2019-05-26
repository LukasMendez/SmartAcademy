package Application;

import javafx.stage.Stage;

/**
 * Created by Lukas
 * 23-05-2019.
 */
public interface Openable {

    void openWindow();

    boolean isStageOpen();

    Object getController();

    Stage getStage();

}
