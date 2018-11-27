package ma.fstm.ilisi.pico.picomobile.View;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import ma.fstm.ilisi.pico.picomobile.Model.Hospitals;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.databinding.ActivityMainBinding;
import ma.fstm.ilisi.pico.picomobile.viewmodel.LoginViewModel;


public class MainActivity extends AppCompatActivity {
    private static Context mContext;
    private static Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(new LoginViewModel());
        activityMainBinding.executePendingBindings();

        Button btn = (Button)findViewById(R.id.SignUpBtn);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
            }
        });
         btn1 = new Button(this);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HospitalsActivity.class));
            }
        });



    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {

        if (message != null){
            btn1.performClick();
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            Log.e("Logged in1",ConfigClass.isLoggedIn+"");
           // if(ConfigClass.isLoggedIn){
                Log.e("Logged in2",ConfigClass.isLoggedIn+"");

          //  }
        }



    }

    public static void goToActivity(Context mContext) {
        Intent activity = new Intent(mContext, HospitalsActivity.class);
        mContext.startActivity(activity);
    }
}