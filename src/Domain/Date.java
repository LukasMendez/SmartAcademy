package Domain;

import java.time.format.DateTimeFormatter;

public class Date {

    private String date;
    private int dateID;

    public Date(String date, int dateID) {
        this.date = date;
        this.dateID = dateID;
    }

    public String getDate() {
        return date;
    }

    public int getDateID() {
        return dateID;
    }

    @Override
    public String toString(){
        return date;
    }

}
