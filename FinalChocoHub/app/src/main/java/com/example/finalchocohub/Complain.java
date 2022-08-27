package com.example.finalchocohub;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Complain extends AppCompatActivity {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    public static NavigationView navigationView;
    String complain, date, status, replay;
    Button compCancle;
    String Tag_array = "complain";
    String Tag_date = "comp_date";
    String Tag_replay = "Reply";
    String Tag_status = "isReply";
    String Tag_complain = "complain";
    String Tag_compid="comp_id";
    JSONArray array = null;



    AlertDialog.Builder alert = null;
    EditText editcomplain;
    Button submit;
    ListView lv;
    datahelper db = null;
    String scomplain, cid = null;
    SharedPreferences ses = null;
    String url = "https://valentinachoco.000webhostapp.com/valentina/complain.php";
    String view_url = "https://valentinachoco.000webhostapp.com/valentina/view_complain.php";
    JSONParser jsonp = new JSONParser();
    ArrayList<HashMap<String, String>> clist = null;
    complainviewAdepter adapter=null;
    TextView txtstatus;

    int cnt = 0;
    LoadingDilog pDialog=null;


    String id = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);


        compCancle=(Button) findViewById(R.id.compCancle);
        editcomplain = (EditText) findViewById(R.id.editcomplain);
        submit = (Button) findViewById(R.id.btncomplain);
        lv = (ListView) findViewById(R.id.complainView);
        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        cid = ses.getString("cid", null);
        txtstatus=(TextView)findViewById(R.id.txtcompstatus);

        db = new datahelper(Complain.this);
        setTitle("Complain");

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                scomplain = editcomplain.getText().toString();
                if (scomplain.equalsIgnoreCase("")) {
                    editcomplain.setError("Please Enter complain");
                } else {
                    new InsertRecord().execute();
                }


            }


            class InsertRecord extends AsyncTask<String, String, String> {
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new LoadingDilog(Complain.this);
                    //pDialog.setMessage("Please wait Submit complain....");
                    //pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }


                @Override
                protected String doInBackground(String... strings) {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("cid", cid));
                    params.add(new BasicNameValuePair("complain", scomplain));
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

                protected void onPostExecute(String file_url) {
                    pDialog.dismiss();
                    if (cnt == 1) {
                        Toast.makeText(Complain.this, "Complain Successfully", Toast.LENGTH_LONG).show();
                        editcomplain.setText("");


                    } else {
                        Toast.makeText(Complain.this, "Sorry", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        new getcomplain().execute();

    }

    class getcomplain extends AsyncTask<String, String, String>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new LoadingDilog(Complain.this);
           // pDialog.setMessage("Please wait Fetch Complain....");
           // pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            clist = new ArrayList<HashMap<String, String>>();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("cid", cid));
            JSONObject obj = jsonp.makeHttpRequest(view_url, "GET", params);
            try {
                if (obj.getInt("success") > 0) {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        complain = o.getString(Tag_complain);
                        date = o.getString(Tag_date);
                        status = o.getString(Tag_status);
                        String compid=o.getString(Tag_compid);

                        if(status.equals("0"))
                        {
                            status="Pendding";
                            replay=" ";
                        }
                        else if(status.equals("-2"))
                        {
                            status="Cancal";
                            replay="";
                        }

                        else
                        {
                            status="Send Reponse";
                            replay = o.getString(Tag_replay);
                        }

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(Tag_complain, complain);
                        map.put(Tag_date, date);
                        map.put(Tag_status, status);
                        map.put(Tag_replay, replay);
                        map.put(Tag_compid,compid);
                        clist.add(map);
                    }
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
            // dismiss the dialog once done
            pDialog.dismiss();
            if (cnt == 0) {
                Toast.makeText(Complain.this, "No Any complain Found", Toast.LENGTH_SHORT).show();

            } else {
               /* ListAdapter adapter = new SimpleAdapter(Complain.this, clist,
                        R.layout.row_complain,
                        new String[]{Tag_complain, Tag_date, Tag_status, Tag_replay}, new int[]{
                        R.id.txtcomplain, R.id.txtcompdate, R.id.txtcompstatus, R.id.txtcompreply});

*/
                adapter=new complainviewAdepter(Complain.this,clist);
                lv.setAdapter(adapter);

            }

        }

    }
    public  class complainviewAdepter extends BaseAdapter
    {
        Context context;
        String compid=null;
        LoadingDilog pDialog=null;
        datahelper db=null;
        JSONParser jsonp =new JSONParser();
        AlertDialog alertDialog=null;
        int cnt=0;
        String complaincancelurl="https://valentinachoco.000webhostapp.com/valentina/cancle_complain.php";
        ArrayList<HashMap<String,String>> clist;
        complainviewAdepter(Context context,ArrayList<HashMap<String,String>> clist)
        {
            this.context =context;
            this.clist=clist;
        }

        @Override
        public int getCount() {
            return clist.size();
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
            final TextView tvcomplain;
            final TextView tvcompdate;
            final TextView tvcompstatus;
            Button cancelcomplain;


            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View custome;
            custome=inflater.inflate(R.layout.row_complain,viewGroup,false);
            HashMap<String,String>map=clist.get(i);
            tvcomplain=(TextView)custome.findViewById(R.id.txtcomplain);
            tvcomplain.setText(map.get(Tag_complain));
            tvcompdate=(TextView)custome.findViewById(R.id.txtcompdate);
            tvcompdate.setText(map.get(Tag_date));
            tvcompstatus=(TextView)custome.findViewById(R.id.txtcompstatus);
            tvcompstatus.setText(map.get(Tag_status));
            final TextView comid=(TextView)custome.findViewById(R.id.compid);
            comid.setText(map.get(Tag_compid));
            cancelcomplain=(Button)custome.findViewById(R.id.compCancle);
            cancelcomplain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert=new AlertDialog.Builder(context);
                    alert.setTitle("Cancel");
                    alert.setMessage("Are you sure want to cancel complain");
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            compid=comid.getText().toString();
                            new complaincancel().execute();
                        }
                    });
                    alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "You click no Button", Toast.LENGTH_SHORT).show();
                        }

                    });
                    alert.show();


                }
                class complaincancel extends AsyncTask<String,String,String>
                {
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new LoadingDilog(Complain.this);
                        //pDialog.setMessage("Please wait Cancal Complain....");
                        //pDialog.setIndeterminate(false);
                        pDialog.setCancelable(true);
                        pDialog.setCanceledOnTouchOutside(false);
                        pDialog.show();
                    }


                    @Override
                    protected String doInBackground(String... strings) {
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("comp_id",compid));

                        try {
                            JSONObject obj = jsonp.makeHttpRequest(complaincancelurl, "POST", params);
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
                            Toast.makeText(Complain.this, "Complain Cancel Successfully", Toast.LENGTH_LONG).show();
                            new getcomplain().execute();

                        }
                        else
                        {
                            Toast.makeText(Complain.this, "Sorry", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
            db=new datahelper(context);
            return custome;
        }

    }

    }
