package ma.fstm.ilisi.pico.picomobile.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import ma.fstm.ilisi.pico.picomobile.Model.Citizen;
import ma.fstm.ilisi.pico.picomobile.Model.GpsCoordinates;
import ma.fstm.ilisi.pico.picomobile.Utilities.PicoWebRestClient;


public class SignupViewModel extends BaseObservable {


    private Citizen citizen;
    private String password;

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

    public SignupViewModel() {
        citizen = new Citizen("",new GpsCoordinates(0,0),"","",null);
        password = "";
    }
    public void afterImageChanged(CharSequence s) {
        citizen.setPhone_number(s.toString());
    }
    public void afterPassword2TextChanged(CharSequence s) {
        password = s.toString();
    }
    public void afterPasswordTextChanged(CharSequence s) {
        citizen.setPassword(s.toString());
    }
    public void afterFullNameTextChanged(CharSequence s) {
        citizen.setFull_name(s.toString());
    }

    public void afterPhoneTextChanged(CharSequence s) {
        citizen.setPhone_number(s.toString());
    }
    public void onSignUpClicked(){
        Log.e("Is valide " , citizen.isDataInputValid()+"");
        if(citizen.isDataInputValid() && citizen.getPassword().equals(password)){

            RequestParams params = new RequestParams();

            params.put("full_name", citizen.getFull_name());

            params.put("latitude", citizen.getCoordinates().getLatitude());

            params.put("longitude", citizen.getCoordinates().getLongitude());

            params.put("phone_number", citizen.getPhone_number());

            params.put("password", citizen.getPassword());

            Log.e("Full name",citizen.getFull_name());

            PicoWebRestClient.setUp("Content-Type","application/x-www-form-urlencoded");

            PicoWebRestClient.post("citizens/signup", params, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    try {

                        //  ConfigClass.token = responseString.getString("");

                        //  ConfigClass.isLoggedIn = true;

                         Log.e("Response in Error" ,errorResponse.getString("error")+"dlkfjlsd");
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

                       Log.e("Response in success" ,responseString.getString("_id")+"dlkfjlsd");
                      // StartActivity(SignupActivity.this,MainActivity.class);

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            });
        }
        else{
            Log.e("Full name",citizen.getFull_name());
            Log.e("Password",citizen.getPassword());
            Log.e("Password2",password);
            setToastMessage(errorMessage);
        }
    }
}
