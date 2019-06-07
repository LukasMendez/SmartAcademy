package Domain;

public class Location {

    private int locationID;
    private String name;
    private String address;
    private int zip;

    public Location(int locationID, String name, String address, int zip){

        this.locationID=locationID;
        this.name=name;
        this.address=address;
        this.zip=zip;

    }

    public int getLocationID() {
        return locationID;
    }

    public int getZip() {
        return zip;
    }

    @Override
    public String toString(){
        return zip + ", " + name + ", " + address;
    }

}
