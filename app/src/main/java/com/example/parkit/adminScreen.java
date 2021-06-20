package com.example.parkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class adminScreen extends AppCompatActivity {

    ImageView callLogout;
    ImageView profileInfo;

    Button viewUsers;
    data info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        Log.e("oncreate","onCreate: adminScreen");

        callLogout = (ImageView) findViewById(R.id.logout);
        callLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(adminScreen.this, callLogout);
                popup.getMenuInflater().inflate(R.menu.logoutmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(
                                adminScreen.this,
                                item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(adminScreen.this, mainActivity.class));

                        finish();
                        return true;
                    }
                });
                popup.show();
            }
        });

        profileInfo = (ImageView) findViewById(R.id.profile);
        profileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminScreen.this, profile.class);
                Toast.makeText(getApplicationContext(),"Profile info",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        viewUsers = (Button) findViewById(R.id.allusers);
        viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminScreen.this, viewusers.class);
                Toast.makeText(getApplicationContext(),"All Users",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}