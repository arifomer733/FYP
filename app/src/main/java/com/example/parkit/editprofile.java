package com.example.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editprofile extends AppCompatActivity {

    Button saveProfile, deleteProfile;

    ImageView callLogout;

    TextInputEditText oldpassword, password, reenterpassword;

    TextView mname,memail;

    data info;

    String moldpassword,mnewpassword,mreenterpassword;


    FirebaseAuth fAuth;
    FirebaseUser user;
    DatabaseReference reference;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Log.e("oncreate","onCreate: editprofile");



        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        user = FirebaseAuth.getInstance().getCurrentUser();


        mname=findViewById(R.id.name);
        memail=findViewById(R.id.email);
        oldpassword=findViewById(R.id.oldPassword);
        password=findViewById(R.id.newPassword);
        reenterpassword=findViewById(R.id.reenterNewPassword);

        mname.setText(info.strname);
        memail.setText(info.stremail);


        callLogout = (ImageView) findViewById(R.id.logout);
        callLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(editprofile.this, callLogout);
                popup.getMenuInflater().inflate(R.menu.logoutmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(
                                editprofile.this,
                                item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(editprofile.this, mainActivity.class));
                        finish();
                        return true;
                    }
                });
                popup.show();
            }
        });


        saveProfile = (Button) findViewById(R.id.saveProfile);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moldpassword = oldpassword.getText().toString().trim();
                mnewpassword = password.getText().toString().trim();
                mreenterpassword = reenterpassword.getText().toString().trim();

                if(TextUtils.isEmpty(moldpassword)){

                    oldpassword.setError("Password cannot be empty.");
                    Toast.makeText(getApplicationContext(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(mnewpassword )){

                    password.setError("Password cannot be empty.");
                    Toast.makeText(getApplicationContext(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(mreenterpassword)){

                    reenterpassword.setError("Password cannot be empty.");
                    Toast.makeText(getApplicationContext(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();

                }
                else if(mnewpassword.length()<7){

                    password.setError("Password must be at least 8 characters long.");
                    Toast.makeText(getApplicationContext(), "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();

                }
                else if(moldpassword.equals(info.strpassword)) {

                    if(moldpassword.equals(mnewpassword)) {

                        password.setError("New Password should be different than the old password");
                        Toast.makeText(getApplicationContext(), "New Password should be different than the old password", Toast.LENGTH_SHORT).show();

                    }
                    else  {

                    if(mnewpassword.equals(mreenterpassword)) {

                        user.updatePassword(mnewpassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            reference = FirebaseDatabase.getInstance().getReference("UserInfo").child(currentUser);
                                            reference.child("Password").setValue(mnewpassword);
                                            data.strpassword = mnewpassword;

                                            Intent intent = new Intent(editprofile.this, profile.class);
                                            Toast.makeText(getApplicationContext(), "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                    else {

                        reenterpassword.setError("New Password does not match");
                        Toast.makeText(getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
                    }
                    }
                }
                else
                {

                    oldpassword.setError("old password is incorrect");
                    Toast.makeText(getApplicationContext(), "old password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });


        deleteProfile = (Button) findViewById(R.id.deleteProfile);
        if(info.strtype.equals("user")){
            deleteProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(editprofile.this);
                    builder.setMessage("Confirm delete?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    reference = FirebaseDatabase.getInstance().getReference("UserInfo").child(currentUser);
                                    FirebaseAuth.getInstance().signOut();
                                    reference.removeValue();
                                    user.delete();


                                    startActivity(new Intent(getApplicationContext(), mainActivity.class));
                                    finish();
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
        else
        {
            deleteProfile.setVisibility(View.INVISIBLE);
        }

    }
}