package Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Earchquake {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private int date;
    private String dateToKey;
    private SimpleStringProperty dis_Date = new SimpleStringProperty();
    private SimpleDoubleProperty latitude = new SimpleDoubleProperty();
    private SimpleDoubleProperty longitude = new SimpleDoubleProperty();
    private SimpleIntegerProperty depth = new SimpleIntegerProperty();
    private SimpleDoubleProperty magnitude = new SimpleDoubleProperty();
    private SimpleStringProperty region = new SimpleStringProperty();

    public String getDateToKey() {
        return dateToKey;
    }

    public void setDateToKey(String dateToKey) {
        this.dateToKey = dateToKey;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDis_Date() {
        return dis_Date.get();
    }

    public SimpleStringProperty dis_DateProperty() {
        return dis_Date;
    }

    public void setDis_Date(String dis_Date) {
        dis_Date = dis_Date.replace("\"", "");
        setDateToKey(dis_Date.substring(0, 10));
        setDate(Integer.parseInt(dis_Date.replace("-", "").substring(0, 8)));
        this.dis_Date.set(dis_Date);
    }

    public double getLatitude() {
        return latitude.get();
    }

    public SimpleDoubleProperty latitudeProperty() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude.set(latitude);
    }

    public double getLongitude() {
        return longitude.get();
    }

    public SimpleDoubleProperty longitudeProperty() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude.set(longitude);
    }

    public int getDepth() {
        return depth.get();
    }

    public SimpleIntegerProperty depthProperty() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth.set(depth);
    }

    public double getMagnitude() {
        return magnitude.get();
    }

    public SimpleDoubleProperty magnitudeProperty() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude.set(magnitude);
    }

    public String getRegion() {
        return region.get();
    }

    public SimpleStringProperty regionProperty() {
        return region;
    }

    public void setRegion(String region) {
        region = region.replace("\"", "");
        this.region.set(region);
    }
}
