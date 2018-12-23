
package ma.fstm.ilisi.pico.picomobile.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import ma.fstm.ilisi.pico.picomobile.Repository.PicoWebRestClientSync;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;

public class Ambulance implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("registration_number")
    @Expose
    private String registrationNumber;
    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Ambulance> CREATOR = new Parcelable.Creator<Ambulance>() {
        @Override
        public Ambulance createFromParcel(Parcel source) {

            return new Ambulance(source);
        }

        @Override
        public Ambulance[] newArray(int size) {
            return new Ambulance[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(registrationNumber);
        dest.writeInt(available ? 1 : 0);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }


    Ambulance(Parcel p){
        this.id = p.readString();
        this.registrationNumber = p.readString();
        this.available = p.readInt() == 0 ? false : true;
        this.latitude = p.readDouble();
        this.longitude = p.readDouble();
    }

    public void BookAnAmbulance(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    RequestParams params = new RequestParams();

                    params.put("ambulance_id", id);

                    PicoWebRestClientSync.setUp("Authorization",ConfigClass.token);

                    PicoWebRestClientSync.setUp("Content-Type","application/x-www-form-urlencoded");

                    PicoWebRestClientSync.post("alarms/citizens",params, new JsonHttpResponseHandler(){

                        @Override
                        public void setUseSynchronousMode(boolean sync) {
                            super.setUseSynchronousMode(sync);
                            if (!sync)
                                sync = true;
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Log.e("Success", "yes");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);

                            Log.e("Error", "Error");

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

}
