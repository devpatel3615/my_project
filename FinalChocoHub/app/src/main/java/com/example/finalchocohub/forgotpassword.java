package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class forgotpassword extends AppCompatActivity {


    TextView email;
    Button submit;
    String femail = null;
    String fpassword;
    String fphoneno;
    String Tag_array = "fpassword";
    String Tag_password = "cpassword";
    String Tag_phone="cemail";
    SharedPreferences ses = null;
    String url="https://valentinachoco.000webhostapp.com/valentina/forget_password.php";


    String cid = null;
    JSONParser jsonp = new JSONParser();
    int cnt = 0;
    ProgressDialog pDialog = null;
    JSONArray array = null;
    String id = null;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email=(EditText)findViewById(R.id.txt_email);
        setTitle("Forgate Password");
        submit=(Button)findViewById(R.id.btn);
        ActivityCompat.requestPermissions(forgotpassword.this, new String[]{Manifest.permission.SEND_SMS},0);
        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        cid = ses.getString("cid", null);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femail=email.getText().toString();
                if(femail.equalsIgnoreCase(""))
                {
                    email.setError("Enter Email Address");
                }
                else
                {
                    new showpassword().execute();
                }

            }
            class showpassword extends AsyncTask<String, String, String>
            {
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(forgotpassword.this);
                    pDialog.setMessage("Please wait Fetch Password....");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... strings) {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("c_email", femail));
                    JSONObject obj = jsonp.makeHttpRequest(url, "GET", params);
                    try {
                        if (obj.getInt("success") > 0) {
                            cnt = 1;
                            array = obj.getJSONArray(Tag_array);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                fpassword = o.getString(Tag_password);
                                fphoneno=o.getString(Tag_phone);

                            }
                        } else {
                            cnt = 0;
                        }
                    } catch (Exception e) {
                        Log.e("Ex is:-", e + "");
                    }

                    return null;
                }
                protected void onPostExecute(String file_url) {
                    // dismiss the dialog once done
                    pDialog.dismiss();
                    if (cnt == 0) {
                        Toast.makeText(forgotpassword.this, "No Any E-mail Found", Toast.LENGTH_SHORT).show();

                        finish();
                    } else
                    {
                        Toast.makeText(forgotpassword.this, "Successfully Fetch", Toast.LENGTH_SHORT).show();
                        sendSMSMessage();
                        try{
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(fphoneno, null, "Your Password is:"+fpassword, null, null);
                            Toast.makeText(getApplicationContext(), "SMS Sent! to Registered Phone No..",
                                    Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Go and Chack your Message",
                                    Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
            }


            void sendSMSMessage()
            {
                if (ContextCompat.checkSelfPermission(forgotpassword.this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(forgotpassword.this, Manifest.permission.SEND_SMS)) {
                    } else {
                        ActivityCompat.requestPermissions(forgotpassword.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                }
            }
        });
    }
}