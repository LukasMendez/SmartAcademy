package Domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class Qualification {



    private int qualificationID;
    private StringProperty type;
    private StringProperty description;
    private StringProperty level;
    private IntegerProperty employeeID;

    public Qualification(int qualificationID, String type, String description, String level, int employeeID){
        this.qualificationID=qualificationID;
        this.setType(type);
        this.setDescription(description);
        this.setLevel(level);
        this.setEmployeeID(employeeID);
    }

    public void setType(String value) { typeProperty().set(value); }
    public String getType() { return typeProperty().get(); }
    public StringProperty typeProperty() {
        if (type == null) type = new SimpleStringProperty(this, "type");
        return type;
    }

    public void setDescription(String value) { descriptionProperty().set(value); }
    public String getDescription() { return descriptionProperty().get(); }
    public StringProperty descriptionProperty() {
        if (description == null) description = new SimpleStringProperty(this, "description");
        return description;
    }

    public void setLevel(String value) { levelProperty().set(value); }
    public String getLevel() { return levelProperty().get(); }
    public StringProperty levelProperty() {
        if (level == null) level = new SimpleStringProperty(this, "level");
        return level;
    }

    public void setEmployeeID(int value) { employeeIDProperty().set(value); }
    public int getEmployeeID() { return employeeIDProperty().get(); }
    public IntegerProperty employeeIDProperty() {
        if (employeeID == null) employeeID = new SimpleIntegerProperty(this, "employeeID");
        return employeeID;
    }

    public int getQualificationID() {
        return qualificationID;
    }

}
