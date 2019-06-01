package Domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CourseByPeriod {

    private StringProperty information;
    private StringProperty provider;
    private StringProperty location;
    private StringProperty period;
    private IntegerProperty periodID;

    public CourseByPeriod(String information, String provider, String location, String period, int periodID){
        this.setInformation(information);
        this.setProvider(provider);
        this.setLocation(location);
        this.setPeriod(period);
        this.setPeriodID(periodID);
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

    public void setPeriod(String value) { periodProperty().set(value); }
    public String getPeriod() { return periodProperty().get(); }
    public StringProperty periodProperty() {
        if (period == null) period = new SimpleStringProperty(this, "period");
        return period;
    }

    public void setPeriodID(int value) { periodIDProperty().set(value); }
    public int getPeriodID() { return periodIDProperty().get(); }
    public IntegerProperty periodIDProperty() {
        if (periodID == null) periodID = new SimpleIntegerProperty(this, "periodID");
        return periodID;
    }
/*
    public String toString(){
        return "" + information + " " + provider + " " + location + " " + period + " " + periodID;
    }*/
}
