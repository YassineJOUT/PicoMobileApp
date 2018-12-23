package ma.fstm.ilisi.pico.picomobile.View;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.R;

public class AmbulanceDetailActivity extends AppCompatActivity {
    Ambulance ambulance;
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


    }
}
// for commit