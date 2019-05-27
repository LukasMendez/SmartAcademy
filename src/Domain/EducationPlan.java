package Domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class EducationPlan {
    private IntegerProperty dateID;
    private StringProperty date;
    private StringProperty information;
    private StringProperty provider;
    private StringProperty location;
    private IntegerProperty priority;
    private IntegerProperty planID;
    private IntegerProperty isActive;

    public EducationPlan(int dateID, String date, String information, String provider, String location, int priority, int planID, int isActive){
        this.setDateID(dateID);
        this.setDate(date);
        this.setInformation(information);
        this.setProvider(provider);
        this.setLocation(location);
        this.setPriority(priority);
        this.setPlanID(planID);
        this.setIsActive(isActive);
    }

    public void setDateID(int value) { dateIDProperty().set(value); }
    public int getDateID() { return dateIDProperty().get(); }
    public IntegerProperty dateIDProperty() {
        if (dateID == null) dateID = new SimpleIntegerProperty(this, "dateID");
        return dateID;
    }

    public void setDate(String value) { dateProperty().set(value); }
    public String getDate() { return dateProperty().get(); }
    public StringProperty dateProperty() {
        if (date == null) date = new SimpleStringProperty(this, "date");
        return date;
    }

    public void setInformation(String value) { informationProperty().set(value); }
    public String getInformation() { return informationProperty().get(); }
    public StringProperty informationProperty() {
        if (information == null) information = new SimpleStringProperty(this, "information");
        return information;
    }

    public void setProvider(String value) { providerProperty().set(value); }
    public String getProvider() { return providerProperty().get(); }
    public StringProperty providerProperty() {
        if (provider == null) provider = new SimpleStringProperty(this, "provider");
        return provider;
    }

    public void setLocation(String value) { locationProperty().set(value); }
    public String getLocation() { return locationProperty().get(); }
    public StringProperty locationProperty() {
        if (location == null) location = new SimpleStringProperty(this, "location");
        return location;
    }

    public void setPriority(int value) { priorityProperty().set(value); }
    public int getPriority() { return priorityProperty().get(); }
    public IntegerProperty priorityProperty() {
        if (priority == null) priority = new SimpleIntegerProperty(this, "priority");
        return priority;
    }

    public void setPlanID(int value) { planIDProperty().set(value); }
    public int getPlanID() { return planIDProperty().get(); }
    public IntegerProperty planIDProperty() {
        if (planID == null) planID = new SimpleIntegerProperty(this, "planID");
        return planID;
    }

    public void setIsActive(int value) { isActiveProperty().set(value); }
    public int getIsActive() { return isActiveProperty().get(); }
    public IntegerProperty isActiveProperty() {
        if (isActive == null) isActive = new SimpleIntegerProperty(this, "isActive");
        return isActive;
    }
}
