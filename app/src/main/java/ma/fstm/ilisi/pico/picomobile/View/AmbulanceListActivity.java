package ma.fstm.ilisi.pico.picomobile.View;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.R;

public class AmbulanceListActivity extends AppCompatActivity {
    //8 elements
    int [] images = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};
    ArrayList<Ambulance> ambulances = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_list);

        ListView listView = (ListView) findViewById(R.id.ambulances_list_view);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        ambulances = getIntent().getParcelableArrayListExtra("ambulances");


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
            ToggleButton toggleButtonAvailable = (ToggleButton) convertView.findViewById(R.id.toggleButtonAvailable);

            imageView.setImageResource(images[position]);
            textViewMatricule.setText(ambulances.get(position).getRegistrationNumber());
            if(ambulances.get(position).getAvailable())
                toggleButtonAvailable.setBackgroundColor(Color.parseColor("#19d32f"));
            else
                toggleButtonAvailable.setBackgroundColor(Color.parseColor("#cccccc"));


            return convertView;

        }
    }
}
// for commit