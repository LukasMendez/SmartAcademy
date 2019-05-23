package Domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class Employee {

    private StringProperty name;
    private StringProperty CPRNumber;
    private StringProperty email;
    private StringProperty phoneNumber;
    private StringProperty company;

    public Employee(String name, String CPRNumber, String email, String phoneNumber, String company){
        this.setName(name);
        this.setCPRNumber(CPRNumber);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setCompany(company);
    }

    public void setName(String value) { nameProperty().set(value); }
    public String getName() { return nameProperty().get(); }
    public StringProperty nameProperty() {
        if (name == null) name = new SimpleStringProperty(this, "name");
        return name;
    }

    public void setCPRNumber(String value) { CPRNumberProperty().set(value); }
    public String getCPRNumber() { return CPRNumberProperty().get(); }
    public StringProperty CPRNumberProperty() {
        if (CPRNumber == null) CPRNumber = new SimpleStringProperty(this, "CPRNumber");
        return CPRNumber;
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

    public void setCompany(String value) { companyProperty().set(value); }
    public String getCompany() { return companyProperty().get(); }
    public StringProperty companyProperty() {
        if (company == null) company = new SimpleStringProperty(this, "company");
        return company;
    }
}
