package ma.fstm.ilisi.pico.picomobile.View;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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


import ma.fstm.ilisi.pico.picomobile.Directions.ViewModel.DirectionsViewModel;
import ma.fstm.ilisi.pico.picomobile.Model.Ambulance;
import ma.fstm.ilisi.pico.picomobile.Model.Hospital;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.viewmodel.AmbulanceViewModel;
import ma.fstm.ilisi.pico.picomobile.viewmodel.HospitalsViewModel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private String locationProvider;
    private Location lastLocation;

    private Marker myLocationMarker;
    private Marker targetMarker;

    DirectionsViewModel directionsViewModel;
    HospitalsViewModel hospitalsViewModel;
    AmbulanceViewModel ambulanceViewModel;

    private Polyline[] polylineArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(33.697815, -7.385291);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Hopital molay abdelah"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,ConfigClass.zoomStreets));

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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getBestProvider(new Criteria(), false);
        locationManager.requestLocationUpdates(locationProvider, 1, 10, this);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        lastLocation = locationManager.getLastKnownLocation(locationProvider);

        // mMap.addMarker(new MarkerOptions().position(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude())).title("Myloc"));
        mMap.setOnMapClickListener(this);

        // get hospitals view model
        hospitalsViewModel = ViewModelProviders.of(this).get(HospitalsViewModel.class);

        // adding hospitals markers on the map
         hospitalsViewModel.onRefreshClicked().observe(this,hospitals -> {
             if(hospitals != null){
                 for (Hospital h :hospitals
                         ) {

                     mMap.addMarker(
                             new MarkerOptions()
                                     .position(new LatLng(h.getLatitude(),h.getLongitude()))
                                     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                     .title(h.getName())).showInfoWindow();
                 }
             }
         });
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("location", "My location  " + location.getLatitude());
        lastLocation = location;
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        if (myLocationMarker != null) myLocationMarker.remove();
      /*  myLocationMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(latlng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady(mMap);
                }

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (targetMarker != null) targetMarker.remove();
        targetMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        Location loc = new Location("");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        if (lastLocation == null) {

            lastLocation = mMap.getMyLocation();
        }
        double dist = lastLocation.distanceTo(loc);
        Log.e("Distance ",dist+"");
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
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e("marker"," jujhgjhgh");

        ambulanceViewModel = ViewModelProviders.of(this).get(AmbulanceViewModel.class);

        ambulanceViewModel.onRefreshClicked().observe(this,ambulances -> {
            if(ambulances != null){
                for(Ambulance a : ambulances){
                    Log.e("Amb ",a.getRegistrationNumber()+"");
                }
            }
        });
       // startActivity(new Intent(this,HospitalsActivity.class));
        return false;
    }
}
    /*private void UpdateLocation(double currentLat, double currentLon) {
        LatLng pos = new LatLng(currentLat, currentLon);

        Geocoder geocoder = new Geocoder(getApplicationContext());

        try {
            List<Address> lstAddr = geocoder.getFromLocation(currentLat, currentLon, 1);
            String str = lstAddr.get(0).getLocality();
            str += lstAddr.get(0).getLocality();
            mMap.addMarker(new MarkerOptions().position(pos).title(str));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, ConfigClass.zoomStreets));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/