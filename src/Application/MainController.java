package Application;

import Domain.Course;
import Persistance.DB;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    private DB db = DB.getInstance();
    private CourseToEPController courseToEPController = new CourseToEPController();


    @FXML
    private TableView educationMatrixTableView;

    @FXML
    private TableView courseTable;

    @FXML
    private TableColumn courseNumberColumn, informationColumn, additionalInformationColumn, numberOfDaysColumn, locationIDColumn, CVRNumberColumn;

    private ObservableList<Course> courseList;




    public void initialize() {

        educationMatrixTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        for (int i = 0; i < 10; i++) {
            educationMatrixTableView.getColumns().addAll(new TableColumn("Test no."+i));
        }

        //Displaying Courses
        //constructing data model
        courseList = DB.testGetCourseList();
        //data binding
        courseTable.setItems(courseList);
        //splitting out the data in the model
        courseNumberColumn.setCellValueFactory(new PropertyValueFactory("courseNumber"));
        informationColumn.setCellValueFactory(new PropertyValueFactory("information"));
        additionalInformationColumn.setCellValueFactory(new PropertyValueFactory("additionalInformation"));
        numberOfDaysColumn.setCellValueFactory(new PropertyValueFactory("numberOfDays"));
        locationIDColumn.setCellValueFactory(new PropertyValueFactory("locationID"));
        CVRNumberColumn.setCellValueFactory(new PropertyValueFactory("CVRNumber"));
        //representing the data in the columns
        courseTable.getColumns().setAll(courseNumberColumn, informationColumn, additionalInformationColumn, numberOfDaysColumn, locationIDColumn, CVRNumberColumn);
    }

    //Test method
    @FXML
    public void testAddCourse(){
        courseList.add(new Course(156,"bloop","bleep",8,1,"87564321"));
    }

    public void start() {

    }


}
