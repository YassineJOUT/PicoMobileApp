package ma.fstm.ilisi.pico.picomobile.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.Model.Driver;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.DownloadImageTask;
import ma.fstm.ilisi.pico.picomobile.Utilities.Sockets;
import ma.fstm.ilisi.pico.picomobile.viewmodel.AmbulanceViewModel;

public class AmbulanceDetailActivity extends AppCompatActivity {
    Ambulance ambulance;
    AmbulanceViewModel ambulanceViewModel;
    String alarm_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_detail);
        ambulance = getIntent().getParcelableExtra("ambulance");

        Socket sock = Sockets.getInstance();

        sock.on("OTHER_CITIZEN_SELECTED_EVENT", args -> {
            Log.e("This is a message","fuck you you mother fucking fuck");
            AmbulanceDetailActivity.this.runOnUiThread(() ->
                    new AlertDialog.Builder(AmbulanceDetailActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Rejected Request")
                    .setMessage("This ambulance has been booked by another citizen please choose another one.")
                    .setPositiveButton("OK", (dialog, which) -> {

                        finish();;
                    })
                    .show());
        });
        sock.on("REJECTED_REQUEST_EVENT", args -> {
            try {
                // compare Alarms ids
                JSONObject obj = (JSONObject)args[0];
                Log.e(" rejected alarm id ",obj.getString("alarm_id"));
                AmbulanceDetailActivity.this.runOnUiThread(() -> new AlertDialog.Builder(AmbulanceDetailActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Rejected Request")
                        .setMessage("Your request was rejected by the driver, for further information call 911")
                        .setPositiveButton("OK", (dialog, which) -> {

                            finish();;
                        })
                        .show());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        sock.on("ACCEPTED_REQUEST_EVENT", args -> {
            JSONObject obj = (JSONObject)args[0];

            try {


                Driver driver = new Driver();
                driver.setDriverId(obj.getString("driver_id"));
                Log.e("Driver id ",obj.getString("driver_id"));
                driver.setDriverFullName(obj.getString("driver_full_name"));
                Log.e("Driver full name ",obj.getString("driver_full_name"));
                driver.setDriverLatitude(Double.valueOf(obj.getString("driver_latitude")));
                Log.e("Driver latitude",obj.getString("driver_latitude"));
                driver.setDriverLongitude(Double.valueOf(obj.getString("driver_longitude")));
                Log.e("Driver longitude ",obj.getString("driver_longitude"));
                alarm_id = obj.getString("alarm_id");
                Intent intent = new Intent(AmbulanceDetailActivity.this,MapsActivity.class);
                intent.putExtra("driver_info",driver);

               boolean isAmbBooked = true;
               intent.putExtra("isbooked",isAmbBooked);
               AmbulanceDetailActivity.this.startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        });


        findViewById(R.id.detailCancelButton).setOnClickListener(v -> {
            JSONObject obj = new JSONObject();
            try {
                obj.put("alarm_id",alarm_id);
                sock.emit("CANCEL_ALARM_EVENT",obj);
                v.setVisibility(View.INVISIBLE);
                v.setEnabled(false);
                ((Button)findViewById(R.id.detailBookButton)).setText("Book");
                findViewById(R.id.detailBookButton).setEnabled(true);
                AmbulanceDetailActivity.this.runOnUiThread(() -> new AlertDialog.Builder(AmbulanceDetailActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Canceled Request")
                        .setMessage("You have canceled your request!")
                        .setPositiveButton("OK", (dialog, which) -> {

                            finish();;
                        })
                        .show());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        TextView detailMatriculeTextView = findViewById(R.id.detailMatriculeTextView);
        Button detailBookButton =  findViewById(R.id.detailBookButton);
        Double rate = ambulance.getRating();
        RatingBar tb = findViewById(R.id.ratingBar);
        if(rate == null)
            tb.setRating(0);
        else
            tb.setRating(Float.valueOf(rate+"")*5);
        detailMatriculeTextView.setText(ambulance.getRegistrationNumber());
        if(!ambulance.getAvailable()){
            detailBookButton.setEnabled(false);
            detailBookButton.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        new DownloadImageTask(findViewById(R.id.ambulanceDetailImageView))
                .execute(ConfigClass.buildUrl("ambulances",ambulance.getId()));


            ambulanceViewModel = ViewModelProviders.of(this).get(AmbulanceViewModel.class);
           findViewById(R.id.detailCancelButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.detailCancelButton).setEnabled(false);
            findViewById(R.id.detailBookButton).setOnClickListener(v -> {
                Log.e("Ambulance booked","A");
                ambulanceViewModel.doBookAnAmbulance(ambulance,
                        AmbulanceDetailActivity.this).observe(AmbulanceDetailActivity.this,driver -> {
                            Log.e("an ambulance ","is booked ");
                    if(driver != null){
                        v.setEnabled(false);
                        ((Button)v).setText("Wating ...");
                        findViewById(R.id.detailCancelButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.detailCancelButton).setEnabled(true);
                        Toast.makeText(AmbulanceDetailActivity.this,
                                "You have booked an ambulance",Toast.LENGTH_LONG);
                        Log.e("alarm ID ",driver);
                    }
                });

            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                // do your code
                startActivity(new Intent(this,ProfileActivity.class));
                return true;
            case R.id.nearestHospital:
                startActivity(new Intent(this,MapsActivity.class));
                // do your code
                return true;
            case R.id.logout:

            {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Pico")
                        .setMessage("Are you sure you want to logout and exit the app ?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            ConfigClass.isLoggedIn = false ;
                            ConfigClass.token = "";
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            // do your code
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


