package com.example.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class userScreen extends AppCompatActivity {

    ImageView callLogout, profileInfo;

    data info;

    Button book,unbook;

    DatabaseReference reference;
    DatabaseReference reference1;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String currentUser;

    String slot;

    TextView mcarName,mcarModel,mregistration,mparking;
    String parking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        Log.e("oncreate","onCreate: userscreen");


        reference = FirebaseDatabase.getInstance().getReference("Slots");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();


        mcarName = findViewById(R.id.profileCar);
        mcarModel = findViewById(R.id.profileCarModel);
        mregistration = findViewById(R.id.profileCarRegistration);

        mcarName.setText(info.strcarName);
        mcarModel.setText(info.strcarModel);
        mregistration.setText(info.strregistration);

        callLogout = (ImageView) findViewById(R.id.logout);
        callLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(userScreen.this, callLogout);
                popup.getMenuInflater().inflate(R.menu.logoutmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(
                                userScreen.this,
                                item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(userScreen.this, mainActivity.class));
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

                Intent intent = new Intent(userScreen.this, profile.class);
                Toast.makeText(getApplicationContext(),"Profile info",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                }
            });


        mparking = (TextView) findViewById(R.id.parking);
        mparking.setText(info.strslot);

        book = (Button) findViewById(R.id.bookaslot);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(info.strslot.equals("free")) {

                    Intent intent = new Intent(userScreen.this, parkingslot.class);
                    Toast.makeText(getApplicationContext(),"Choose a slot",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Unbook first",Toast.LENGTH_SHORT).show();
                }
            }
        });

        unbook = (Button) findViewById(R.id.unbook);

        unbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(userScreen.this);
                            builder.setMessage("Confirm unbooking?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if(info.strslot.equals("free")) {
                                                Toast.makeText(getApplicationContext(),"You have not booked any slot",Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            }
                                            else {
                                                reference = FirebaseDatabase.getInstance().getReference("UserInfo").child(currentUser);
                                                reference.child("slot").setValue("free");
                                                reference = FirebaseDatabase.getInstance().getReference("Slot").child(info.strslot);
                                                reference.setValue("free");
                                                info.strslot = "free";
                                                mparking.setText(info.strslot);

                                                Toast.makeText(getApplicationContext(),"SuccessFully Unbooked",Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            }

                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }

                    });

}
}