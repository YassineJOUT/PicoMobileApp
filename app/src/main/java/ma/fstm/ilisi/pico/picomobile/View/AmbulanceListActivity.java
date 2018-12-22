package ma.fstm.ilisi.pico.picomobile.View;

import android.graphics.Color;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.R;

public class AmbulanceListActivity extends AppCompatActivity {
    // Array of strings...
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};
    //8 elements
    int [] images = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};
    //String[] matricules = {"123654K5M","123654K5M", "123654K5M", "123654K5M", "123654K5M","123654K5M", "123654K5M", "123654K5M"};
    //Boolean[] available = {true, false, false, true, false, true, true, true};
    ArrayList<Ambulance> ambulances = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_list);

        ListView listView = (ListView) findViewById(R.id.ambulances_list_view);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

         ambulances = getIntent().getParcelableArrayListExtra("ambulances");

    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return ambulances.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
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
