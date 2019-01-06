package ma.fstm.ilisi.pico.picomobile.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.android.databinding.library.baseAdapters.BR;

import ma.fstm.ilisi.pico.picomobile.Model.Citizen;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.View.LoginActivity;

/**
 * LoginViewModel class
 * This class is responsible for data binding and data observable with the Login view
 *
 * @author      Yassine jout
 * @version     1.0
 */
public class LoginViewModel extends BaseObservable {

    private Citizen citizen;
    private String errorMessage = "Phone Number or Password not valid";


    @Bindable
    public String toastMessage = null;
    /**
     * Method getToastMessage
     * getter to the message that will be displayed in the toast in the login view
     * @return Message to be displayed as a string
     */
    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }
    /**
     * LoginViewModel Constructor
     * initialize an empty citizen object
     */

    public LoginViewModel() {
        citizen = new Citizen("", "");
    }
    /**
     * Method afterPhoneTextChanged
     * After the phone entry is changed in the login activity this method attributes the values typed
     * into the phone field of a citizen
     * @param phoneN char sequence representing
     */
    public void afterPhoneTextChanged(CharSequence phoneN) {
        citizen.setPhone_number(phoneN.toString());
    }
    /**
     * Method afterPasswordTextChanged
     * After the password entry is changed in the login activity this method attributes the values typed
     * into the password field of a citizen
     * @param pass char sequence representing
     */
    public void afterPasswordTextChanged(CharSequence pass) {
        citizen.setPassword(pass.toString());
    }
    /**
     * Method onLoginClicked
     * This method handles the click on the login button
     * if the phone and the password typed by the user are valid then this method calls the signIn method
     * located in the citizen class
     *
     */
    public void onLoginClicked(View view) {
        // if typed data is valid
        if (citizen.isDataInputValidForLogin()){
            // call Sign In function
            citizen.SignIn(view);
        }
        else{
            Log.e(" MSG2 ","Error");
            setToastMessage(errorMessage);
        }
    }

}