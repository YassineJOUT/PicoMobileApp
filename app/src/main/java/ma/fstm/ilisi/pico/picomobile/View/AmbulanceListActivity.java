package ma.fstm.ilisi.pico.picomobile.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.InputStream;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.Model.Citizen;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.DownloadImageTask;

public class AmbulanceListActivity extends AppCompatActivity {

    ArrayList<Ambulance> ambulances = new ArrayList<>();
    Location CitizenLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_list);

        ListView listView = findViewById(R.id.ambulances_list_view);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        ambulances = getIntent().getParcelableArrayListExtra("ambulances");
        CitizenLocation = getIntent().getParcelableExtra("myPosition");

        Intent ambulanceDetailsIntent = new Intent(this, AmbulanceDetailActivity.class);
        listView.setItemsCanFocus(true);
        // Set an item click listener for ListView
        listView.setOnItemClickListener((adapter, view, position, arg) -> {

            ambulanceDetailsIntent.putExtra("ambulance", ambulances.get(position));
            startActivity(ambulanceDetailsIntent);
        });


    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return ambulances.size();
        }

        @Override
        public Object getItem(int position) {
            return ambulances.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.ambulances_list_view, null);
            ImageView imageView =  convertView.findViewById(R.id.ambulanceImageView);
            TextView textViewMatricule =  convertView.findViewById(R.id.ambulanceMatriculeTextView);
            TextView textViewDistance =  convertView.findViewById(R.id.Lv_Dist);
            ToggleButton toggleButtonAvailable =  convertView.findViewById(R.id.toggleButtonAvailable);
            RatingBar tb = convertView.findViewById(R.id.lv_ratingBar);

            // get image from the api
            new DownloadImageTask(imageView)
                    .execute(ConfigClass.buildUrl("ambulances",ambulances.get(position).getId()));


            Double rate = ambulances.get(position).getRating();
            if(rate == null)
                tb.setRating(0);
            else
                tb.setRating(Float.valueOf(ambulances.get(position).getRating()+"")*5);

            Log.e("rating ",rate+"");
            textViewMatricule.setText(ambulances.get(position).getRegistrationNumber());
            Location loc = new Location("gps");
            loc.setLatitude(ambulances.get(position).getLatitude());
            loc.setLatitude(ambulances.get(position).getLongitude());
            Float distance = 0f;

            DecimalFormat df = new DecimalFormat("##.##");
            df.setRoundingMode(RoundingMode.DOWN);
            if(CitizenLocation != null){
                distance = loc.distanceTo(CitizenLocation);
                textViewDistance.setText(df.format(distance)+" m");
            }
            if(ambulances.get(position).getAvailable())
                toggleButtonAvailable.setBackgroundColor(Color.parseColor("#19d32f"));
            else
                toggleButtonAvailable.setBackgroundColor(Color.parseColor("#cccccc"));
            return convertView;
        }
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
            case R.id.settings:
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
