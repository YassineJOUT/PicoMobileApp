package ma.fstm.ilisi.pico.picomobile.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //CITIZEN_FEEDBACK_EVENT (alarm_id,percentage,comment) to send
                FBrating = rating;


            }
        });

        findViewById(R.id.sendFB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject obj = new JSONObject();
                try {
                    String comment = ((EditText)findViewById(R.id.fb_Comment)).getText().toString();
                    obj.put("percentage",FBrating/5);
                    obj.put("comment",comment);
                    obj.put("alarm_id",alarm_id);

                    sock.emit("CITIZEN_FEEDBACK_EVENT", obj);
                    RatingActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(RatingActivity.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Thank you")
                                    .setMessage("We thank you for your feed back, Return to the main page")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startAct();
                                        }

                                    })
                                    .show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.cancelFB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct();

            }
        });
    }
    private void startAct(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(RatingActivity.this,MapsActivity.class);
                intent.putExtra("feedBack","true");
                RatingActivity.this.startActivity(intent);
            }
        });
    }
}
