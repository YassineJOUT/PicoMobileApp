package ma.fstm.ilisi.pico.picomobile.Model;

import android.media.Image;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

public class Citizen {
    @NonNull
    private String full_name ;
    @NonNull
    private GpsCoordinates coordinates ;
    @NonNull
    private String phone_number ;
    @NonNull
    private String password ;

    private Image image ;

    public Citizen(@NonNull String full_name, @NonNull GpsCoordinates coordinates,
                   @NonNull String phone_number, @NonNull String password, Image image) {
        this.full_name = full_name;
        this.coordinates = coordinates;
        this.phone_number = phone_number;
        this.password = password;
        this.image = image;
    }

    public Citizen(@NonNull String full_name, @NonNull GpsCoordinates coordinates,
                   @NonNull String phone_number, @NonNull String password) {
        this.full_name = full_name;
        this.coordinates = coordinates;
        this.phone_number = phone_number;
        this.password = password;
    }

    @NonNull
    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(@NonNull String full_name) {
        this.full_name = full_name;
    }

    @NonNull
    public GpsCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(@NonNull GpsCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    @NonNull
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(@NonNull String phone_number) {
        this.phone_number = phone_number;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isDataInputValid(){

        return !TextUtils.isEmpty(getPhone_number()) &&
                Patterns.PHONE.matcher(getPhone_number()).matches() && getPassword().length() > 4
                ;
    }
}
