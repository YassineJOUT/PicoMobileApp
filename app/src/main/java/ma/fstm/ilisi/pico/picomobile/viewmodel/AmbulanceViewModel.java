package ma.fstm.ilisi.pico.picomobile.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.file.AtomicMoveNotSupportedException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.Model.Driver;
import ma.fstm.ilisi.pico.picomobile.Model.Hospital;
import ma.fstm.ilisi.pico.picomobile.Repository.PicoWebRestClient;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.View.AmbulanceDetailActivity;
import ma.fstm.ilisi.pico.picomobile.View.MapsActivity;

public class AmbulanceViewModel  extends ViewModel {


    public LiveData<List<Ambulance>> getAmbulances() {
        return ambulances;
    }

    public void setAmbulances(LiveData<List<Ambulance>> ambulances) {
        this.ambulances = ambulances;
    }

    private LiveData<List<Ambulance>> ambulances;
    private String successMessage = "Sign in was successful";
    private String errorMessage = "field invalid not valid";

    public MutableLiveData<String> doBookAnAmbulance(Ambulance amb, AmbulanceDetailActivity v) {

        final MutableLiveData<String> data = new MutableLiveData<>();
        amb.BookAnAmbulance().observe(v,alarmId -> {

                data.setValue(alarmId);

        });
        return data;
    }
    public AmbulanceViewModel() {

    }

    public LiveData<List<Ambulance>> onRefreshClicked(Hospital hospital)  {
        final MutableLiveData<List<Ambulance>> data = new MutableLiveData<>();
        if(ConfigClass.isLoggedIn){

            PicoWebRestClient.setUp("Authorization",ConfigClass.token);
            PicoWebRestClient.get("hospitals/citizens/"+hospital.get_id()+"/ambulances", null, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    try {


                        errorResponse.getString(0);

                        data.setValue(null);

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseString) {
                    try {
                        //  ConfigClass.token = responseString.getString("");

                        //  ConfigClass.isLoggedIn = true;

                        Gson gson = new Gson();

                        String content = responseString.getString("ambulances").toString()+"";
                        Type listType = new TypeToken<List<Ambulance>>() {}.getType();
                        // hospitals =
                        Log.e("Response in success" ,content);
                        data.setValue(gson.fromJson(content,listType));
                        //   Log.e("Response in success" , hospitals.get(1).toString());

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            });

        }
        return data;
    }
}
