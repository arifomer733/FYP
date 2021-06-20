package com.example.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class viewusers extends AppCompatActivity {

    ImageView callLogout, profileInfo;

    ListView listView;

    FirebaseDatabase database;

    DatabaseReference reference;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    User user;
    String muser;

    Button delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewusers);

        callLogout = (ImageView) findViewById(R.id.logout);
        callLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(viewusers.this, callLogout);
                popup.getMenuInflater().inflate(R.menu.logoutmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(viewusers.this,
                                item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(viewusers.this, mainActivity.class));
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

                Intent intent = new Intent(viewusers.this, profile.class);
                Toast.makeText(getApplicationContext(),"Profile info",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

     listView = (ListView) findViewById(R.id.listview);
     database = FirebaseDatabase.getInstance();
     reference = database.getReference("UserInfo");
     list = new ArrayList<>();
     user = new User();

     delete = (Button) findViewById(R.id.deleteProfile);

     adapter = new ArrayAdapter<String>(this,R.layout.user_info,R.id.userInfo, list);
     reference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
             for(DataSnapshot ds: snapshot.getChildren())
             {


                     user = ds.getValue(User.class);

                     if(user.getType().toString().equals("user")) {

                     list.add("Name: " + user.getName().toString()  + ", Email: " + user.getEmail().toString());

                 }
             }
             listView.setAdapter(adapter);
         }

         @Override
         public void onCancelled(@NonNull @NotNull DatabaseError error) {

         }
     });

    }
}