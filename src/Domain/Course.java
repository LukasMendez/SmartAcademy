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

    private IntegerProperty courseNumber;
    private StringProperty information;
    private StringProperty additionalInformation;
    private IntegerProperty numberOfDays;
    private IntegerProperty locationID;
    private StringProperty CVRNumber;

    public Course(int courseNumber, String information, String additionalInformation, int numberOfDays, int locationID, String CVRNumber){
        this.setCourseNumber(courseNumber);
        this.setInformation(information);
        this.setAdditionalInformation(additionalInformation);
        this.setNumberOfDays(numberOfDays);
        this.setLocationID(locationID);
        this.setCVRNumber(CVRNumber);
    }

    public void setCourseNumber(int value) { courseNumberProperty().set(value); }

    public int getCourseNumber() { return courseNumberProperty().get(); }
    public IntegerProperty courseNumberProperty() {
        if (courseNumber == null) courseNumber = new SimpleIntegerProperty(this, "courseNumber");
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

    public void setLocationID(int value) { locationIDProperty().set(value); }
    public int getLocationID() { return locationIDProperty().get(); }
    public IntegerProperty locationIDProperty() {
        if (locationID == null) locationID = new SimpleIntegerProperty(this, "locationID");
        return locationID;
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
