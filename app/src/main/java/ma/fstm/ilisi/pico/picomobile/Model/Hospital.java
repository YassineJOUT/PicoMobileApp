package ma.fstm.ilisi.pico.picomobile.Model;
/**
 * Hospital is a business logic class
 * This class contains all information about a hospital
 *
 * @author      Yassine jout
 * @version     1.0
 */
public class Hospital {
    /**
     * @param _id hospital's ID
     */

    private String _id;
    /**
     * @param name the name of a hospital
     */
    private String name;
    /**
     * @param coordinates object containing the latitude and longitude
     */
    private GpsCoordinates coordinates;
    /**
     * Hospital class constructor
     * @param _id Hospital ID
     * @param name Hospital name
     * @param coordinates hospital latitude and longitude coordinates
     */
    public Hospital(String _id, String name, GpsCoordinates coordinates) {
        this._id = _id;
        this.name = name;
        this.coordinates = coordinates;
    }

    /**
     * Hospital class constructor
     * @param _id Hospital ID
     * @param name Hospital name
     * @param latitude hospital latitude
     * @param longitude hospital longitude
     */
    public Hospital(String _id, String name, double latitude, double longitude) {
        this._id = _id;
        this.name = name;
        this.coordinates = new GpsCoordinates(latitude,longitude);
    }
    /**
     * hospital ID getter
     * @return the id of the hospital
     */
    public String get_id() {
        return _id;
    }
    /**
     * hospital ID setter
     * @param _id the new hospital ID
     */
    public void set_id(String _id) {
        this._id = _id;
    }
    /**
     * hospital name getter
     * @return the name of hospital
     */
    public String getName() {
        return name;
    }
    /**
     * hospital name setter
     * @return the name of the hospital
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * hospital gsp coordinates getter
     * @return GpsCoordinates object
     */
    public GpsCoordinates getCoordinates() {
        return coordinates;
    }
    /**
     * hospital gsp coordinates setter
     * @param coordinates GpsCoordinates object of hospital location
     */
    public void setCoordinates(GpsCoordinates coordinates) {
        this.coordinates = coordinates;
    }
}
