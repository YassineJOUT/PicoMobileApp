package ma.fstm.ilisi.pico.picomobile.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.Sockets;

public class RatingActivity extends AppCompatActivity {

    RatingBar rb ;
    Socket sock;
    float FBrating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        rb = findViewById(R.id.fb_ratingBar);
        sock = Sockets.getInstance();

        String alarm_id = getIntent().getStringExtra("alarm_id");
        rb.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            FBrating = rating;


        });

        findViewById(R.id.sendFB).setOnClickListener(v -> {
            JSONObject obj = new JSONObject();
            try {
                String comment = ((EditText)findViewById(R.id.fb_Comment)).getText().toString();
                obj.put("percentage",FBrating/5);
                obj.put("comment",comment);
                obj.put("alarm_id",alarm_id);

                sock.emit("CITIZEN_FEEDBACK_EVENT", obj);
                RatingActivity.this.runOnUiThread(() -> new AlertDialog.Builder(RatingActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Thank you")
                        .setMessage("We thank you for your feed back, Return to the main page")
                        .setPositiveButton("OK", (dialog, which) -> startAct())
                        .show());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        findViewById(R.id.cancelFB).setOnClickListener(v -> startAct());
    }
    private void startAct(){
        this.runOnUiThread(() -> {
            Intent intent =new Intent(RatingActivity.this,MapsActivity.class);
            intent.putExtra("feedBack","true");
            RatingActivity.this.startActivity(intent);
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
