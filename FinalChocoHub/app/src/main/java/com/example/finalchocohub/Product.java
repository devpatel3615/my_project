package com.example.finalchocohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Product extends AppCompatActivity
{
    public static String cid=null;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    ImageView hart,imageView;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    public static NavigationView navigationView;
    JSONArray array;
    ListView lv;
    String id=null;
    String url = "https://valentinachoco.000webhostapp.com/valentina/view_Product.php";
    ProgressDialog pDialog;
    int cnt = 0;
    JSONParser jsonp = new JSONParser();
    AlertDialog.Builder alert = null;
    public static String Tag_Array = "product";
    public static String Tag_pro_id = "product_id";
    public static String Tag_pro_name = "product_name";
    public static String Tag_pro_price="product_price";
    public static String Tag_pro_pro_qty="product_qty";
    public static String Tag_pro_disc="product_disc";
    public static String Tag_pro_image="product_image";


    String catid = null;
    ArrayList<HashMap<String, String>> plist = null;
    SharedPreferences ses = null;
    datahelper db = null;
    ProductViewAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        hart = findViewById(R.id.hart_image);
        imageView = findViewById(R.id.imageView);
        setTitle("Product");
        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        catid = ses.getString("catid", null);
        cid=ses.getString("cid",null);
        db = new datahelper(Product.this);

        lv = findViewById(R.id.catview);
        new Product.GetProduct().execute();
        Toolbar toolbar = findViewById(R.id.toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer);
        navigationView=(NavigationView)findViewById(R.id.nv);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.green1));



        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int name=menuItem.getItemId();
                switch (name)
                {
                    case R.id.nav_Chang_password:
                        Intent i_category=new Intent(Product.this,change_password.class);
                        startActivity(i_category);
                        break;
                    case R.id.nav_My_cart:
                        Intent i_beg=new Intent(Product.this,YourCart.class);
                        startActivity(i_beg);
                        break;
                    case R.id.nav_Complain:
                        Intent i_Whishlist=new Intent(Product.this,Complain.class);
                        startActivity(i_Whishlist);
                        break;
                    case R.id.nav_brochure:
                        Intent i_viewcart=new Intent(Product.this,PDF_download.class);
                        startActivity(i_viewcart);
                        break;
                    case R.id.nav_feedback:
                        Intent i_feedback=new Intent(Product.this,Feedback.class);
                        startActivity(i_feedback);
                        break;
                    case R.id.nav_logout:
                        androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(Product.this);
                        alert.setTitle("Logout");
                        alert.setMessage("Are You sure You want to Loged Out !");
                        alert.setIcon(R.drawable.nav_log_out);
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                SharedPreferences.Editor obj=ses.edit();
                                obj.clear();
                                obj.commit();
                                int deleteResponce=db.delete(id);
                                if(deleteResponce>0) {




                                    Toast.makeText(Product.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                    Intent p = new Intent(Product.this, login.class);
                                    startActivity(p);

                                }
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

    }



    class GetProduct extends AsyncTask<String, String, String>
    {

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(Product.this);
            pDialog.setMessage("Please wait Loading Product....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {

            plist = new ArrayList<HashMap<String, String>>();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("catid", catid));
            try {
                JSONObject obj = jsonp.makeHttpRequest(url, "GET", params);
                int ans = obj.getInt("success");
                if (ans == 1)
                {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_Array);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        String product_id = o.getString(Tag_pro_id);
                        String product_name = o.getString(Tag_pro_name);
                        String product_price=o.getString(Tag_pro_price);
                        String product_qty=o.getString(Tag_pro_pro_qty);
                        String product_disc=o.getString(Tag_pro_disc);
                        String product_image=o.getString(Tag_pro_image);


                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(Tag_pro_id, product_id);
                        hashMap.put(Tag_pro_name, product_name);
                        hashMap.put(Tag_pro_price,product_price);
                        hashMap.put(Tag_pro_pro_qty,product_qty);
                        hashMap.put(Tag_pro_disc,product_disc);
                        hashMap.put(Tag_pro_image,"https://valentinachoco.000webhostapp.com/"+product_image);


                        plist.add(hashMap);

                    }
                }
                else
                {
                    cnt = 0;
                }
            }

            catch (Exception e)
            {
                Log.e("Ex-", e + "");
            }

            return null;
        }
        protected void onPostExecute(String file_url)
        {
            pDialog.dismiss();
            if(cnt==0)
            {
                Toast.makeText(Product.this, "No Product Found", Toast.LENGTH_SHORT).show();
            }
            else
            {

                adapter=new ProductViewAdapter(Product.this,plist);
                lv.setAdapter(adapter);
            }
        }

    }
}