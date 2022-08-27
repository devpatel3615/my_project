package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalchocohub.JSONParser;
import com.example.finalchocohub.Product;
import com.example.finalchocohub.Profile;
import com.example.finalchocohub.R;
import com.example.finalchocohub.login;
import com.google.android.material.tabs.TabLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Update_profile<Databasehelper> extends AppCompatActivity {

    TabLayout tb;
    String c_id, cname, caddress, cemail, cphone, cpassword;
    public static String Tag_Array = "customer";
    public static String Tag_emp_name = "cname";
    public static String Tag_emp_id="cid";
    public static String Tag_emp_address = "caddress";
    public static String Tag_emp_email = "cemail";
    public static String Tag_emp_phone = "cphone";
    public static String Tag_emp_password = "cpassword";
    JSONArray array=null;
    AlertDialog.Builder alert = null;
    EditText uname,uaddress, uphone, uemail, upass;
    Button saveupdate;
    Databasehelper db = null;
    SharedPreferences ses = null;

    String updateurl="https://valentinachoco.000webhostapp.com/valentina/Update_Employee.php";
    String url = "https://valentinachoco.000webhostapp.com/valentina/View_Employee.php";
    JSONParser jsonp = new JSONParser();
    ArrayList<HashMap<String, String>> elist = null;
    int cnt = 0;
    ProgressDialog pDialog;
    String cid = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        cid = ses.getString("cid", null);
        uname = (EditText) findViewById(R.id.upname);
        uaddress = (EditText) findViewById(R.id.upaddress);
        uphone = (EditText) findViewById(R.id.upphone);
        uemail = (EditText) findViewById(R.id.upemail);
        upass = (EditText) findViewById(R.id.upassword);
        saveupdate = (Button) findViewById(R.id.btnsave);
////        tb = findViewById(R.id.tabb_layout);
//        tb.addTab(tb.newTab().setText("Update"));
//        tb.setTabGravity(TabLayout.GRAVITY_FILL);
        new getProfile().execute();


        saveupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new saveupdates().execute();
                Intent I=new Intent(Update_profile.this, Profile.class);
                startActivity(I);
            }

            class saveupdates extends AsyncTask<String, String, String> {

                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(Update_profile.this);
                    pDialog.setMessage("Please wait Update Profile....");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... strings) {

                    cname=uname.getText().toString();
                    caddress=uaddress.getText().toString();
                    cphone=uphone.getText().toString();
                    cemail=uemail.getText().toString();
                    cpassword=upass.getText().toString();
//                    employee_designation=udesignation.getText().toString();

                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("cid",cid));
                    params.add(new BasicNameValuePair("name",cname));
                    params.add(new BasicNameValuePair("address",caddress));
                    params.add(new BasicNameValuePair("email",cemail));
                    params.add(new BasicNameValuePair("phone",cphone));

                    JSONObject obj = jsonp.makeHttpRequest(updateurl, "POST", params);
                    try {
                        Log.e("--->", updateurl);
                        if (obj.getInt("success") > 0)
                        {
                            cnt = 1;

                        } else {
                            cnt = 0;
                        }
                    } catch (Exception e) {
                        Log.e("Ex-", e + "");
                    }


                    return null;
                }

                protected void onPostExecute(String file_url) {
                    pDialog.dismiss();

                    if (cnt == 0) {
                        Toast.makeText(Update_profile.this, "Sorry", Toast.LENGTH_SHORT).show();


                    } else {
                        uname.setText(cname);
                        uaddress.setText(caddress);
                        uphone.setText(cphone);
                        uemail.setText(cemail);
                        upass.setText(cpassword);
//                        udesignation.setText(employee_designation);
                        Intent i =new Intent(Update_profile.this,Profile.class);
                        startActivity(i);
                    }
                    finish();
                }
            }
        });
    }





    class getProfile extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Update_profile.this);
            pDialog.setMessage("Please wait Loading Profile....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("cid", cid));
            JSONObject obj = jsonp.makeHttpRequest(url, "GET", params);
            try {
                Log.e("--->", url);
                if (obj.getInt("success") > 0) {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_Array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        c_id=o.getString(Tag_emp_id);
                        cname = o.getString(Tag_emp_name);
                        caddress = o.getString(Tag_emp_address);
                        cphone = o.getString(Tag_emp_phone);
                        cemail = o.getString(Tag_emp_email);
                        cpassword=o.getString(Tag_emp_password);


                    }
                } else {
                    cnt = 0;
                }
            } catch (Exception e) {
                Log.e("Ex-", e + "");
            }
            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (cnt == 0) {
                Toast.makeText(Update_profile.this, "No Profile Found", Toast.LENGTH_SHORT).show();

            } else {
                uname.setText(cname);
                uaddress.setText(caddress);
                uphone.setText(cphone);
                uemail.setText(cemail);
                upass.setText(cpassword);
//                udesignation.setText(employee_designation);
            }
        }

    }
}