package Application;

import Domain.CourseByPeriod;
import Persistance.DB;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class CourseToEPController implements Openable {

    private Stage courseToEducationPlanStage = new Stage();
    private FXMLLoader fxmlLoader;
    private DB db = DB.getInstance();

    @FXML
    private TableView addCourseToEPTableView;
    @FXML
    private TableColumn informationColumn, providerColumn, locationColumn, periodColumn;

    // Used to check if the stage has been initialized before. Used for setting Modality
    private static boolean isInitialized = false;

    private ObservableList<CourseByPeriod> coursesByPeriodList;
    private CourseByPeriod selectedCourse;

    // Instance of itself. Used for regaining access to the instance from the MainController.
    private CourseToEPController courseToEPController;

    @Override
    public void openWindow() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("..\\UI\\AddCourseToEPWindow.fxml"));
            Parent root = fxmlLoader.load();
            courseToEducationPlanStage.setTitle("Add Course To Education Plan");
            courseToEducationPlanStage.getIcons().add(new Image("UI/Images/clipboard.png"));
            courseToEducationPlanStage.setScene(new Scene(root));
            courseToEducationPlanStage.setResizable(false);



            // You may only run .initModality once. Therefore we need to check if the window has been opened before
            setupModality();

            courseToEducationPlanStage.show();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupModality(){
        if (!isInitialized){
            courseToEducationPlanStage.initModality(Modality.APPLICATION_MODAL);
            isInitialized=true;
        }
    }

    public void start(){
        //CoursesByPeriod
        //constructing data model + data binding
        updateAddCourseToEPTableView();
        //splitting out the data in the model
        informationColumn.setCellValueFactory(new PropertyValueFactory("information"));
        providerColumn.setCellValueFactory(new PropertyValueFactory("provider"));
        locationColumn.setCellValueFactory(new PropertyValueFactory("location"));
        periodColumn.setCellValueFactory(new PropertyValueFactory("period"));
        //representing the data in the columns
        addCourseToEPTableView.getColumns().setAll(informationColumn, providerColumn, locationColumn, periodColumn);
    }

    private void updateAddCourseToEPTableView() {
        //constructing data model
        coursesByPeriodList = db.getCoursesByPeriod();
        //data binding
        addCourseToEPTableView.setItems(coursesByPeriodList);
    }

    @FXML
    private void setSelectedCourse(){
        //storing the selected course
        if(addCourseToEPTableView.getSelectionModel().getSelectedItem() != null){
            selectedCourse = (CourseByPeriod)addCourseToEPTableView.getSelectionModel().getSelectedItem();
            //closing the window through a detour because javaFX
            Stage stage = getStage();
            stage.close();
        }
    }

    public CourseByPeriod getSelectedCourse(){
        return selectedCourse;
    }

    @Override
    public boolean isStageOpen() {
        return courseToEducationPlanStage.isShowing();
    }

    @Override
    public Object getController() {
        return fxmlLoader.getController();
    }

    @Override
    public Stage getStage() {
        return (Stage)addCourseToEPTableView.getScene().getWindow();
    }
}
