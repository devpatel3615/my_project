package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    Button login;
    Button singup;
    TextView ForgotePass;

    String url="https://valentinachoco.000webhostapp.com/valentina/login.php";
    JSONParser jsonp=new JSONParser();
    int cnt=0;
    ProgressDialog pDialog=null;
    EditText eduname,edpass;
    String uname=null,pass=null,cid=null;
    SharedPreferences ses=null;
    datahelper db=null;


    public static String sessionLogin="customer";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setTitle("Login");
        setContentView(R.layout.login);
        ses=getSharedPreferences(sessionLogin,MODE_PRIVATE);
        db=new datahelper(login.this);
        singup=(Button) findViewById(R.id.singupb2);
        login=(Button)findViewById(R.id.loginb1);
        ForgotePass=(TextView)findViewById(R.id.forgot_password);
        eduname=findViewById(R.id.userb1);
        edpass=findViewById(R.id.passb1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //   Intent i=new Intent(Login.this,menu_list.class);
                // startActivity(i);
                uname=eduname.getText().toString();
                pass=edpass.getText().toString();
                if(uname.equals(""))
                {
                    eduname.setError("Pls Enter User Name");
                }
                else if(pass.equals(""))
                {
                    edpass.setError("Pls Enter Password");
                }
                else
                {
                    new Authentication().execute();
                }

            }


            class Authentication extends AsyncTask<String,String,String>
            {
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(login.this);
                    pDialog.setMessage("Please wait Submit Record ....");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... strings)
                {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("uname", uname));
                    params.add(new BasicNameValuePair("pass", pass));
                    try
                    {
                        JSONObject obj = jsonp.makeHttpRequest(url, "GET", params);
                        int ans = obj.getInt("success");
                        if (ans == 1)
                        {
                            cnt = 1;
                            cid=obj.getString("cid");
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
                    if (cnt == 1)
                    {
                        SharedPreferences.Editor obj=ses.edit();
                        obj.putString("cid",cid);
                        obj.commit();
                        Toast.makeText(login.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        eduname.setText("");
                        edpass.setText("");
                        db.setlogindetail(Integer.parseInt(cid));
                        Intent i=new Intent(login.this, Fragment_menu.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(login.this, "Sorry, Invalid Details", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        singup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(login.this, singup.class);
                startActivity(intent);
            }
        });
        ForgotePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login.this, forgotpassword.class);
                startActivity(intent);
            }
        });


    }
}