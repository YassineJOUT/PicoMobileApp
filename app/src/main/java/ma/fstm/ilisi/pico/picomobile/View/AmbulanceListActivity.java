package ma.fstm.ilisi.pico.picomobile.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.Model.Citizen;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.DownloadImageTask;

public class AmbulanceListActivity extends AppCompatActivity {
    //8 elements
    int [] images = {R.drawable.amb1, R.drawable.amb2,
            R.drawable.amb3, R.drawable.amb4,
            R.drawable.amb5, R.drawable.amb6,
            R.drawable.amb7, R.drawable.amb8};
    ArrayList<Ambulance> ambulances = new ArrayList<>();
    Location CitizenLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_list);

        ListView listView = (ListView) findViewById(R.id.ambulances_list_view);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        ambulances = getIntent().getParcelableArrayListExtra("ambulances");
        CitizenLocation = getIntent().getParcelableExtra("myPosition");

        Intent ambulanceDetailsIntent = new Intent(this, AmbulanceDetailActivity.class);
        listView.setItemsCanFocus(true);
        // Set an item click listener for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg){

                //Toast.makeText(getApplicationContext(), "selected Item position is " + ambulances.get(position), Toast.LENGTH_LONG).show();
                ambulanceDetailsIntent.putExtra("ambulance", (Ambulance) ambulances.get(position));
                startActivity(ambulanceDetailsIntent);
            }
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
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ambulanceImageView);
            TextView textViewMatricule = (TextView) convertView.findViewById(R.id.ambulanceMatriculeTextView);
            TextView textViewDistance = (TextView) convertView.findViewById(R.id.Lv_Dist);
            ToggleButton toggleButtonAvailable = (ToggleButton) convertView.findViewById(R.id.toggleButtonAvailable);
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
            if(CitizenLocation != null){
                distance = loc.distanceTo(CitizenLocation);
                textViewDistance.setText(distance+"");
            }

            if(ambulances.get(position).getAvailable())
                toggleButtonAvailable.setBackgroundColor(Color.parseColor("#19d32f"));
            else
                toggleButtonAvailable.setBackgroundColor(Color.parseColor("#cccccc"));


            return convertView;

        }
    }

}
// for commit