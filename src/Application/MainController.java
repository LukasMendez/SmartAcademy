package Application;

import Persistance.DB;

public class MainController {

    private DB db = DB.getInstance();

    public void start(){
        db.connect();
        //db.test();
    }


}
