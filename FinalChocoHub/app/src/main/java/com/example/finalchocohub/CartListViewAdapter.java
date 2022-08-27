package com.example.finalchocohub;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartListViewAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    JSONParser jsonp=new JSONParser();
    int cnt=0;

    String plusURL="https://valentinachoco.000webhostapp.com/valentina/add_to_cart_qty_delet_update.php";
    String DeleteURL="https://valentinachoco.000webhostapp.com/valentina/add_to_cart_delet_.php";

    ArrayList<HashMap<String, String>> dataTemp;
    ArrayList<HashMap<String, String>> data;
    Context context;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public CartListViewAdapter(Context context, ArrayList<HashMap<String, String>> arraylist)
    {
        this.context = context;
        dataTemp = arraylist;
        data = arraylist;
    }
    @Override
    public int getCount()
    {
        return dataTemp.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        final TextView tvpid, tvpname, tvprice, tvacid, tvqty;
        final Button btnplus,btnmin;
        final ImageView iv;
        ImageView deleteIv;
        Button button;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.row_yourcart, parent, false);
        resultp = dataTemp.get(position);

        deleteIv=(ImageView)itemView.findViewById(R.id.iv_AddtoBeg_Delete);
        button=(Button) itemView.findViewById(R.id.btnAPPOINTEMENT);
        btnplus = (Button) itemView.findViewById(R.id.btn_Plus);
        btnmin=(Button)itemView.findViewById(R.id.btn_Minus);
        btnplus.setText("+");

        btnmin.setText("-");
        tvpid = (TextView) itemView.findViewById(R.id.tvproid);
        tvpname = (TextView) itemView.findViewById(R.id.tvAddToBegProductName);
        tvprice = (TextView) itemView.findViewById(R.id.tvAddToBegProductPrice);
        tvqty = (TextView) itemView.findViewById(R.id.tvAddToBegProductQTY);
        tvacid=(TextView)itemView.findViewById(R.id.tvAddToBegATBID);
        iv=(ImageView)itemView.findViewById(R.id.iv_AddtoBeg);

        tvpid.setText(resultp.get(YourCart.tagPid));
        tvpname.setText(resultp.get(YourCart.tagPname));
        tvprice.setText(resultp.get(YourCart.tagprice));

        tvqty.setText(resultp.get(YourCart.tagqty));
        tvacid.setText(resultp.get(YourCart.tagAcid));
        Picasso.with(context).load(resultp.get(YourCart.tagimage)).into(iv);

        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new DeleteItem().execute();
            }
            class DeleteItem extends AsyncTask<String,String,String>
            {
                @Override
                protected String doInBackground(String... arg0)
                {



                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("acid", tvacid.getText().toString()));
//                    params.add(new BasicNameValuePair("qty", tvqty.getText().toString()));
//                    params.add(new BasicNameValuePair("pid", tvpid.getText().toString()));

                    try {
                        JSONObject obj = jsonp.makeHttpRequest(DeleteURL, "POST", params);
                        int ans = obj.getInt("success");
                        if (ans == 1) {
                            cnt = 1;
                        } else {
                            cnt = 0;
                        }
                    } catch (Exception e) {
                        Log.e("Exception is :", e + "");
                    }

                    return null;

                }

                protected void onPostExecute(String file_url) {
                    if (cnt == 1)
                    {
                        Toast.makeText(context, "Item Remove From Cart", Toast.LENGTH_LONG).show();
                        Intent in=new Intent(context,YourCart.class);
                        context.startActivity(in);

                    } else {

                        Toast.makeText(context, "Sorry Item is not Remove From Cart", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                int n= Integer.parseInt(tvqty.getText().toString());
                if(n>=0)
                {
                    int ans=n+1;
                    tvqty.setText(String.valueOf(ans));
                    new atc_UpdateQTY().execute();
                }
            }
            class atc_UpdateQTY extends AsyncTask<String,String,String>
            {
                @Override
                protected String doInBackground(String... arg0) {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("acid", tvacid.getText().toString()));
                    params.add(new BasicNameValuePair("qty", tvqty.getText().toString()));
                    params.add(new BasicNameValuePair("pid", tvpid.getText().toString()));
                    params.add(new BasicNameValuePair("action", "plus"));
                    try {
                        JSONObject obj = jsonp.makeHttpRequest(plusURL, "POST", params);
                        int ans = obj.getInt("success");
                        if (ans == 1) {
                            cnt = 1;
                        } else {
                            cnt = 0;
                        }
                    } catch (Exception e) {
                        Log.e("Exception is :", e + "");
                    }

                    return null;
                }

                protected void onPostExecute(String file_url) {
                    if (cnt == 1)
                    {
                        Toast.makeText(context, "Quantity Is Updated", Toast.LENGTH_LONG).show();
                        Intent in=new Intent(context,YourCart.class);
                        context.startActivity(in);
                    } else {

                        Toast.makeText(context, "Sorry Quantity Is Updated", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int n= Integer.parseInt(tvqty.getText().toString());
                if(n>0)
                {
                    int ans=n-1;
                    if(ans==0)
                    {

                    }
                    else
                    {
                        tvqty.setText(String.valueOf(ans));
                        new atc_UpdateQTY().execute();
                    }
                }
            }
            class atc_UpdateQTY extends AsyncTask<String,String,String> {
                @Override
                protected String doInBackground(String... arg0) {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    //params.add(new BasicNameValuePair("pid",lvproductID));
                    //params.add(new BasicNameValuePair("cid",cid));
                    params.add(new BasicNameValuePair("acid", tvacid.getText().toString()));
                    params.add(new BasicNameValuePair("qty", tvqty.getText().toString()));
                    params.add(new BasicNameValuePair("pid", tvpid.getText().toString()));
                    params.add(new BasicNameValuePair("action", "minus"));
                    try {
                        JSONObject obj = jsonp.makeHttpRequest(plusURL, "POST", params);
                        int ans = obj.getInt("success");
                        if (ans == 1) {
                            cnt = 1;
                        } else {
                            cnt = 0;
                        }
                    } catch (Exception e) {
                        Log.e("Exception is :", e + "");
                    }

                    return null;
                }

                protected void onPostExecute(String file_url) {
                    if (cnt == 1)
                    {
                        Toast.makeText(context, "Quantity Is Updated", Toast.LENGTH_LONG).show();
                        Intent in=new Intent(context,YourCart.class);
                        context.startActivity(in);
                    } else {

                        Toast.makeText(context, "Sorry Quantity Is Updated", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return  itemView;
    }

}

