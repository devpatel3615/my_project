package com.example.finalchocohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YourOrder extends AppCompatActivity {
    //    public static String cid=null;
    //    // public static String Tag_pro_img;
    JSONArray array;
    ListView lv,lvmore;
    TextView oid;
    //    ImageView imageView;
    String url="https://valentinachoco.000webhostapp.com/valentina/view_order.php";


    ProgressDialog pDialog;
    public static String ordid;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    public static NavigationView navigationView;
    int cnt=0;
    public static String pqty=null;
    JSONParser jsonp=new JSONParser();
    AlertDialog.Builder alert=null;
    public static String Tag_Array="order";
    public static String Tag_username="cname";
    public static String Tag_userphone="cphone";
    public static String Tag_useremail="cemail";
    public static String Tag_amount="amount";
    public static String Tag_status="order_tracking";


    public static String Tag_pro_name="product_name";
    public static String Tag_pro_pro_qty="product_qty";
    public static String Tag_tamount="total_amount";
    public static String Tag_oid="order_id";
    public static String orderid;



    //    TextView tvname,tvphone,tvemail,tvamount,tvstatus;
//    String name,phone,email,amount,status;
//
    String cid=null;

    ArrayList<HashMap<String,String>> olist=null;
    ArrayList<HashMap<String,String>> vlist=null;
    SharedPreferences ses=null;
    datahelper db=null;
    String id=null;
    OrderViewAdapter adapter=null;
    //
//
////    String cid=null;
////    String Tag_order_id ="order_id";
////    String Tag_product_id = "product_id";
////    String Tag_product_name = "product_name";
////    String Tag_product_price = "product_price";
////    String Tag_order_disc = "order_disc";
////    String Tag_order_date = "order_date";
////    String Tag_amount = "amount";
////    String Tag_order_delete_date = "order_delete_date";
////    String Tag_cgst = "cgst";
////    String Tag_sgst = "sgst";
////    String Tag_total_amount = "total_amount";
////    String Tag_order_tracking = "order_tracking";
////    String Tag_order_qty = "order_qty";
////    String Tag_acid = "acid";
//


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("ORDER");
//        //ActivityCompat.requestPermissions(My_Order.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        cid=ses.getString("cid",null);

        //oid = ses.getString("oid", null);
        db = new datahelper(YourOrder.this);
        lv = findViewById(R.id.lvorder);
        
        lvmore=findViewById(R.id.lvviewmore);
        ses=getSharedPreferences(login.sessionLogin,MODE_PRIVATE);
        db=new datahelper(YourOrder.this);
//        drawer=(DrawerLayout)findViewById(R.id.drawer);
//        navigationView=(NavigationView)findViewById(R.id.nv);
        oid=(TextView)findViewById(R.id.orderid);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);



        new GetOrder().execute();
    }


    class GetOrder extends AsyncTask<String,String,String>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(YourOrder.this);
            pDialog.setMessage(Html.fromHtml("<font color='#4169e1'> Please Wait Loading Order... </font>"));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            olist=new ArrayList<HashMap<String, String>>();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("cid", cid));
            try {
                JSONObject obj = jsonp.makeHttpRequest(url,"GET",params);
                int ans = obj.getInt("success");
                if (ans == 1) {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_Array);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        String username= o.getString(Tag_username);
                        String userphone = o.getString(Tag_userphone);
                        String useremail=o.getString(Tag_useremail);
                        String amount=o.getString(Tag_amount);
                        String status=o.getString(Tag_status);
                        orderid=o.getString(Tag_oid);
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(Tag_username, username);
                        hashMap.put(Tag_userphone, userphone);
                        hashMap.put(Tag_useremail, useremail);
                        hashMap.put(Tag_amount,amount);
                        hashMap.put(Tag_status,status);
                        hashMap.put(Tag_oid,orderid);
                        olist.add(hashMap);


                    }
                } else {
                    cnt = 0;
                }
            }
            catch (Exception e)
            {
                Log.e("Ex-",e+"");
            }

            return null;
        }
        protected void onPostExecute(String file_url)
        {
            pDialog.dismiss();
            if(cnt==0)
            {
                Toast.makeText(YourOrder.this, "No Order Found", Toast.LENGTH_SHORT).show();
            }
            else
            {
                adapter=new OrderViewAdapter(YourOrder.this,olist);
                lv.setAdapter(adapter);

            }
        }
    }

    public class OrderViewAdapter  extends BaseAdapter
    {
        Context context;
        String oid=null;
        ProgressDialog pDialog=null;
        datahelper db=null;

        JSONParser  jsonp=new JSONParser();
        int cnt=0;
        ArrayList<HashMap<String,String>> olist;

        OrderViewAdapter(Context context, ArrayList<HashMap<String,String>> olist)
        {
            this.context=context;
            this.olist=olist;
        }

        @Override
        public int getCount() {
            return olist.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final TextView tvname;
            final TextView tvphone;
            final TextView tvemail;
            final TextView tvamount;
            final TextView tvstatus;
            final TextView tvoid;
            final Button viewmore;

            final LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View custome;

            custome=inflater.inflate(R.layout.row_order,viewGroup,false);
            HashMap<String,String>map=olist.get(i);
            tvname=(TextView)custome.findViewById(R.id.username);
            tvname.setText(map.get(YourOrder.Tag_username));
            tvphone=(TextView)custome.findViewById(R.id.userphone);
            tvphone.setText(map.get(YourOrder.Tag_userphone));
            tvemail=(TextView)custome.findViewById(R.id.useremail);
            tvemail.setText(map.get(YourOrder.Tag_useremail));
            tvamount=(TextView)custome.findViewById(R.id.amount);
            tvamount.setText(map.get(YourOrder.Tag_amount));
            tvstatus=(TextView)custome.findViewById(R.id.status);
            tvstatus.setText(map.get(YourOrder.Tag_status));
            tvoid=(TextView)custome.findViewById(R.id.orderid);
            tvoid.setText(map.get(YourOrder.Tag_oid));
            viewmore=(Button)custome.findViewById(R.id.Viewmore);
            db=new datahelper(context);

            viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String oid=((TextView)view.findViewById(R.id.orderid)).getText().toString();
//                    SharedPreferences.Editor obj=ses.edit();
//                    obj.putString("oid",oid);
//                    obj.commit();
                    ordid=tvoid.getText().toString();
                    Intent i=new Intent(context,View_Order_Detail.class);
                    context.startActivity(i);
                }
            });
            return custome;
        }
    }
}