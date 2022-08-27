package com.example.finalchocohub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductViewAdapter extends BaseAdapter {

    Context context;

    String proid=null;
    String proimg=null;
    ProgressDialog pDialog=null;
    datahelper db=null;
    String id=null;
    String url="https://valentinachoco.000webhostapp.com/valentina/add_to_cart.php";
    JSONParser  jsonp=new JSONParser();
    int cnt=0;
    ArrayList<HashMap<String,String>> plist;
    ProductViewAdapter(Context context, ArrayList<HashMap<String,String>> plist)
    {
        this.context=context;
        this.plist=plist;
    }
    @Override
    public int getCount() {
        return plist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        final TextView tvname;
        final ImageView iv;
        final TextView tvqty;
        final TextView tvprice;
        final TextView tvdisc;
        final TextView tvproid;

        Button btnAddtoCart;

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View custome;
        if(view==null)
        {
            custome=inflater.inflate(R.layout.row_product,viewGroup,false);
            HashMap<String,String>map=plist.get(i);
            tvname=(TextView)custome.findViewById(R.id.textView);
            tvname.setText(map.get(Product.Tag_pro_name));
            tvqty=(TextView)custome.findViewById(R.id.txtqty);
            tvqty.setText(map.get(Product.Tag_pro_pro_qty));
            tvprice=(TextView)custome.findViewById(R.id.txtprice);
            tvprice.setText(map.get(Product.Tag_pro_price));
            tvdisc=(TextView)custome.findViewById(R.id.txtdisc);
            tvdisc.setText(map.get(Product.Tag_pro_disc));
            tvproid=(TextView)custome.findViewById(R.id.proid);
            tvproid.setText(map.get(Product.Tag_pro_id));
            iv=(ImageView)custome.findViewById(R.id.imageView);
            Picasso.with(context).load(map.get(Product.Tag_pro_image)).into(iv);
            db=new datahelper(context);
            btnAddtoCart=(Button)custome.findViewById(R.id.addtocart);
            btnAddtoCart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view)
                {
                    proid=tvproid.getText().toString();
                    new InsertRecord().execute();
                }
                class InsertRecord extends AsyncTask<String,String,String>
                {
                    protected void onPreExecute()
                    {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(context);
                        pDialog.setMessage("Please wait Submit Cart....");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(true);
                        pDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("cid",Product.cid));
                        params.add(new BasicNameValuePair("proid",proid));
                        params.add(new BasicNameValuePair("proqty","1"));
                        params.add(new BasicNameValuePair("proimg",proimg));

                        try {
                            JSONObject obj = jsonp.makeHttpRequest(url, "POST", params);
                            int ans = obj.getInt("success");
                            if (ans == 1)
                            {
                                cnt = 1;
                            } else
                            {
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

                            Toast.makeText(context, "Added to cart", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Sorry", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            });
            tvname.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(context, tvname.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            custome=(View)view;
        }
        return custome;
    }
}
