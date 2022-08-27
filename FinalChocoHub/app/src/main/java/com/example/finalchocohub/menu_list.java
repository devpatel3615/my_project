package com.example.finalchocohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


//import com.google.firebase.auth.FirebaseAuth;


public class menu_list extends AppCompatActivity
{
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    public static NavigationView navigationView;
    GridView gv;
    //FirebaseAuth mAuth;
    AlertDialog.Builder alert=null;
    String menudata[]=new String[]
            {"Profile","Category","My Cart","My Order","Payment","Feedback","Complain","Logout"};
    SharedPreferences ses=null;
    datahelper db=null;
    String id=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manu_list);


        setTitle("Manu");
        //  mAuth=FirebaseAuth.getInstance();
        ses=getSharedPreferences(login.sessionLogin,MODE_PRIVATE);
        db=new datahelper(menu_list.this);
        gv=findViewById(R.id.gvmain);
        gv.setAdapter(new ImageAdapter(this,menudata));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selectMenu=((TextView)view.findViewById(R.id.tv1)).getText().toString();
                Toast.makeText(menu_list.this, selectMenu, Toast.LENGTH_SHORT).show();


                if(selectMenu.equalsIgnoreCase("Profile"))

                {
                    Intent d=new Intent(menu_list.this,Profile.class);
                    startActivity(d);
                }
                else if(selectMenu.equalsIgnoreCase("Payment"))
                {
                    Intent d=new Intent(menu_list.this,Payment.class);
                    startActivity(d);
                }
                else if(selectMenu.equalsIgnoreCase("My Cart"))
                {
                    Intent d=new Intent(menu_list.this, YourCart.class);
                    startActivity(d);
                }
                else if(selectMenu.equalsIgnoreCase("Category"))
                {
                    Intent d=new Intent(menu_list.this, Category.class);
                    startActivity(d);
                }
                else if(selectMenu.equalsIgnoreCase("Complain"))
                {
                    Intent d=new Intent(menu_list.this,Complain.class);
                    startActivity(d);
                }
                else if(selectMenu.equalsIgnoreCase("My Order"))
                {
                    Intent d=new Intent(menu_list.this,YourOrder.class);
                    startActivity(d);
                }
                else if(selectMenu.equalsIgnoreCase("Feedback"))
                {
                    Intent d=new Intent(menu_list.this,Feedback.class);
                    startActivity(d);
                }
                else if(selectMenu.equalsIgnoreCase("Logout"))
                {
                    alert=new AlertDialog.Builder(menu_list.this);
                    alert.setTitle("Valentina Choco");
                    alert.setMessage("Are You Sure Want To Logout?");
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor obj=ses.edit();
                            obj.clear();
                            obj.commit();
                            int deleteResponce=db.delete(id);
                            if(deleteResponce>0)
                            {
                                Toast.makeText(menu_list.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                Intent p = new Intent(menu_list.this, login.class);
                                startActivity(p);
                            }
                        }
                    });
                    alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(menu_list.this, "U Click No Button", Toast.LENGTH_SHORT).show();

                        }
                    });
                    alert.show();
                }
            }
        });

        // navigation
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=(DrawerLayout)findViewById(R.id.drawer);
        navigationView=(NavigationView)findViewById(R.id.nv);
        toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.green1));
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
                        Intent i_pass=new Intent(menu_list.this,change_password.class);
                        startActivity(i_pass);
                        break;

                    case R.id.nav_My_cart:
                        Intent i_category=new Intent(menu_list.this, YourCart.class);
                        startActivity(i_category);
                        break;
                    case R.id.nav_Complain:
                        Intent i_beg=new Intent(menu_list.this,Complain.class);
                        startActivity(i_beg);
                        break;

                    case R.id.nav_brochure:
                        Intent i_brochure=new Intent(menu_list.this, PDF_download.class);
                        startActivity(i_brochure);
                        break;

                    case R.id.nav_feedback:
                        Intent i_Whishlist=new Intent(menu_list.this,Feedback.class);
                        startActivity(i_Whishlist);
                        break;

                    case R.id.nav_share:
                        {
                        Intent i_share = new Intent(menu_list.this, menu_list.class);
                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here");
                        String app_url = " https://drive.google.com/file/d/1TVcQHSPZWKk8Ec8YGKJdS0d6rOrNRBFj/view";
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                        };

                        break;

                    case R.id.nav_logout:
                        AlertDialog.Builder alert=new AlertDialog.Builder(menu_list.this);
                        alert.setTitle("Logout");
                        alert.setMessage("Are You sure You want to Loged Out !");
                        alert.setIcon(R.drawable.logout_org);
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent i=new Intent(menu_list.this,login.class);
                                startActivity(i);

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

    class ImageAdapter extends BaseAdapter
    {
        Context context;
        String detail[];
        ImageAdapter(Context context,String detail[])
        {
            this.context=context;
            this.detail=detail;
        }
        @Override
        public int getCount() {
            return detail.length;
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
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View grid;
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            if(view==null)
            {
                grid=new View(context);
                grid=inflater.inflate(R.layout.menurow,null);
                TextView tv=(TextView)grid.findViewById(R.id.tv1);
                ImageView iv=(ImageView)grid.findViewById(R.id.iv1);
                tv.setText(detail[i]);
                String menudetail=detail[i];
                if(menudetail.equalsIgnoreCase("logout"))
                {
                    iv.setImageResource(R.drawable.logout_org);
                }
                if(menudetail.equalsIgnoreCase("My Order"))
                {
                    iv.setImageResource(R.drawable.order);
                }

                if(menudetail.equalsIgnoreCase("Category"))
                {
                    iv.setImageResource(R.drawable.category111);
                }
                if(menudetail.equalsIgnoreCase("Complain"))
                {
                    iv.setImageResource(R.drawable.complain);
                }
                if(menudetail.equalsIgnoreCase("My Cart"))
                {
                    iv.setImageResource(R.drawable.addtocart);
                }
                if(menudetail.equalsIgnoreCase("Payment"))
                {
                    iv.setImageResource(R.drawable.payment1);
                }
                if(menudetail.equalsIgnoreCase("Product"))
                {
                    iv.setImageResource(R.drawable.product);
                }
                if(menudetail.equalsIgnoreCase("Feedback"))
                {
                    iv.setImageResource(R.drawable.feedback);
                }
                if(menudetail.equalsIgnoreCase("Profile"))
                {
                    iv.setImageResource(R.drawable.customer);
                }

            }
            else
            {
                grid=(View)view;
            }
            return grid;
        }
    }
}
