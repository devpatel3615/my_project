package com.example.finalchocohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YourCart extends AppCompatActivity {
    StringBuffer sbpid=null;
    StringBuffer pro_name=null;
    StringBuffer pro_price=null;
    StringBuffer oqty=null;

    StringBuffer ac_id=null;



    String url="https://valentinachoco.000webhostapp.com/valentina/view_add_to_cart.php";
    String orderURL="https://valentinachoco.000webhostapp.com/valentina/order.php";
    int cnt;
    long amount=0;
    String cid=null,Name_product=null;
    String proid=null;
    double sgst=0.0,cgst=0.0,totalamount=0.0;
    ProgressDialog pDialog;
    ArrayList<HashMap<String,String>> slist=null;
    JSONParser jsonp=new JSONParser();
    SharedPreferences ses=null;
    public static String tagArray="cartdetail";
    public static String tagPid="product_id";
    public static String tagPname="product_name";
    public static String tagqty="product_qty";
    public static String tagprice="product_price";
    public static String tagAcid="acid";
    public static String tagimage="product_image";

    ListView lv;

    CartListViewAdapter adapter=null;
    Button order;
    JSONArray array=null;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    public static NavigationView navigationView;
    TextView tvamount,tvsgst,tvcgst,tvtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setTitle("MyCart");
        lv=(ListView)findViewById(R.id.lvcart);
        tvamount=findViewById(R.id.tvamountv);
        tvcgst=findViewById(R.id.tvcgstv);
        tvsgst=findViewById(R.id.tvsgstv);
        order=findViewById(R.id.btnAPPOINTEMENT);
        tvtotal=findViewById(R.id.tvtotalv);
        ses=getSharedPreferences(login.sessionLogin,MODE_PRIVATE);
        cid=ses.getString("cid",null);
        Log.e("Cid is",cid);
        new SelectCartDetail().execute();

        order.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                new order().execute();
            }
            class order extends AsyncTask<String,String,String>
            {
                protected void onPreExecute()
                {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(YourCart.this);
                    pDialog.setMessage("Please wait create your order....");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }
                @Override
                protected String doInBackground(String... arg0)
                {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("cid",cid));
//                        params.add(new BasicNameValuePair("productname", Name_product));
                    params.add(new BasicNameValuePair("proid",sbpid+""));
                    params.add(new BasicNameValuePair("proname",pro_name+""));
                    params.add(new BasicNameValuePair("proprice", pro_price+""));
                    params.add(new BasicNameValuePair("acd_id", ac_id+""));
                    params.add(new BasicNameValuePair("proamount", amount+""));
                    params.add(new BasicNameValuePair("ord_cgst", cgst+""));
                    params.add(new BasicNameValuePair("ord_sgst", sgst+""));
                    params.add(new BasicNameValuePair("totalamount", totalamount+""));
                    params.add(new BasicNameValuePair("oqty",oqty+""));



                    try
                    {
                        JSONObject obj = jsonp.makeHttpRequest(orderURL, "POST", params);
                        int ans = obj.getInt("success");
                        if (ans == 1) {
                            cnt = 1;
                        } else {
                            cnt = 0;
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("Exception is :", e + "");
                    }

                    return null;
                }

                protected void onPostExecute(String file_url)
                {
                    pDialog.dismiss();
                    if (cnt == 1)
                    {
                        Toast.makeText(YourCart.this, "Successfully order", Toast.LENGTH_LONG).show();
                        //Intent in=new Intent(YourCart.this,YourCart.class);
                        // YourCart.this.startActivity(in);
                    } else {

                        Toast.makeText(YourCart.this, "Sorry ", Toast.LENGTH_LONG).show();
                    }

                }



            }
        });


        setSupportActionBar(toolbar);
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
                        Intent i_pass=new Intent(YourCart.this,change_password.class);
                        startActivity(i_pass);
                        break;

                    case R.id.nav_My_cart:
                        Intent i_category=new Intent(YourCart.this, YourCart.class);
                        startActivity(i_category);
                        break;
                    case R.id.nav_Complain:
                        Intent i_beg=new Intent(YourCart.this,Complain.class);
                        startActivity(i_beg);
                        break;

                    case R.id.nav_brochure:
                        Intent i_brochure=new Intent(YourCart.this, PDF_download.class);
                        startActivity(i_brochure);
                        break;

                    case R.id.nav_feedback:
                        Intent i_Whishlist=new Intent(YourCart.this,Feedback.class);
                        startActivity(i_Whishlist);
                        break;

                    case R.id.nav_logout:
                        AlertDialog.Builder alert=new AlertDialog.Builder(YourCart.this);
                        alert.setTitle("Logout");
                        alert.setMessage("Are You sure You want to Loged Out !");
                        alert.setIcon(R.drawable.logout_org);
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent i=new Intent(YourCart.this,login.class);
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

    }
    class SelectCartDetail extends AsyncTask<String,String,String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(YourCart.this);
            pDialog.setMessage("Please wait....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings)
        {
            // buffer=new StringBuffer();
            sbpid=new StringBuffer();
            pro_name=new StringBuffer();
            pro_price=new StringBuffer();

            oqty=new StringBuffer();

            ac_id=new StringBuffer();
            slist=new ArrayList<HashMap<String, String>>();
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cid",cid));
            JSONObject obj=jsonp.makeHttpRequest(url,"GET",params);
            try
            {
                if(obj.getInt("success")==0)
                {
                    cnt=0;
                }
                else
                {
                    cnt=1;
                    array = obj.getJSONArray(tagArray);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        String prodname = o.getString(tagPname);
                        String prodprice = o.getString(tagprice);
                        String pid = o.getString(tagPid);
                        String acid=o.getString(tagAcid);
                        String qty=o.getString(tagqty);
                        String proimg=o.getString(tagimage);

                        sbpid.append(pid+",");
                        pro_name.append(prodname+",");
                        pro_price.append(prodprice+",");
                        ac_id.append(acid+",");
                        oqty.append(qty+",");

                        // buffer.append(acid+",");

                        amount=amount+((Long.parseLong(prodprice))*(Long.parseLong(qty)));
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(tagPname, prodname);
                        map.put(tagprice, prodprice);
                        map.put(tagPid, pid);
                        map.put(tagqty,qty);
                        map.put(tagAcid,acid);
                        map.put(tagimage,proimg);
                        slist.add(map);
                    }
                    sgst=(amount*2.5)/100;
                    cgst=(amount*2.5)/100;
                    totalamount=amount+sgst+cgst;
                }
            }
            catch (Exception e)
            {

            }

            return null;
        }

        protected void onPostExecute(String file_url)
        {
            pDialog.dismiss();
            if(cnt==0)
            {
                Toast.makeText(YourCart.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                tvamount.setText(amount+"");
                tvsgst.setText(sgst+"");
                tvcgst.setText(cgst+"");
                tvtotal.setText(totalamount+"");
                adapter = new CartListViewAdapter(YourCart.this, slist);
                lv.setAdapter(adapter);
            }
        }

    }
}
