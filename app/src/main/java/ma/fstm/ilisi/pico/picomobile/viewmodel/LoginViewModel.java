package ma.fstm.ilisi.pico.picomobile.viewmodel;

import org.json.*;
import com.loopj.android.http.*;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import cz.msebera.android.httpclient.Header;
import ma.fstm.ilisi.pico.picomobile.Model.User;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.PicoWebRestClient;

public class LoginViewModel extends BaseObservable {

    private User user;
    private String successMessage = "Login was successful";
    private String errorMessage = "Phone Number or Password not valid";

    @Bindable
    public String toastMessage = null;


    public String getToastMessage() {
        return toastMessage;
    }


    private void setToastMessage(String toastMessage) {

        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public LoginViewModel() {
        user = new User("", "");
    }

    public void afterEmailTextChanged(CharSequence s) {
        user.setmPhoneNumber(s.toString());
    }

    public void afterPasswordTextChanged(CharSequence s) {
        user.setPassword(s.toString());
    }

    public void onLoginClicked() {

        if (user.isInputDataValid()){

            RequestParams params = new RequestParams();

            params.put("phone_number", user.getmPhoneNumber());

            params.put("password", user.getPassword());

            Log.e("Phone",user.getmPhoneNumber());

            PicoWebRestClient.setUp();

            PicoWebRestClient.post("citizens/signin", params, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    ConfigClass.token = "";

                    ConfigClass.isLoggedIn = false;

                    Log.e("Response on failure",responseString+"ldkjfskdj");
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseString) {
                    try {

                        ConfigClass.token = responseString.getString("token");

                        ConfigClass.isLoggedIn = true;

                        Log.e("Response in success" ,responseString.getString("token")+"dlkfjlsd");

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            });
        }
        else{
            setToastMessage(errorMessage);
        }
            //setToastMessage(errorMessage);
    }
}