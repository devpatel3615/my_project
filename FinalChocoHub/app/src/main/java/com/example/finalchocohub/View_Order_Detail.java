package com.example.finalchocohub;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class View_Order_Detail extends AppCompatActivity {
    String Tag_array = "view";
    String Tag_vorderid = "order_id";
    String Tag_pname="product_name";
    String Tag_pqty="order_qty";
    String Tag_pprice="product_price";
    String Tag_status="order_tracking";
    String order_tracking,price=null,qty=null,product_name=null;
    Dialog mydialog;
    JSONArray array = null;
    View_Order_Detail.viewmoreadepter adapter=null;
    AlertDialog.Builder alert = null;
    ListView lv;
    datahelper db = null;
    String cid = null;
    Button Viewmore;
    SharedPreferences ses = null;
    String viewurl = "https://valentinachoco.000webhostapp.com/valentina/view_more.php";
    JSONParser jsonp = new JSONParser();
    ArrayList<HashMap<String, String>> vlist = null;
    int cnt = 0;
    ProgressDialog pDialog = null;
    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__order__detail);
        setTitle("ViewMore");
        lv = (ListView) findViewById(R.id.lvviewmore);
        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        cid = ses.getString("cid", null);
        Viewmore=findViewById(R.id.Viewmore);
        db = new datahelper(View_Order_Detail.this);
        new getview().execute();
        /////Animation animation= AnimationUtils.loadAnimation(View_Order_Detail.this,R.anim.bottom_animation);
        //lv.startAnimation(animation);
        mydialog=new Dialog(this);



    }

    private void openlistener() {
    }


//    public void showpop(View v)
//    {
//
//        TextView Sclose;
//        Button done;
//        mydialog.setContentView(R.layout.row_view_list);
//        Sclose = (TextView) mydialog.findViewById(R.id.close);
//
//
//        Sclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mydialog.dismiss();
//            }
//        });
//        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        mydialog.show();
//    }


    class getview extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(View_Order_Detail.this);
            //  pDialog.setMessage(Html.fromHtml("<font color='#056BF9'> Please Wait Submit Feedback \n ... </font>"));
            pDialog.setMessage(Html.fromHtml("<font color='#4169e1'> Please Wait Submit Order... </font>"));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings)
        {
            vlist=new ArrayList<HashMap<String, String>>();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("oid", YourOrder.ordid));
            try {
                JSONObject obj = jsonp.makeHttpRequest(viewurl,"GET",params);
                int ans = obj.getInt("success");
                if (ans == 1) {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        product_name = o.getString(Tag_pname);
                        qty = o.getString(Tag_pqty);

                        price = o.getString(Tag_pprice);
                        order_tracking = o.getString(Tag_status);
                    }
                    String aqty[]=qty.split(",");
                    String aprice[]=price.split(",");
                    String apname[]=product_name.split(",");
                    for(int k=0;k<aqty.length;k++)
                    {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(Tag_pname, apname[k]);
                        hashMap.put(Tag_pqty,aqty[k]);
                        hashMap.put(Tag_pprice,aprice[k]);
                        hashMap.put(Tag_status,order_tracking);
                        if(order_tracking.equals("2"))
                        {
                            order_tracking="Cancel";
                        }

                        vlist.add(hashMap);
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
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (cnt == 0) {
                Toast.makeText(View_Order_Detail.this, "No Any Order Found", Toast.LENGTH_SHORT).show();

            } else {
                adapter= new View_Order_Detail.viewmoreadepter(View_Order_Detail.this,vlist);
                lv.setAdapter(adapter);
            }

        }

    }
    public class viewmoreadepter extends BaseAdapter
    {
        Context context;

        String oid=null;
        String cencalourls="https://valentinachoco.000webhostapp.com/valentina/cancle_order.php";
        ProgressDialog pDialog = null;
        datahelper db = null;

        JSONParser jsonp = new JSONParser();
        int cnt = 0;
        ArrayList<HashMap<String, String>> vlist;
        viewmoreadepter(Context context, ArrayList<HashMap<String, String>> vlist) {
            this.context = context;
            this.vlist = vlist;
        }

        @Override
        public int getCount() {
            return vlist.size();
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
            final TextView proname;
            final TextView proqulity;
            final TextView proprice;
            final TextView procompany;
            final TextView ostatus;
            Button btnocencal;
            Button payment;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View custome;

            custome = inflater.inflate(R.layout.row_view_list, viewGroup, false);
            HashMap<String, String> map = vlist.get(i);
            proname = (TextView) custome.findViewById(R.id.pname);
            proname.setText(map.get(Tag_pname));
            proqulity = (TextView) custome.findViewById(R.id.oqty);
            proqulity.setText(map.get(Tag_pqty));
//            Log.e("QTY is:- ",map.get(Tag_pqty));
            proprice = (TextView) custome.findViewById(R.id.pqprice);
            proprice.setText(map.get(Tag_pprice));
            Log.e("Price is:- ",map.get(Tag_pprice));
            //          ostatus=(TextView)custome.findViewById(R.id.ordstatus);
//            ostatus.setText(map.get(Tag_status));
            payment=(Button)custome.findViewById(R.id.payment);
            btnocencal=(Button)custome.findViewById(R.id.btnocencal);
            btnocencal.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AlertDialog.Builder alert=new AlertDialog.Builder(context);
                    alert.setTitle("Cancel");
                    alert.setMessage("Are You Sure Cancel Complain");
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new cencalorder().execute();
                        }
                    });
                    alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, "you click No Button", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.show();
                }
                class cencalorder extends AsyncTask<String,String,String>
                {
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(context);
                        pDialog.setMessage("Please wait Order Cancel....");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(true);
                        pDialog.show();
                    }
                    @Override
                    protected String doInBackground(String... strings) {
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("oid",YourOrder.ordid));
                        try {
                            JSONObject obj = jsonp.makeHttpRequest(cencalourls, "POST", params);
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
                            Toast.makeText(context, "Order Cancel Successfully", Toast.LENGTH_LONG).show();
                            Intent i=new Intent(View_Order_Detail.this,YourOrder.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(context, "Order Not Cancel", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(View_Order_Detail.this, SelectPayment.class);
                    startActivity(intent);
                }
            });
            db=new datahelper(context);
            return custome;
        }
    }
}