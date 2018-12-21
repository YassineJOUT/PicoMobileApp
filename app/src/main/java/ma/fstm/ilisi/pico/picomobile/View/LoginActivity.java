package ma.fstm.ilisi.pico.picomobile.View;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import am.appwise.components.ni.NoInternetDialog;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.InternetCheck;
import ma.fstm.ilisi.pico.picomobile.databinding.ActivityLoginBinding;
import ma.fstm.ilisi.pico.picomobile.viewmodel.HospitalsViewModel;
import ma.fstm.ilisi.pico.picomobile.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private static Context mContext;
    private static Button btn1;

    LoginViewModel loginViewModel;
    NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
         noInternetDialog = new NoInternetDialog.Builder(this).build();

        ActivityLoginBinding activityLoginBinding;
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setViewModel(new LoginViewModel());
        activityLoginBinding.executePendingBindings();
       if(InternetCheck.isConnected(this)) {


           findViewById(R.id.link_signup).setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {
                   // Start the Signup activity
                   Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                   startActivity(intent);
                   // finish();
                   //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                   findViewById(R.id.sign_in_button).setEnabled(true);
               }
           });
       }else{
           findViewById(R.id.sign_in_button).setEnabled(false);
           Toast.makeText(this,"Check you internet connexion",Toast.LENGTH_LONG).show();
           buildDialog(this).show();
       }
    }
    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null){
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
}
