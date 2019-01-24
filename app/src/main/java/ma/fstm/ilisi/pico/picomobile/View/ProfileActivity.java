package ma.fstm.ilisi.pico.picomobile.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // hide Edit text

        ((TextView)findViewById(R.id.textFullName)).setText(ConfigClass.fullName);
        ((TextView)findViewById(R.id.textPhone)).setText(ConfigClass.phoneNumber);;
        //new DownloadImageTask( findViewById(R.id.bs_imageView))
         //       .execute(ConfigClass.buildUrl("citizen", ));

        findViewById(R.id.btnImageUpload).setOnClickListener((view)->{
            update();
        });

        ((EditText)findViewById(R.id.rptnvPassword)).addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String nvPass = ((EditText)findViewById(R.id.nvPassword)).getText().toString();
                Log.e("nvPass ",nvPass);

                String reptnvPass = ((EditText)findViewById(R.id.rptnvPassword)).getText().toString();

                Log.e("reptnvPass ",reptnvPass);
                if(nvPass.equals(reptnvPass) ){
                    (findViewById(R.id.nvPassword)).setBackgroundResource(0);
                    (findViewById(R.id.rptnvPassword)).setBackgroundResource(0);
                    (findViewById(R.id.nvPassword)).setBackgroundResource(R.drawable.edittext_bgvert);
                    (findViewById(R.id.rptnvPassword)).setBackgroundResource(R.drawable.edittext_bgvert);
                }
                if(!nvPass.equals(reptnvPass) ){
                    (findViewById(R.id.nvPassword)).setBackgroundResource(0);
                    (findViewById(R.id.rptnvPassword)).setBackgroundResource(0);
                    (findViewById(R.id.nvPassword)).setBackgroundResource(R.drawable.edittext_bgrouge);
                    (findViewById(R.id.rptnvPassword)).setBackgroundResource(R.drawable.edittext_bgrouge);
                }
            }
        });
    }

    public void update() {
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);


        ((EditText) findViewById(R.id.edtFulName)).setText(((TextView)findViewById(R.id.textFullName)).getText());
        //((EditText) switcher.getNextView()).setText(FullName);
        switcher.showNext(); //or switcher.showPrevious();

        //TextView myTV = (TextView) switcher.findViewById(R.id);
        //myTV.setText("value");
    }
}
