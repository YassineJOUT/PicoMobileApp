package ma.fstm.ilisi.pico.picomobile.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ma.fstm.ilisi.pico.picomobile.Repository.PicoWebRestClient;

/**
 * Config class
 * this class contains config data
 */
public class ConfigClass {
    // Authorization token
    public static String token = "";
    // true if a citizen is logged in
    public static boolean isLoggedIn = false ;
    //
    public static final String IPAddr = "pico.ossrv.nl";
    private static final String BASE_URL = "http://"+IPAddr+":9090/api/";

    // Zoom options
   // 1: World
    public static float zoomWord = 1f;
    //5: Landmass/continent
    public static float zoomLandmass = 5f;
    //10: City
    public static float zoomCity = 10f;
    //15: Streets
    public static float zoomStreets = 15f;
    //20: Buildings
    public static float zoomBuildings = 20f;
    //get Image from url


    public static String buildUrl(String role, String id){
        switch (role){
            case "citizens" : return BASE_URL+"citizens/image/"+id+".jpg";
            case "drivers" : return BASE_URL+"drivers/image/"+id+".jpg";
            case "ambulances" : return BASE_URL+"ambulances/image/"+id+".jpg";

        }
        return null;
    }

}
