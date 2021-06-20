package com.example.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class profile extends AppCompatActivity {

    Button edit;

    TextView pName,pEmail,pCarName,pCarModel,pCarRegistration;
    DatabaseReference dbRef;
    data info;

    ImageView callLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        pName = findViewById(R.id.profileName);
        pEmail = findViewById(R.id.profileEmail);
        pCarName = findViewById(R.id.profileCar);
        pCarModel = findViewById(R.id.profileCarModel);
        pCarRegistration = findViewById(R.id.profileCarRegistration);

        Log.e("oncreate","onCreate: profile");

        callLogout = (ImageView) findViewById(R.id.logout);
        callLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(profile.this, callLogout);
                popup.getMenuInflater().inflate(R.menu.logoutmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(
                                profile.this,
                                item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(profile.this, mainActivity.class));
                        finish();
                        return true;
                    }
                });
                popup.show();
            }
        });


        pName.setText(info.strname);
        pEmail.setText(info.stremail);
        pCarName.setText(info.strcarName);
        pCarModel.setText(info.strcarModel);
        pCarRegistration.setText(info.strregistration);

        edit = (Button) findViewById(R.id.editProfile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, editprofile.class);
                Toast.makeText(getApplicationContext(),"Edit your password",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }
}