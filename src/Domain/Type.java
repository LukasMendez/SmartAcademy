package Domain;


public class Type {

    private int typeID;
    private String type;


    public Type(int typeID, String type) {
        this.typeID = typeID;
        this.type = type;
    }

    public int getTypeID() {
        return typeID;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString(){
        return type;
    }

}
