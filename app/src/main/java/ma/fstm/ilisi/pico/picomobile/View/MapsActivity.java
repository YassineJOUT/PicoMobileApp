package ma.fstm.ilisi.pico.picomobile.View;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ma.fstm.ilisi.pico.picomobile.Directions.ViewModel.DirectionsViewModel;
import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.Model.Driver;
import ma.fstm.ilisi.pico.picomobile.Model.Hospital;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Repository.PicoWebRestClient;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.DownloadImageTask;
import ma.fstm.ilisi.pico.picomobile.Utilities.Sockets;
import ma.fstm.ilisi.pico.picomobile.viewmodel.AmbulanceViewModel;
import ma.fstm.ilisi.pico.picomobile.viewmodel.HospitalsViewModel;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private String locationProvider;
    private Location lastLocation ;

    private  Ambulance nearestAmbulance;
    private Marker ambulanceMarker;
    private Marker targetMarker;
    private Driver driver;

    DirectionsViewModel directionsViewModel;
    HospitalsViewModel hospitalsViewModel;
    AmbulanceViewModel ambulanceViewModel;

    private Polyline[] polylineArray;
    private int currentPolylineLenght;

    private HashMap<Marker,Hospital> hospitalMarkerHash;

    private Socket socket;

    private boolean isAmbBooked ;
    private String last_alarm_id;



    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hospitalMarkerHash = new HashMap<>();

        isAmbBooked = false ;

        sheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));

       Button  bs_btn_book = findViewById(R.id.bs_Book);
        findViewById(R.id.bs_Cancel).setVisibility(View.INVISIBLE);
        findViewById(R.id.bs_Cancel).setEnabled(false);
       bs_btn_book.setOnClickListener(v -> {
           Log.e("Ambulance booked","A");
           if(nearestAmbulance != null)
           ambulanceViewModel.doBookAnAmbulance(nearestAmbulance,
                   MapsActivity.this).observe(MapsActivity.this,driver -> {
               Log.e("an ambulance ","is booked ");
               if(driver != null){
                   v.setEnabled(false);
                   ((Button)v).setText("Wating ...");
                   findViewById(R.id.bs_Cancel).setVisibility(View.VISIBLE);
                   findViewById(R.id.bs_Cancel).setEnabled(true);
                   Toast.makeText(MapsActivity.this,
                           "You have booked an ambulance",Toast.LENGTH_LONG);
                   Log.e("alarm ID ",driver);
               }
           });
       });
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        if(!isAmbBooked) {
                            if (!hospitalMarkerHash.isEmpty()) {
                                LinkedHashMap<Hospital, Float> hmHospitals = getNearestHospital();
                                Map.Entry<Hospital, Float> nearestHospital = null;
                                if (hmHospitals != null) {
                                    nearestHospital = hmHospitals.entrySet().iterator().next();
                                }
                                if (nearestHospital != null) {
                                    setBottomSheetContent("hospital");
                                    ((TextView) findViewById(R.id.bs_hospitalName)).setText(nearestHospital.getKey().getName());
                                    ((TextView) findViewById(R.id.bs_distance)).setText("Dist : " + nearestHospital.getValue() + "");
                                    ambulanceViewModel = ViewModelProviders.of(MapsActivity.this).get(AmbulanceViewModel.class);

                                    Hospital h = nearestHospital.getKey();
                                    ambulanceViewModel.onRefreshClicked(h)
                                            .observe(MapsActivity.this, ambulances -> {
                                                if (ambulances != null) {
                                                    if (!ambulances.isEmpty()) {
                                                        nearestAmbulance = ambulances.get(0);
                                                        RatingBar tb = findViewById(R.id.bs_ratingBar);
                                                        Double rate = nearestAmbulance.getRating();
                                                        if (rate == null)
                                                            tb.setRating(0);
                                                        else
                                                            tb.setRating(Float.valueOf(rate + "") * 5);
                                                        Log.e("rating ", rate + "");
                                                        ((TextView) findViewById(R.id.bs_amb_RN)).setText("Registration number : " + nearestAmbulance.getRegistrationNumber());
                                                        // get image from the api
                                                        new DownloadImageTask( findViewById(R.id.bs_imageView))
                                                                .execute(ConfigClass.buildUrl("ambulances", nearestAmbulance.getId()));
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                        else{
                            if(driver != null ){
                                setBottomSheetContent("driver");
                                ((TextView) findViewById(R.id.bs_amb_RN)).setText("Driver name : "+driver.getDriverFullName());
                                // get image from the api
                                new DownloadImageTask( findViewById(R.id.bs_imageView))
                                        .execute(ConfigClass.buildUrl("drivers",driver.getDriverId()));
                            }
                        }
//                        btnBottomSheet.setText("Close Sheet");

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
         //               btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });



    }
    private void setBottomSheetContent(String hospOrDriver){
        switch (hospOrDriver){
            case "hospital" :
                {
                    // set Title
                    ((TextView)findViewById(R.id.bs_Title)).setText("Nearest Hospital");
                    // set Subtitle
                    ((TextView)findViewById(R.id.bs_Subtitle)).setText("Ambulance");
                    // set button to disabled and invisible
                    (findViewById(R.id.bs_Book)).setVisibility(View.VISIBLE);
                    (findViewById(R.id.bs_Book)).setEnabled(true);


                }
                 break;
            case "driver"  :
            {
                // set Title
                ((TextView)findViewById(R.id.bs_Title)).setText("Drivers Info");
                // set Subtitle
                ((TextView)findViewById(R.id.bs_Subtitle)).setText("Driver");
                (findViewById(R.id.bs_distance)).setVisibility(View.INVISIBLE);
                //
                ((TextView)findViewById(R.id.bs_hospitalName)).setText("Status :  Mission en cours ..");
                // set button to disabled and invisible
                (findViewById(R.id.bs_Book)).setVisibility(View.INVISIBLE);
                (findViewById(R.id.bs_Book)).setEnabled(false);

                (findViewById(R.id.bs_Cancel)).setVisibility(View.INVISIBLE);
                (findViewById(R.id.bs_Cancel)).setEnabled(false);


            } break;
        }

    }

    public  void removeHospitals(){
        if (!hospitalMarkerHash.isEmpty()) {
            for (Map.Entry<Marker, Hospital> entry : hospitalMarkerHash.entrySet()) {
                Marker key = entry.getKey();
                key.remove();
            }
        }
    }

    private void DrawPolyLine(Location loc){
        if(lastLocation == null ) return ;

        directionsViewModel = ViewModelProviders.of(this).get(DirectionsViewModel.class);

        directionsViewModel.getDirectionsLiveData(lastLocation.getLatitude()+","+lastLocation.getLongitude(),
                loc.getLatitude()+","+loc.getLongitude(),
                getString(R.string.google_maps_key)).observe(this,direction ->
        {
            directionsViewModel.getPoylineLiveData(direction).observe(this,
                    polylines -> {
                        Log.e("GetPolyLiveData ","Live data call back");
                        if (polylineArray != null) {
                            for (Polyline polyline : polylineArray) {
                                polyline.remove();
                            }
                        }
                        if (polylines != null) {

                            polylineArray = new Polyline[polylines.length];
                            currentPolylineLenght = polylines.length;
                                    Log.d("reached here", "reached here");
                            for (int i=0;i<polylines.length;i++) {
                                PolylineOptions options = new PolylineOptions();

                                options.color(Color.MAGENTA);
                                options.width(10);
                                options.addAll(PolyUtil.decode(polylines[i]));
                                polylineArray[i] = mMap.addPolyline(options);

                            }
                        }
                    });
        });
    }



    private void DrawDriverPosition(JSONObject obj){

        runOnUiThread(() -> {

            try {
                removeHospitals();

                if(ambulanceMarker != null) {
                    ambulanceMarker.remove();
                }
                double latitude = obj.getDouble("latitude");
                double longitude = obj.getDouble("longitude");
                ambulanceMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer50x50)));
                Log.e("Draw polyline Latitude",latitude+"");
                Log.e("Draw polyline longitude",longitude+"");
                Location loc = new Location(locationProvider);
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);
                DrawPolyLine(loc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
    public void socketAuthentication() {

            socket = Sockets.getInstance();

            socket.on("CITIZEN_AUTH_SUCCESS_EVENT", args -> {
                Log.e("Socket status : ","Socket authenticated");
              //sendAlarm();
            }).on("AMBULANCE_POSITION_CHANGE_EVENT", args -> {
                JSONObject obj = (JSONObject)args[0];
                DrawDriverPosition(obj);
            }).on(Socket.EVENT_CONNECT, args -> {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("token",ConfigClass.token);
                    socket.emit("CITIZEN_AUNTENTICATION_EVENT", obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //logElt.innerHTML = 'Socket connected';

            }).on("MISSION_ACCOMPLISHED_EVENT", args -> {
                // Show dialog feed back
                JSONObject obj = (JSONObject)args[0];
                try {
                    last_alarm_id = obj.getString("alarm_id");
                    MapsActivity.this.runOnUiThread(() -> new AlertDialog.Builder(MapsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Mission Accomplished")
                            .setMessage("Please give your feed back about our service")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // send to feed back activity
                                Intent intent = new Intent(MapsActivity.this,RatingActivity.class);
                                intent.putExtra("alarm_id",last_alarm_id);
                                startActivity(intent);
                            }).setNegativeButton("No", (dialog, which) -> {
                                isAmbBooked = false ;
                                if(ambulanceMarker != null && currentPolylineLenght != 0){

                                    ambulanceMarker.remove();
                                    for (int i = 0 ; i< currentPolylineLenght ;i++)
                                        polylineArray[i].remove();
                                }
                                onMapReady(mMap);
                            })
                            .show());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).on("ACCOUNT_DEACTIVATED_EVENT", args -> {
                JSONObject obj = (JSONObject)args[0];
                try {
                    // Logout + message
                    Log.e("deactivation alarm id","True");
                    MapsActivity.this.runOnUiThread(() -> new AlertDialog.Builder(MapsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Fake alarm")
                            .setMessage("You're account has been dssactivated! contact your admin :p")
                            .setPositiveButton("OK", (dialog, which) -> {
                                ConfigClass.isLoggedIn = false ;
                                ConfigClass.token = "";
                                finish();
                                System.exit(0);
                            })
                            .show());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        socket.connect();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String resumedFromFb = intent.getStringExtra("feedBack");
        Log.e("resumedFromFb 1",resumedFromFb+"");
        if(mMap != null){

            if(resumedFromFb != null)
                if(resumedFromFb.equalsIgnoreCase("true")){

                    ambulanceMarker.remove();
                    isAmbBooked = false ;
                    if(ambulanceMarker != null && currentPolylineLenght != 0){

                        ambulanceMarker.remove();
                        for (int i = 0 ; i< currentPolylineLenght ;i++)
                            polylineArray[i].remove();
                    }
                    onMapReady(mMap);

                }

        }
        driver =  intent.getParcelableExtra("driver_info");
        boolean isbooked = intent.getBooleanExtra("isbooked",false);
        isAmbBooked = isbooked;
        Log.e("isbooked ",isbooked +"");
        if(driver != null){
            setBottomSheetContent("driver");
            Log.e("new intent ","driver");

        }




    }



    /**
     *
     * getHospital with the nearest distance
     */

    public LinkedHashMap<Hospital, Float> sortHashMapByValues(
            HashMap<Hospital, Float>  passedMap) {
        List<Hospital> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Float> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);

        LinkedHashMap<Hospital, Float> sortedMap =
                new LinkedHashMap<>();

        Iterator<Float> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Float val = valueIt.next();
            Iterator<Hospital> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Hospital key = keyIt.next();
                Float comp1 = passedMap.get(key);
                Float comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
   public LinkedHashMap<Hospital,Float> getNearestHospital(){

       HashMap<Hospital,Float> hospitalDistanceMap = new HashMap<>();
        if (!hospitalMarkerHash.isEmpty()) {
           double[] distances = new double[hospitalMarkerHash.size()];
           int i = 0;
           for (Map.Entry<Marker, Hospital> entry : hospitalMarkerHash.entrySet()) {
               Hospital h = entry.getValue();
               Location l = new Location("jps") ;

               l.setLatitude(h.getLatitude());
               l.setLongitude(h.getLongitude());

               lastLocation = mMap.getMyLocation();
               if(lastLocation != null){
                   hospitalDistanceMap.put(h,lastLocation.distanceTo(l));
               }
           }
           if(!hospitalDistanceMap.isEmpty()){
              return sortHashMapByValues(hospitalDistanceMap);
           }

           else return null;
       }

       return  null;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        setBottomSheetContent("hospital");

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION

                    }, 0);
            return;
        }

        String resumedFromFb = getIntent().getStringExtra("feedBack");
        Log.e("resumedFromFb 1 onmapr",resumedFromFb+"");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getBestProvider(new Criteria(), false);
        locationManager.requestLocationUpdates(locationProvider, 1, 10, this);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        lastLocation = locationManager.getLastKnownLocation(locationProvider);

        // get hospitals view model
        hospitalsViewModel = ViewModelProviders.of(this).get(HospitalsViewModel.class);
        isAmbBooked =  getIntent().getBooleanExtra("isAmbBooked",false);
        if(!isAmbBooked){
            // adding hospitals markers on the map
            hospitalsViewModel.onRefreshClicked().observe(this,hospitals -> {
                if(hospitals != null){
                    for (Hospital h :hospitals) {
                        hospitalMarkerHash.put(
                                mMap.addMarker(
                                        new MarkerOptions()
                                                .position(new LatLng(h.getLatitude(),h.getLongitude()))
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                                .title(h.getName())),h);
                    }
                }
            });
            if(lastLocation == null){
                lastLocation = new Location("");
                lastLocation.setLatitude(33.699995 );
                lastLocation.setLongitude(-7.362469);}

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastLocation.getLatitude(),
                            lastLocation.getLongitude()),
                    ConfigClass.zoomStreets-0.5f));


            socketAuthentication();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e("Location ","changed");
        Log.d("location", "My location  " + location.getLatitude());
        lastLocation = location;

        JSONObject obj = new JSONObject();
        try {
            obj.put("latitude",location.getLatitude());
            obj.put("longitude",location.getLongitude());
            socket.emit("POSITION_CHANGE_EVENT", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("status ","changed");
    }

    @Override
    public void onProviderEnabled(String provider) {

        Log.e("Provider ","enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("Provider ","disabled");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady(mMap);
                }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        ambulanceViewModel = ViewModelProviders.of(this).get(AmbulanceViewModel.class);
        Intent intent = new Intent(this, AmbulanceListActivity.class);
        hospitalMarkerHash.get(marker);
        ambulanceViewModel.onRefreshClicked(hospitalMarkerHash.get(marker)).observe(
                this,ambulances -> {
            if(ambulances != null){
                intent.putExtra("myPosition",lastLocation);
                intent.putParcelableArrayListExtra("ambulances",
                        (ArrayList<Ambulance>) ambulances);
                startActivity(intent);

            }
        });
        return false;
    }

    //menu

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
