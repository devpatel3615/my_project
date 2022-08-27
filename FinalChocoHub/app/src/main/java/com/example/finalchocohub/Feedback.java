package com.example.finalchocohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Feedback extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    public static NavigationView navigationView;
    AlertDialog.Builder alert=null;
    EditText ledfeedback;
    Button btnsubmit;
    RatingBar rv;
    TextView tvratings;
    String sfeedback,cid,sratingbar;
    SharedPreferences ses=null;
    String url="https://valentinachoco.000webhostapp.com/valentina/feedback.php";
    JSONParser  jsonp=new JSONParser();
    int cnt=0;
    LoadingDilog pDialog=null;

    datahelper db=null;
    String id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Feedback");
        ledfeedback=(EditText)findViewById(R.id.ledfeedback);
        btnsubmit=(Button)findViewById(R.id.btnsubmit);
        tvratings=(TextView)findViewById(R.id.tvrating);
        rv=(RatingBar)findViewById(R.id.Ratingbar);


        ses=getSharedPreferences(login.sessionLogin,MODE_PRIVATE);
        cid=ses.getString("cid",null);
        db=new datahelper(Feedback.this);
        drawer=(DrawerLayout)findViewById(R.id.drawer);
        navigationView=(NavigationView)findViewById(R.id.nv);
        toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        rv.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                Toast.makeText(Feedback.this, rating+"", Toast.LENGTH_SHORT).show();
                tvratings.setText(rating+"");
            }
        });

        drawer=(DrawerLayout)findViewById(R.id.drawer);
        navigationView=(NavigationView)findViewById(R.id.nv);
        toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                int id=menuItem.getItemId();
                switch (id)
                {
                    case R.id.nav_Chang_password:
                        Intent i_pass=new Intent(Feedback.this,change_password.class);
                        startActivity(i_pass);
                        break;

                    case R.id.nav_My_cart:
                        Intent i_category=new Intent(Feedback.this, YourCart.class);
                        startActivity(i_category);
                        break;
                    case R.id.nav_Complain:
                        Intent i_beg=new Intent(Feedback.this,Complain.class);
                        startActivity(i_beg);
                        break;

                    case R.id.nav_brochure:
                        Intent i_brochure=new Intent(Feedback.this, PDF_download.class);
                        startActivity(i_brochure);
                        break;

                    case R.id.nav_feedback:
                        Intent i_Whishlist=new Intent(Feedback.this,Feedback.class);
                        startActivity(i_Whishlist);
                        break;

                    case R.id.nav_logout:
                        android.app.AlertDialog.Builder alert=new android.app.AlertDialog.Builder(Feedback.this);
                        alert.setTitle("Logout");
                        alert.setMessage("Are You sure You want to Loged Out !");
                        alert.setIcon(R.drawable.logout_org);
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent i=new Intent(Feedback.this,login.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alert.create();
                        alert.show();
                        break;

                    default:
                        return true;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfeedback=ledfeedback.getText().toString();
                sratingbar=tvratings.getText().toString();

                if(sfeedback.equalsIgnoreCase(""))
                {
                    ledfeedback.setError("Please Enter Feedback");
                }
                else
                {
                    new InsertRecord().execute();
                }


            }


            class InsertRecord extends AsyncTask<String,String,String>
            {
                protected void onPreExecute()
                {
                    super.onPreExecute();
                    pDialog = new LoadingDilog(Feedback.this);
                    pDialog.setTitle("Please wait Submit feedback....");
//                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... strings) {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("cid",cid));
                    params.add(new BasicNameValuePair("feedback", sfeedback));
                    params.add(new BasicNameValuePair("rating",sratingbar));
                    try {
                        JSONObject obj = jsonp.makeHttpRequest(url, "POST", params);
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
                        Toast.makeText(Feedback.this, "Feedback Successfully", Toast.LENGTH_LONG).show();
                        ledfeedback.setText("");

                        Toast.makeText(Feedback.this, "Thanks for the feedback", Toast.LENGTH_LONG).show();
                        ledfeedback.setText("");
                    }
                    else
                    {
                        Toast.makeText(Feedback.this, "Sorry", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }


}