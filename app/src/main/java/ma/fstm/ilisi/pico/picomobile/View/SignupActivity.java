package ma.fstm.ilisi.pico.picomobile.View;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.databinding.ActivitySignUpBinding;
import ma.fstm.ilisi.pico.picomobile.viewmodel.SignupViewModel;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignUpBinding activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        activitySignUpBinding.setViewModel(new SignupViewModel());
        activitySignUpBinding.executePendingBindings();
        findViewById(R.id.link_signin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Sign in activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                //finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null)
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
