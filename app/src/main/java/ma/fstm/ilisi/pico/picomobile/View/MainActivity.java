package ma.fstm.ilisi.pico.picomobile.View;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;
    private static Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {

        if (message != null){
            btn1.performClick();
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            Log.e("Logged in1",ConfigClass.isLoggedIn+"");
                Log.e("Logged in2",ConfigClass.isLoggedIn+"");
        }
    }
}