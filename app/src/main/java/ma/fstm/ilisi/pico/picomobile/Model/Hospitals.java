package ma.fstm.ilisi.pico.picomobile.Model;

public class Hospitals {

    private String _id;
    private String name;
    private GpsCoordinates coordinates;

    public Hospitals(String _id, String name, GpsCoordinates coordinates) {
        this._id = _id;
        this.name = name;
        this.coordinates = coordinates;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GpsCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GpsCoordinates coordinates) {
        this.coordinates = coordinates;
    }
}
