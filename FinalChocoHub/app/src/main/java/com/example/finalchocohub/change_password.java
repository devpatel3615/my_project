package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;import java.util.List;

public class change_password extends AppCompatActivity {
    EditText oldpass;
    Button btnsubmit;
    EditText newpass;
    EditText confirmpass;
    String snewpassword;
    String sconfurmpassword;

    String soldpassword=null, cid=null;
    SharedPreferences ses = null;
    String Change_Password = "https://valentinachoco.000webhostapp.com/valentina/Change_Password.php";
    JSONParser jsonp = new JSONParser();
    int cnt = 0;
    LoadingDilog pDialog = null;
    String Tag_array = "fpassword";
    String Tag_password="c_password";
    JSONArray array = null;
    datahelper db = null;
    String id = null;
    String Update_Password="https://valentinachoco.000webhostapp.com/valentina/Update_Password.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldpass = (EditText) findViewById(R.id.oldpass);
        newpass=(EditText)findViewById(R.id.newpass);
        confirmpass=(EditText)findViewById(R.id.confirmpass);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        cid = ses.getString("cid", null);
        db = new datahelper(change_password.this);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soldpassword = oldpass.getText().toString();
                sconfurmpassword=confirmpass.getText().toString();


                snewpassword=newpass.getText().toString();

                if(soldpassword.equalsIgnoreCase(""))
                {
                    oldpass.setError("Please Enter Old Password");
                }
                else if(snewpassword.equalsIgnoreCase(""))

                {
                    newpass.setError("Please Enter New Password");
                }
                else if(sconfurmpassword.equalsIgnoreCase(""))
                {
                    confirmpass.setError("Please Enter Confurm Password");
                }

                else {
                    new fetchpassword().execute();
                }



            }

            class fetchpassword extends AsyncTask<String, String, String> {
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new LoadingDilog(change_password.this);
//                    pDialog.setMessage("Please wait Fetch Password....");
//                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... strings) {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("cid", cid));
                    params.add(new BasicNameValuePair("c_oldpassword", soldpassword));


                    JSONObject obj = jsonp.makeHttpRequest(Change_Password, "GET", params);
                    try {
                        if (obj.getInt("success") > 0)  {

                            cnt = 1;

                            array = obj.getJSONArray(Tag_array);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);


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
                    pDialog.dismiss();
                    if (cnt == 1) {

                        if(soldpassword.matches(snewpassword))
                        {
                            newpass.setText("");
                            newpass.setError("Please Enter New Password");
                        }
                        else if(!sconfurmpassword.matches(snewpassword))
                        {
                            confirmpass.setText("");
                            confirmpass.setError("Please Enter Confurm Password");
                        }
                        else
                        {


                            new updatepassword().execute();
                        }

                    } else {
                        Toast.makeText(change_password.this, "Invalid Old Password Password do not Update", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            class updatepassword extends AsyncTask<String,String,String>
            {
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new LoadingDilog(change_password.this);
//                    pDialog.setMessage("Please wait update Password....");
//                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... strings) {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("cid",cid));
                    params.add(new BasicNameValuePair("c_password",snewpassword));
                    try {
                        JSONObject obj = jsonp.makeHttpRequest(Update_Password, "POST", params);
                        int ans = obj.getInt("success");
                        if (ans == 1) {
                            cnt = 1;
                        } else {
                            cnt = 0;
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("Ex is:-", e + "");
                    }

                    return null;
                }
                protected void onPostExecute(String file_url){
                    pDialog.dismiss();
                    if (cnt == 1)
                    {
                        Toast.makeText(change_password.this, "Update Successfully", Toast.LENGTH_LONG).show();





                    }
                    else
                    {
                        Toast.makeText(change_password.this, "Sorry", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}