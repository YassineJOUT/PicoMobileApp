package ma.fstm.ilisi.pico.picomobile.View;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.databinding.ActivityHospitalsBinding;
import ma.fstm.ilisi.pico.picomobile.viewmodel.HospitalsViewModel;

public class HospitalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHospitalsBinding hospitalsMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_hospitals);
        hospitalsMainBinding.setViewModel(new HospitalsViewModel());
        hospitalsMainBinding.executePendingBindings();
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null){
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
