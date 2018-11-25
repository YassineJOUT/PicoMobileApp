package ma.fstm.ilisi.pico.picomobile.Model;

import android.media.Image;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

public class Citizen {
    @NonNull
    private String full_name ;
    @NonNull
    private double latitude ;
    @NonNull
    private double longitude ;
    @NonNull
    private String phone_number ;
    @NonNull
    private String password ;

    private Image image ;

    public Citizen(@NonNull final String full_name,@NonNull final double latitude,
                   @NonNull final double longitude,@NonNull final String phone_number,
                   @NonNull final String password,  final Image image) {
       this.full_name=full_name;
       this.latitude = latitude;
       this.longitude = longitude;
       this.phone_number = phone_number;
       this.password = password;
       this.image = image;
    }

    public Citizen(@NonNull final String full_name,@NonNull final double latitude,
                   @NonNull final double longitude,@NonNull final String phone_number,
                   @NonNull final String password) {
        this.full_name=full_name;
        this.latitude = latitude;
        this.longitude = longitude;
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

}
