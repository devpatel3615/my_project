package com.example.finalchocohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.finalchocohub.Fragment.Home_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Fragment_menu extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_menu);
        bottomNavigationView=findViewById(R.id.bottmNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.containe,new Home_Fragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod=new
            BottomNavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.home:
                            selectedFragment = new Home_Fragment();
                            startActivity(new Intent(Fragment_menu.this, menu_list.class));
                            break;


                        case R.id.profile:

                            startActivity(new Intent(Fragment_menu.this, Profile.class));
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.containe, selectedFragment).commit();
                    }


                    //                    Intent i = new Intent(Fragment_menu.this, menu_list.class);
                    //                    startActivity(i);


                    return true;

                };
            };
}