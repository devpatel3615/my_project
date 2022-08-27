package com.example.finalchocohub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Payment extends AppCompatActivity {

    String feedback;
    String Tag_array = "payment";
    public static String Tag_order_id="order_id";
    public static String Tag_orderdate="pay_date";
    public static String Tag_amount="amount";
    public static String Tag_cname="cname";
    public static String Tag_caddress="caddress";
    String Tag_cphone="cphone";
    String Tag_option="option";
    String Tag_pname ="product_name";
    JSONArray array = null;
    AlertDialog.Builder alert = null;
    ListView lv;
    datahelper db = null;
    String cid = null;
    SharedPreferences ses = null;
    String url = "https://valentinachoco.000webhostapp.com/valentina/view_payment.php";
    JSONParser jsonp = new JSONParser();
    ArrayList<HashMap<String, String>> plist = null;
    int cnt = 0;
    ProgressDialog pDialog = null;
    String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Payment");
        setContentView(R.layout.activity_payment);

        lv = (ListView) findViewById(R.id.pview);
        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        cid = ses.getString("cid", null);
        setTitle("Payment");
        db = new datahelper(Payment.this);
        new getpayment().execute();

    }
    class getpayment extends AsyncTask<String, String, String>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Payment.this);
            // pDialog.setMessage("Please wait Fetch Review....");
            //  pDialog.setMessage(Html.fromHtml("<font color='#056BF9'> Please Wait ... </font>"));
            pDialog.setMessage(Html.fromHtml("<font color='#4169e1'> Please Wait for Payment... </font>"));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            plist = new ArrayList<HashMap<String, String>>();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("cid",cid));

            JSONObject obj = jsonp.makeHttpRequest(url, "GET", params);
            try {
                if (obj.getInt("success") > 0) {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        String oid = o.getString(Tag_order_id);
                        String pdate = o.getString(Tag_orderdate);
                        String pamount = o.getString(Tag_amount);
                        String cname = o.getString(Tag_cname);
                        String caddress = o.getString(Tag_caddress);
                        String cphone = o.getString(Tag_cphone);
                        String coption = o.getString(Tag_option);
                        String pname = o.getString(Tag_pname);



                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(Tag_order_id, oid);
                        hashMap.put(Tag_orderdate, pdate);
                        hashMap.put(Tag_amount, pamount);
                        hashMap.put(Tag_cname, cname );
                        hashMap.put(Tag_caddress, caddress);
                        hashMap.put(Tag_cphone, cphone);
                        hashMap.put(Tag_option, coption);
                        hashMap.put(Tag_pname, pname);

                        plist.add(hashMap);

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
                Toast.makeText(Payment.this, "No Any Payment Found", Toast.LENGTH_SHORT).show();

            } else {
                SimpleAdapter adps = new SimpleAdapter(Payment.this, plist, R.layout.row_payment, new String[]{Tag_order_id, Tag_orderdate, Tag_amount, Tag_cname, Tag_caddress, Tag_cphone, Tag_option, Tag_pname}, new int[]{R.id.o_id, R.id.pdate, R.id.pamount,R.id.cname, R.id.caddress, R.id.cphone, R.id.ptype, R.id.prname});
                lv.setAdapter(adps);
            }
        }
    }
}
