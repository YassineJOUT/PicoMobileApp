package ma.fstm.ilisi.pico.picomobile.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // get latitude
                    double currentLat = location.getLatitude();
                    // get longitude
                    double currentLon = location.getLongitude();

                    UpdateLocation(currentLat,currentLon);

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
            });
        }
// location manager get location from GPS provider
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // get latitude
                    double currentLat = location.getLatitude();
                    // get longitude
                    double currentLon = location.getLongitude();

                    UpdateLocation(currentLat,currentLon);

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
            });
        }
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(33.697815, -7.385291);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Hopital molay abdelah"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,ConfigClass.zoomStreets));

    }

    private void UpdateLocation(double currentLat,double currentLon){
        LatLng pos = new LatLng(currentLat,currentLon);

        Geocoder geocoder = new Geocoder(getApplicationContext());

        try{
            List<Address> lstAddr = geocoder.getFromLocation(currentLat,currentLon,1);
            String str = lstAddr.get(0).getLocality();
            str += lstAddr.get(0).getLocality();
            mMap.addMarker(new MarkerOptions().position(pos).title(str));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,ConfigClass.zoomStreets));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}