package Application;

import Persistance.DB;

public class MainController {

    private DB db = DB.getInstance();
    private CourseToEPController courseToEPController = new CourseToEPController();



    public void start(){
        //db.connect();
        //db.test();
    }


    public void initialize(){

    }


}
