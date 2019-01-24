package ma.fstm.ilisi.pico.picomobile.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import ma.fstm.ilisi.pico.picomobile.Repository.PicoWebRestClient;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;

public class ProfileViewModel extends ViewModel {


    public LiveData<Boolean> ModifyUserData()  {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        if(ConfigClass.isLoggedIn){

            PicoWebRestClient.setUp("Authorization",ConfigClass.token);

            PicoWebRestClient.post("hospitals/citizens/", null,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {

                                errorResponse.getString(0);
                                data.setValue(false);

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject responseString) {
                            try {

                                data.setValue(true);
                                //   Log.e("Response in success" , hospitals.get(1).toString());

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    });

        }
        return data;
    }
    public LiveData<Boolean> retreiveProfileUser()  {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        if(ConfigClass.isLoggedIn){

            PicoWebRestClient.setUp("Authorization",ConfigClass.token);

            PicoWebRestClient.get("hospitals/citizens/", null,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {

                                errorResponse.getString(0);
                                data.setValue(false);

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject responseString) {
                            try {

                                data.setValue(true);
                                //   Log.e("Response in success" , hospitals.get(1).toString());

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    });

        }
        return data;
    }
}
