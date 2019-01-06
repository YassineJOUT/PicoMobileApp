
package ma.fstm.ilisi.pico.picomobile.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Driver implements Parcelable {

    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("driver_full_name")
    @Expose
    private String driverFullName;
    @SerializedName("ambulance_registration_number")
    @Expose
    private String ambulanceRegistrationNumber;
    @SerializedName("driver_longitude")
    @Expose
    private Double driverLongitude;
    @SerializedName("driver_latitude")
    @Expose
    private Double driverLatitude;

    public Driver(){

    }
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverFullName() {
        return driverFullName;
    }

    public void setDriverFullName(String driverFullName) {
        this.driverFullName = driverFullName;
    }

    public String getAmbulanceRegistrationNumber() {
        return ambulanceRegistrationNumber;
    }

    public void setAmbulanceRegistrationNumber(String ambulanceRegistrationNumber) {
        this.ambulanceRegistrationNumber = ambulanceRegistrationNumber;
    }

    public Double getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(Double driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    public Double getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(Double driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public static final Parcelable.Creator<Driver> CREATOR = new Parcelable.Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel source) {

            return new Driver(source);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(driverId);
        parcel.writeString(driverFullName);
        parcel.writeString(ambulanceRegistrationNumber);
        parcel.writeDouble(driverLongitude);
        parcel.writeDouble(driverLatitude);
    }
    public Driver(Parcel parcel){
        driverId = parcel.readString();
        driverFullName = parcel.readString();
        ambulanceRegistrationNumber = parcel.readString();
        driverLongitude = parcel.readDouble();
        driverLatitude = parcel.readDouble();

    }
}
