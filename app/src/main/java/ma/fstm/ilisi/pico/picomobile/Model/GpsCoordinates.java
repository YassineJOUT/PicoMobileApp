package ma.fstm.ilisi.pico.picomobile.Model;

import android.support.annotation.NonNull;

public class GpsCoordinates {
    @NonNull
    private double latitude ;
    @NonNull
    private double longitude ;


    public GpsCoordinates(@NonNull int latitude, @NonNull int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull double latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull double longitude) {
        this.longitude = longitude;
    }
}
