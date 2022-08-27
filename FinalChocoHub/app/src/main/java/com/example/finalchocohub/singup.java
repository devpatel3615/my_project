package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class singup extends AppCompatActivity {

    EditText name,address,phone,email,password;
    String   sname,saddress,sphone,semail,spassword;
    Button singup;

    String url="https://valentinachoco.000webhostapp.com/valentina/newuser.php";
    JSONParser jsonp=new JSONParser();
    int cnt=0;
    ProgressDialog pDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Sign Up");
        setContentView(R.layout.activity_singup);

        name=(EditText)findViewById(R.id.siupname);
        address=(EditText)findViewById(R.id.siupaddress);
        phone=(EditText)findViewById(R.id.siupphone);
        email=(EditText)findViewById(R.id.siupemail);
        password=(EditText)findViewById(R.id.siuppassword);
        singup=(Button)findViewById(R.id.btnsiup);
        singup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sname=name.getText().toString();
                saddress=address.getText().toString();
                sphone=phone.getText().toString();
                semail=email.getText().toString();
                spassword=password.getText().toString();


                if(sname.equalsIgnoreCase(""))
                {
                    name.setError("Enter Name");
                }
                else  if(saddress.equalsIgnoreCase(""))
                {
                    address.setError("Enter Address");
                }
                else if(sphone.equalsIgnoreCase(""))
                {
                    phone.setError("Enter Phone No.");
                }
                else if(semail.equalsIgnoreCase(""))
                {
                    email.setError("Enter E-mail");
                }


                else  if(spassword.equalsIgnoreCase(""))
                {
                    password.setError("Enter Password");
                }
                else
                {
                    new InsertRecord().execute();
                    //Toast.makeText(sing_up.this, "Submitted", Toast.LENGTH_SHORT).show();
                }
            }
            class InsertRecord extends AsyncTask<String,String,String>
            {
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(singup.this);
                    pDialog.setMessage("Please wait Submit Record ....");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }
                @Override
                protected String doInBackground(String... strings)
                {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("name", sname));
                    params.add(new BasicNameValuePair("address", saddress));
                    params.add(new BasicNameValuePair("email", semail));
                    params.add(new BasicNameValuePair("phone", sphone));
                    params.add(new BasicNameValuePair("pass", spassword));
                    try {


                        JSONObject obj = jsonp.makeHttpRequest(url, "POST", params);
                        int ans = obj.getInt("success");
                        if (ans == 1) {
                            cnt = 1;
                        } else {
                            cnt = 0;
                        }
                    } catch (Exception e) {
                        Log.e("Ex is:-", e + "");
                    }
                    return null;
                }

                protected void onPostExecute(String file_url)
                {
                    pDialog.dismiss();
                    if (cnt == 1)
                    {
                        Toast.makeText(singup.this, "User Created Successfully", Toast.LENGTH_LONG).show();
                        name.setText("");
                        address.setText("");
                        phone.setText("");
                        email.setText("");
                        password.setText("");

                        Intent i=new Intent(singup.this,login.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(singup.this, "Sorry", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });


    }
}