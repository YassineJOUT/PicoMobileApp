package ma.fstm.ilisi.pico.picomobile.View;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Future;

import ma.fstm.ilisi.pico.picomobile.R;
import ma.fstm.ilisi.pico.picomobile.Utilities.ConfigClass;
import ma.fstm.ilisi.pico.picomobile.Utilities.DownloadImageTask;
import ma.fstm.ilisi.pico.picomobile.Utilities.RealPathUtil;
import ma.fstm.ilisi.pico.picomobile.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    String path;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // hide Edit text


        // get user data :
        ProfileViewModel  profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);


        if(profileViewModel != null){
            profileViewModel.retreiveProfileUser().observe(this,(data)->{
                if(data != null){
                    ConfigClass.fullName = data.get(0);
                    ((TextView)findViewById(R.id.textFullName)).setText(ConfigClass.fullName);
                    Log.e("name",data.get(0));
                    new DownloadImageTask( findViewById(R.id.imgProfile))
                    .execute(ConfigClass.buildUrl("citizens",data.get(1)));
                }
            });
        }

        ((TextView)findViewById(R.id.textPhone)).setText(ConfigClass.phoneNumber);

        //

        findViewById(R.id.btnImageUpload).setOnClickListener((view)->{
            update();
        });

        findViewById(R.id.btnSave).setEnabled(false);
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
                    if(!((EditText)findViewById(R.id.edtFulName)).getText().equals(""))
                        findViewById(R.id.btnSave).setEnabled(true);
                }
                if(!nvPass.equals(reptnvPass) ){
                    (findViewById(R.id.nvPassword)).setBackgroundResource(0);
                    (findViewById(R.id.rptnvPassword)).setBackgroundResource(0);
                    (findViewById(R.id.nvPassword)).setBackgroundResource(R.drawable.edittext_bgrouge);
                    (findViewById(R.id.rptnvPassword)).setBackgroundResource(R.drawable.edittext_bgrouge);
                        findViewById(R.id.btnSave).setEnabled(false);

                }
            }
        });
        findViewById(R.id.imgProfile).setOnClickListener((view)->{
            Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
            fintent.setType("image/jpeg");
            try {
                startActivityForResult(fintent, 100);
            } catch (ActivityNotFoundException e) {
            }
        });
        findViewById(R.id.btnSave).setOnClickListener((view)->{
            if(findViewById(R.id.btnSave).isEnabled()){
                //changing mdp
                if(profileViewModel != null){
                    String nvPassword = ((EditText)findViewById(R.id.edtFulName)).getText().toString();
                    if(!nvPassword.equals(R.id.edtFulName))
                        if(ConfigClass.password.equals(((EditText)findViewById(R.id.ancPassword)).getText().toString()))
                        profileViewModel.ModifyUserPassword(nvPassword).observe(this,(data)->{
                            if(data){
                                if(path != null){
                                    File f = new File(path);

                                    Log.e("path up",path);
                                    Future uploading = Ion.with(ProfileActivity.this)
                                            .load("patch","http://pico.ossrv.nl:9090/api/citizens/image").
                                                    addHeader("Authorization",ConfigClass.token)
                                            .setMultipartFile("image", f)
                                            .asString()
                                            .withResponse()
                                            .setCallback((e, result)-> {
                                                try {
                                                    JSONObject jobj = new JSONObject(result.getResult());
                                                    Toast.makeText(getApplicationContext(), jobj.getString("msg"), Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                            });
                                }

                                Toast.makeText(ProfileActivity.this,
                                        "The password has being changed successfully",
                                        Toast.LENGTH_LONG).show();
                                findViewById(R.id.btnImageUpload).performClick();
                            }
                        });
                        else
                            Toast.makeText(ProfileActivity.this,
                                    "The password is not correct",
                                    Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @SuppressLint("WrongViewCast")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    path = RealPathUtil.getRealPath(this,data.getData());//Log.d("path",path);
                    ((ImageView)findViewById(R.id.imgProfile)).setImageURI(data.getData());
                   // upload.setVisibility(View.VISIBLE);

                }
        }
    }

    public void update() {
        ViewSwitcher switcher = findViewById(R.id.my_switcher);


        ((EditText) findViewById(R.id.edtFulName)).setText(((TextView)findViewById(R.id.textFullName)).getText());
        //((EditText) switcher.getNextView()).setText(FullName);
        switcher.showNext(); //or switcher.showPrevious();

        //TextView myTV = (TextView) switcher.findViewById(R.id);
        //myTV.setText("value");
    }


}
