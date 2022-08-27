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
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectPayment extends AppCompatActivity {
    JSONArray array;
    RadioGroup rg;
    RadioButton rb;
    RadioButton radio_online;
    Button payment;


//    Button btnExpandBottomSheet, btnCollapseBottomSheet;
    TextView changingState;
    String cid;
    SharedPreferences ses=null;
    TextView address,amounttotal,damount,ttamount,pinstallation,showtext;
    JSONParser  jsonp=new JSONParser();
    int cnt=0;
    ProgressDialog pDialog=null;
    String Tag_array="payment";
    public static String Tag_paddress="caddress";
    public static String Tag_pamount="total_amount";
    String paddress,ptamount;
    String url ="https://valentinachoco.000webhostapp.com/valentina/paymentdetail.php";
    String optionurl="https://valentinachoco.000webhostapp.com/valentina/submit_payment.php";

    datahelper db=null;
    String id=null;
    String selectoption,paytotalamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);setTitle("Payment");
        ses=getSharedPreferences(login.sessionLogin,MODE_PRIVATE);
        cid=ses.getString("cid",null);
        db=new datahelper(SelectPayment.this);
        address=(TextView)findViewById(R.id.taddress);
        amounttotal=(TextView)findViewById(R.id.amount_total);
        damount=(TextView)findViewById(R.id.d_cost);
        ttamount=(TextView)findViewById(R.id.total_amount);
        pinstallation=(TextView)findViewById(R.id.install_amount);
        rg=(RadioGroup)findViewById(R.id.radiogrp);
        radio_online=(RadioButton)findViewById(R.id.radio_online);
        showtext=(TextView)findViewById(R.id.showtext);
        payment=(Button)findViewById(R.id.payment);
        rg.check(R.id.radio_offline);



//        btnExpandBottomSheet = findViewById(R.id.btn_expand_bottom_sheet);
//        btnCollapseBottomSheet = findViewById(R.id.btn_collapse_bottom_sheet);
        changingState = findViewById(R.id.changing_state);



//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        btnExpandBottomSheet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });


        pinstallation.setText("Free Intallation");
        new getorderpaymentitem().execute();

       /* radio_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectPayment.this);
                alertDialog.setTitle("AlertDialog");
                String[] items = {"Paytm","Phone pay","Google pay","Pay pal"};
                int checkedItem = 1;
                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent i=new Intent(SelectPayment.this,OnlinePayment.class);
                                getDrawable(R.drawable.openedfolder);
                                startActivity(i);
                                break;
                            case 1:
                                Intent pp=new Intent(SelectPayment.this,PhonePay.class);
                                startActivity(pp);
                                break;
                            case 2:
                                //  Intent gp=new Intent(SelectPayment.this,Google_pay.class);
                                // startActivity(gp);
                                break;
                            case 3:
                                // Intent ppl=new Intent(SelectPayment.this,Pay_pal.class);
                                //  startActivity(ppl);
                                break;
                        }
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();
            }
        });*/


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rb=(RadioButton)findViewById(rg.getCheckedRadioButtonId());
                String str=rb.getText().toString();
                showtext.setText(str);
                selectoption=showtext.getText().toString();
                paytotalamount=ttamount.getText().toString();
                if(selectoption.equalsIgnoreCase("Cash on Delivery"))
                {

                    new insertpayment().execute();
                }
                else
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectPayment.this);
                    alertDialog.setTitle("Select Payment");
                    alertDialog.setIcon(R.drawable.payment1);
                    String[] items = {"Paytm","Phone pay","Google pay","Pay pal"};
                    int checkedItem = 1;
                    alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent i=new Intent(SelectPayment.this,OnlinePayment.class);
                                    new insertpayment().execute();
                                    startActivity(i);
                                    break;
                                case 1:
                                    Intent pp=new Intent(SelectPayment.this,PhonePay.class);

                                    startActivity(pp);
                                    new insertpayment().execute();
                                    break;
                                case 2:
                                    Intent gp=new Intent(SelectPayment.this,Google_pay.class);

                                    startActivity(gp);
                                    break;
                                case 3:
                                    Intent ppl=new Intent(SelectPayment.this,Pay_pal.class);

                                    startActivity(ppl);
                                    break;


                            }
                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.setCanceledOnTouchOutside(true);
                    alert.show();
                    // Intent i_Whishlist=new Intent(SelectPayment.this,OnlinePayment.class);
                    // startActivity(i_Whishlist);
                    //  new insertpayment().execute();

                    // Toast.makeText(SelectPayment.this, "hdH", Toast.LENGTH_SHORT).show();
                }

            }



            class insertpayment extends AsyncTask<String,String,String>
            {
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(SelectPayment.this);
                    //  pDialog.setMessage(Html.fromHtml("<font color='#056BF9'> Please Wait Submit Feedback \n ... </font>"));
                    pDialog.setMessage(Html.fromHtml("<font color='#4169e1'> Please Wait For Submit Payment... </font>"));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... strings) {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("cid",cid));
                    params.add(new BasicNameValuePair("oid", YourOrder.ordid));
                    params.add(new BasicNameValuePair("option", selectoption));
                    params.add(new BasicNameValuePair("totalamount",paytotalamount));
                    try {
                        JSONObject obj = jsonp.makeHttpRequest(optionurl, "POST", params);
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
                        Intent ppl=new Intent(SelectPayment.this,Thank_payment.class);
                        startActivity(ppl);
                        Toast.makeText(SelectPayment.this, "Payment Successfully Submit", Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                        Toast.makeText(SelectPayment.this, "You Already Pay", Toast.LENGTH_LONG).show();
                        //Toasty.info(SelectPayment.this, "You Already Pay", Toast.LENGTH_SHORT, true).show();
                    }
                }
            }
        });




    }
    class getorderpaymentitem extends AsyncTask<String, String, String>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectPayment.this);
            // pDialog.setMessage("Please wait For Your Profile....");
            pDialog.setMessage(Html.fromHtml("<font color='#4169e1'> Please Wait for Your Payment Details... </font>"));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("oid", YourOrder.ordid));
            JSONObject obj = jsonp.makeHttpRequest(url, "GET", params);
            try {
                if (obj.getInt("success") > 0) {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        paddress=o.getString(Tag_paddress);
                        ptamount=o.getString(Tag_pamount);
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
                Toast.makeText(SelectPayment.this, "No Any Detail found", Toast.LENGTH_SHORT).show();

            } else {
                address.setText(paddress);
                amounttotal.setText(ptamount);
                if(Integer.parseInt(ptamount)<7000)
                {
                    damount.setText("150");
                }
                if(Integer.parseInt(ptamount)>7000)
                {
                    damount.setText("Free Delivery");
                }
                if(Integer.parseInt(ptamount)<7000)
                {
                    Double amounts=Double.parseDouble(ptamount)+150;
                    ttamount.setText(amounts.toString());
                }
                if(Integer.parseInt(ptamount)>7000)
                {
                    ttamount.setText(ptamount);
                }
            }
        }
    }
}
