package Domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class Company {

    private StringProperty name;
    private StringProperty address;
    private IntegerProperty zip;
    private StringProperty email;
    private StringProperty phoneNumber;
    private StringProperty CVRNumber;

    public Company(String name, String address, int zip, String email, String phoneNumber, String CVRNumber){
        this.setName(name);
        this.setAddress(address);
        this.setZip(zip);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setCVRNumber(CVRNumber);
    }

    public void setName(String value) { nameProperty().set(value); }
    public String getName() { return nameProperty().get(); }
    public StringProperty nameProperty() {
        if (name == null) name = new SimpleStringProperty(this, "name");
        return name;
    }

    public void setAddress(String value) { addressProperty().set(value); }
    public String getAddress() { return addressProperty().get(); }
    public StringProperty addressProperty() {
        if (address == null) address = new SimpleStringProperty(this, "address");
        return address;
    }

    public void setZip(int value) { zipProperty().set(value); }
    public int getZip() { return zipProperty().get(); }
    public IntegerProperty zipProperty() {
        if (zip == null) zip = new SimpleIntegerProperty(this, "zip");
        return zip;
    }

    public void setEmail(String value) { emailProperty().set(value); }
    public String getEmail() { return emailProperty().get(); }
    public StringProperty emailProperty() {
        if (email == null) email = new SimpleStringProperty(this, "email");
        return email;
    }

    public void setPhoneNumber(String value) { phoneNumberProperty().set(value); }
    public String getPhoneNumber() { return phoneNumberProperty().get(); }
    public StringProperty phoneNumberProperty() {
        if (phoneNumber == null) phoneNumber = new SimpleStringProperty(this, "phoneNumber");
        return phoneNumber;
    }

    public void setCVRNumber(String value) { CVRNumberProperty().set(value); }
    public String getCVRNumber() { return CVRNumberProperty().get(); }
    public StringProperty CVRNumberProperty() {
        if (CVRNumber == null) CVRNumber = new SimpleStringProperty(this, "CVRNumber");
        return CVRNumber;
    }

    public String toString(){
        return getName();
    }

}
