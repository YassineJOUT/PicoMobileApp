package ma.fstm.ilisi.pico.picomobile.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import ma.fstm.ilisi.pico.picomobile.Model.Citizen;
import ma.fstm.ilisi.pico.picomobile.Model.GpsCoordinates;

/**
 * SignupViewModel class
 * This class is responsible for data binding and data observable with the Signup view
 *
 * @author      Yassine jout
 * @version     1.0
 */
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
    /**
     * Method afterImageChanged
     * After the image entry is changed in the sign up activity this method attributes the values typed
     * into the image field of a citizen
     * @param image char sequence representing
     */
    public void afterImageChanged(CharSequence image) {
        citizen.setPhone_number(image.toString());
    }
    /**
     * Method afterPassword2TextChanged
     * After the password2 entry is changed in the sign up activity this method attributes the values typed
     * into the password field of a this class
     * @param password2 char sequence representing
     */
    public void afterPassword2TextChanged(CharSequence password2) {
        password = password2.toString();
    }
    /**
     * Method afterPasswordTextChanged
     * After the password entry is changed in the sign up activity this method attributes the values typed
     * into the password field of a citizen
     * @param Password char sequence representing
     */
    public void afterPasswordTextChanged(CharSequence Password) {
        citizen.setPassword(Password.toString());
    }
    /**
     * Method afterFullNameTextChanged
     * After the fullName entry is changed in the sign up activity this method attributes the values typed
     * into the fullName field of a citizen
     * @param fullName char sequence representing
     */
    public void afterFullNameTextChanged(CharSequence fullName) {
        citizen.setFull_name(fullName.toString());
    }
    /**
     * Method afterPhoneTextChanged
     * After the phone entry is changed in the sign up activity this method attributes the values typed
     * into the phone field of a citizen
     * @param phone char sequence representing
     */
    public void afterPhoneTextChanged(CharSequence phone) {
        citizen.setPhone_number(phone.toString());
    }
    /**
     * Method onSignUpClicked
     * This method handles the click on the SignUp button
     * if all typed data are valid then this method calls the signUP method
     * located in the citizen class
     * @param view the view parameter is used to start a new intent to navigate to another activity
     *             when the signUp succeeded
     */
    public void onSignUpClicked(View view){

        if(citizen.isDataInputValid() && citizen.getPassword().equals(password)){

                citizen.SignUp(view);
        }
        else{

            setToastMessage(errorMessage);
        }
    }
}
