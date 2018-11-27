package ma.fstm.ilisi.pico.picomobile.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import ma.fstm.ilisi.pico.picomobile.Model.Citizen;
import ma.fstm.ilisi.pico.picomobile.Model.User;

public class LoginViewModel extends BaseObservable {

    private Citizen citizen;
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
        citizen = new Citizen("", "");
    }

    public void afterPhoneTextChanged(CharSequence s) {
        citizen.setPhone_number(s.toString());
    }

    public void afterPasswordTextChanged(CharSequence s) {
        citizen.setPassword(s.toString());
    }

    public void onLoginClicked(View view) {

        if (citizen.isDataInputValid()){

            citizen.SignIn(view);
        }
        else{
            setToastMessage(errorMessage);
        }
    }
}