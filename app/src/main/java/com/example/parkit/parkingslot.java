package com.example.parkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseArray;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class parkingslot extends AppCompatActivity {

    TextInputEditText parkingslot;
    String slot;
    Button save;

    ListView mylistview;
    ArrayList<String> myArrayList = new ArrayList<>();
    DatabaseReference mref;
    ImageView callLogout, profileInfo;

    data info;

    String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkingslot);

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(parkingslot.this,android.R.layout.simple_list_item_1,myArrayList);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        callLogout = (ImageView) findViewById(R.id.logout);
        callLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(parkingslot.this, callLogout);
                popup.getMenuInflater().inflate(R.menu.logoutmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(
                                parkingslot.this,
                                item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(parkingslot.this, mainActivity.class));
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

                Intent intent = new Intent(parkingslot.this, profile.class);
                Toast.makeText(getApplicationContext(),"Profile info",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        mylistview = (ListView) findViewById(R.id.listview1);
        mylistview.setAdapter(myArrayAdapter);
        mref = FirebaseDatabase.getInstance().getReference("Slot");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                String value = snapshot.getValue(String.class);

                if(value.equals("occupied")){

                }
                else {
                    myArrayList.add("Slot # "+ snapshot.getKey().toString() +" is " + value);
                }
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                myArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        save = (Button) findViewById(R.id.saveslot);
        parkingslot = (TextInputEditText) findViewById(R.id.parkingslot);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slot = parkingslot.getText().toString();

                Toast.makeText(getApplicationContext(),slot,Toast.LENGTH_SHORT).show();

                if(TextUtils.isEmpty(slot))
                {
                    parkingslot.setError("Parking Slot cannot be empty.");
                }
                else if(slot.length()<2)
                {
                    parkingslot.setError("Slot Should be from range given below.");
                }
                else
                {
                    mref = FirebaseDatabase.getInstance().getReference("UserInfo").child(currentUser);
                    mref.child("slot").setValue(slot);
                    mref = FirebaseDatabase.getInstance().getReference("Slot").child(slot);
                    mref.setValue("occupied");
                    info.strslot = slot;

                    Intent intent = new Intent(parkingslot.this, userScreen.class);
                    Toast.makeText(getApplicationContext(),"Booked Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }

            }
        });

    }
}