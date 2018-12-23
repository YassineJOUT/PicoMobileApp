package ma.fstm.ilisi.pico.picomobile.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.viewmodel.AmbulanceViewModel;

public class AmbulanceDetailActivity extends AppCompatActivity {
    Ambulance ambulance;
    AmbulanceViewModel ambulanceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_detail);
        ambulance = getIntent().getParcelableExtra("ambulance");

        TextView detailMatriculeTextView = (TextView) findViewById(R.id.detailMatriculeTextView);
        Button detailBookButton = (Button) findViewById(R.id.detailBookButton);

        detailMatriculeTextView.setText(ambulance.getRegistrationNumber());
        if(!ambulance.getAvailable()){
            detailBookButton.setEnabled(false);
            detailBookButton.setBackgroundColor(Color.parseColor("#cccccc"));
        }

            ambulanceViewModel = ViewModelProviders.of(this).get(AmbulanceViewModel.class);
            findViewById(R.id.detailBookButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Ambulance booked","A");
                    ambulanceViewModel.doBookAnAmbulance(ambulance);
                    Toast.makeText(AmbulanceDetailActivity.this,"You have booked an ambulance",Toast.LENGTH_LONG);
                    startActivity(new Intent(AmbulanceDetailActivity.this,MapsActivity.class));
                }
            });
            ambulanceViewModel.doBookAnAmbulance(ambulance);


    }
}
// for commit