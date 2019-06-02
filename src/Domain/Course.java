package Domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class Course {

    private IntegerProperty courseID;
    private StringProperty courseNumber;
    private StringProperty information;
    private StringProperty additionalInformation;
    private IntegerProperty numberOfDays;
    private StringProperty location;
    private StringProperty CVRNumber;


    public Course(int courseID, String courseNumber, String information, String additionalInformation, int numberOfDays, String location, String CVRNumber){
        this.setCourseID(courseID);
        this.setCourseNumber(courseNumber);
        this.setInformation(information);
        this.setAdditionalInformation(additionalInformation);
        this.setNumberOfDays(numberOfDays);
        this.setLocation(location);
        this.setCVRNumber(CVRNumber);
    }

    public void setCourseID(int value) { courseIDProperty().set(value); }
    public int getCourseID() { return courseIDProperty().get(); }
    public IntegerProperty courseIDProperty() {
        if (courseID == null) courseID = new SimpleIntegerProperty(this, "courseID");
        return courseID;
    }


    public void setCourseNumber(String value) { courseNumberProperty().set(value); }
    public String getCourseNumber() { return courseNumberProperty().get(); }
    public StringProperty courseNumberProperty() {
        if (courseNumber == null) courseNumber = new SimpleStringProperty(this, "courseNumber");
        return courseNumber;
    }

    public void setInformation(String value) { informationProperty().set(value); }
    public String getInformation() { return informationProperty().get(); }
    public StringProperty informationProperty() {
        if (information == null) information = new SimpleStringProperty(this, "information");
        return information;
    }

    public void setAdditionalInformation(String value) { additionalInformationProperty().set(value); }
    public String getAdditionalInformation() { return additionalInformationProperty().get(); }
    public StringProperty additionalInformationProperty() {
        if (additionalInformation == null) additionalInformation = new SimpleStringProperty(this, "additionalInformation");
        return additionalInformation;
    }

    public void setNumberOfDays(int value) { numberOfDaysProperty().set(value); }
    public int getNumberOfDays() { return numberOfDaysProperty().get(); }
    public IntegerProperty numberOfDaysProperty() {
        if (numberOfDays == null) numberOfDays = new SimpleIntegerProperty(this, "numberOfDays");
        return numberOfDays;
    }

    public void setLocation(String value) { locationProperty().set(value); }
    public String getLocation() { return locationProperty().get(); }
    public StringProperty locationProperty() {
        if (location == null) location = new SimpleStringProperty(this, "location");
        return location;
    }

    public void setCVRNumber(String value) { CVRNumberProperty().set(value); }
    public String getCVRNumber() { return CVRNumberProperty().get(); }
    public StringProperty CVRNumberProperty() {
        if (CVRNumber == null) CVRNumber = new SimpleStringProperty(this, "CVRNumber");
        return CVRNumber;
    }
/*
    public String toString(){
        return "" + courseNumber + " " + information + " " + additionalInformation + " " + numberOfDays + " " + locationID + " " + CVRNumber;
    }*/
}
