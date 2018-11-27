package ma.fstm.ilisi.pico.picomobile.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cz.msebera.android.httpclient.Header;
import ma.fstm.ilisi.pico.picomobile.Model.Citizen;
import ma.fstm.ilisi.pico.picomobile.Model.Hospitals;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.PicoWebRestClient;

public class HospitalsViewModel extends BaseObservable {



    private List<Hospitals> hospitalsList;

    private String successMessage = "Sign in was successful";
    private String errorMessage = "field invalid not valid";


    @Bindable
    public String toastMessage = null;


    public String getToastMessage() {
        return toastMessage;
    }


    private void setToastMessage(String toastMessage) {

        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public HospitalsViewModel() {
        hospitalsList = new ArrayList<Hospitals>();
    }

    public void onRefreshClicked(View view){

        Log.e("Response in Error" ,ConfigClass.isLoggedIn+"");
        if(ConfigClass.isLoggedIn){

            PicoWebRestClient.setUp("Authorization",ConfigClass.token);

            PicoWebRestClient.get("hospitals/citizens/", null, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    try {
                        //  ConfigClass.token = responseString.getString("");

                        //  ConfigClass.isLoggedIn = true;

                        Log.e("Response in Error" ,errorResponse.getString(0)+"dlkfjlsd");
                        //  StartActivity(SignupActivity.this,MainActivity.class);
                        setToastMessage(errorMessage);

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseString) {
                    try {
                        //  ConfigClass.token = responseString.getString("");

                        //  ConfigClass.isLoggedIn = true;
                        String content = responseString.getString("hospitals").toString()+"";
                        Log.e("Response in success" ,content);


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            });
        }
        else{
            setToastMessage(errorMessage);
        }
    }
}
