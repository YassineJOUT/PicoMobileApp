package ma.fstm.ilisi.pico.picomobile.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.Sockets;

public class RatingActivity extends AppCompatActivity {

    RatingBar rb ;
    Socket sock;
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
                JSONObject obj = new JSONObject();
                try {
                    String comment = ((EditText)findViewById(R.id.fb_Comment)).getText().toString();
                    obj.put("percentage",rating/5);
                    obj.put("comment",comment);
                    obj.put("alarm_id",alarm_id);

                    sock.emit("CITIZEN_FEEDBACK_EVENT", obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}
