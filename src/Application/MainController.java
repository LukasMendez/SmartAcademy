package Application;

import Persistance.DB;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

public class MainController {

    private DB db = DB.getInstance();

    @FXML
    private TableView educationMatrixTableView;

    public void initialize() {


        for (int i = 0; i < 10; i++) {




        }

    }




    public void start() {
        db.connect();
        //db.test();
    }


}
