package com.example.finalchocohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Category extends AppCompatActivity {
    public static String cid=null;
    //public static String Tag_cate_img;
    JSONArray array;
    ListView lvcat;
    String url="https://valentinachoco.000webhostapp.com/valentina/view_category.php";

    int cnt=0;
    JSONParser jsonp=new JSONParser();
    AlertDialog.Builder alert=null;
    String Tag_Array="category";
    public static String Tag_cate_id="catid";
    public static String Tag_cate_name="catname";
    public static String Tag_cate_img="catimage";
    ArrayList<HashMap<String,String>> clist=null;
    SharedPreferences ses=null;
    datahelper db=null;
    String id=null;
    ImageView iv;
    TextView tvcatname;
    CategoryViewAdapter categoryViewAdapter;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    public static NavigationView navigationView;

    EditText EDT_SEARCH;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        Toolbar toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(Category.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        setTitle("Category");
        final LoadingDilog  pDialog1 =new LoadingDilog(Category.this);
        ses=getSharedPreferences(login.sessionLogin,MODE_PRIVATE);
        db=new datahelper(Category.this);
        LoadingDilog loadingDilog=new LoadingDilog(Category.this);
        //iv=findViewById(R.id.ivcat);
        lvcat=findViewById(R.id.catview);
        categoryViewAdapter=new CategoryViewAdapter(getApplicationContext(),clist);
        //EDT_SEARCH=findViewById(R.id.edsearch);
//        EDT_SEARCH.addTextChangedListener(new TextWatcher()
//        {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // TODO Auto-generated method stub
//                // ADPT_FAV_CONTACT.getFilter().filter(EDT_SEARCH.getText().toString());
//                // mAdapter.getFilter().filter(s);
//                System.out.println("saerch " + s);
//                // ADPT_FAV_CONTACT.getFilter().filter(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                System.out.println(EDT_SEARCH.getText().toString());
//                System.out.println("eee "+s);
//                categoryViewAdapter.getFilter().filter(EDT_SEARCH.getText().toString());
//            }
//        });




        clist=new ArrayList<HashMap<String, String>>();
        new GetCategory().execute();
        lvcat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String catid=((TextView)view.findViewById(R.id.tv_cat_id)).getText().toString();
                SharedPreferences.Editor obj=ses.edit();
                obj.putString("catid",catid);
                obj.commit();
                Intent in=new Intent(Category.this,Product.class);
                startActivity(in);
            }
        });

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
                        Intent i_category=new Intent(Category.this,change_password.class);
                        startActivity(i_category);
                        break;
                    case R.id.nav_My_cart:
                        Intent i_beg=new Intent(Category.this,YourCart.class);
                        startActivity(i_beg);
                        break;
                    case R.id.nav_Complain:
                        Intent i_Whishlist=new Intent(Category.this,Complain.class);
                        startActivity(i_Whishlist);
                        break;
                    case R.id.nav_brochure:
                        Intent i_viewcart=new Intent(Category.this,PDF_download.class);
                        startActivity(i_viewcart);
                        break;
                    case R.id.nav_feedback:
                        Intent i_feedback=new Intent(Category.this,Feedback.class);
                        startActivity(i_feedback);
                        break;
                    case R.id.nav_logout:
                        AlertDialog.Builder alert=new AlertDialog.Builder(Category.this);
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




                                    Toast.makeText(Category.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                    Intent p = new Intent(Category.this, login.class);
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



    ProgressDialog pDialog;
    class GetCategory extends AsyncTask<String,String,String> {


        protected void onPreExecute() {

            super.onPreExecute();
            pDialog=new ProgressDialog(Category.this);
            pDialog = new ProgressDialog(Category.this);
           pDialog.setTitle(Html.fromHtml(" Please Wait Loding Category... "));
            pDialog.setIndeterminate(false);
            pDialog.setContentView(R.layout.custom_tost);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            clist=new ArrayList<HashMap<String, String>>();
            try {
                JSONObject obj = jsonp.getJSONFromUrl(url);
                int ans = obj.getInt("success");
                if (ans == 1) {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_Array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        String cat_id = o.getString(Tag_cate_id);
                        String cat_name = o.getString(Tag_cate_name);
                        String cate_img=o.getString(Tag_cate_img);
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(Tag_cate_name, cat_name);
                        hashMap.put(Tag_cate_id, cat_id + "");
                        hashMap.put(Tag_cate_img,"https://valentinachoco.000webhostapp.com/categoryimage/"+cate_img);
                        clist.add(hashMap);
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
                Toast.makeText(Category.this, "No  Category Found", Toast.LENGTH_SHORT).show();
                // Toast.makeText(Category.this, "No Category Found", Toast.LENGTH_SHORT).show();
            }
            else
            {
                categoryViewAdapter=new CategoryViewAdapter(Category.this,clist);
                lvcat.setAdapter(categoryViewAdapter);



            }
        }


    }
}