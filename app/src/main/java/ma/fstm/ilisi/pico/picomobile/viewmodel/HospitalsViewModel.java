package ma.fstm.ilisi.pico.picomobile.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ma.fstm.ilisi.pico.picomobile.Model.Hospital;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.PicoWebRestClient;
/**
 * HospitalsViewModel class
 * This class is responsible for data binding and data observable with the hospitals view
 *
 * @author      Yassine jout
 * @version     1.0
 */
public class HospitalsViewModel extends BaseObservable {

    private List<Hospital> hospitalList;
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
        hospitalList = new ArrayList<Hospital>();
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
                        List<Hospital> hospitals = new ArrayList<>();
                        Gson gson = new Gson();

                        String content = responseString.getString("hospitals").toString()+"";
                        Type listType = new TypeToken<List<Hospital>>() {}.getType();
                        hospitals = gson.fromJson(content,listType);
                        Log.e("Response in success" ,content);
                        Log.e("Response in success" ,hospitals.get(1).toString());

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
