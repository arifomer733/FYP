package com.example.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class mainActivity extends AppCompatActivity {

    Button callSignUp, callLogin;

    TextInputEditText email,password;
    TextInputLayout passwordIcon;

    ProgressBar progressBar;

    FirebaseAuth fAuth;


    DatabaseReference dbRef;
    String data, usertype;

    data info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("oncreate","onCreate: mainscreen");

        info.strslot= "free";

        callSignUp = findViewById( R.id.signup);
        callLogin = findViewById( R.id.Login_btn);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();

        email    = findViewById(R.id.email);
        password = findViewById(R.id.password);


        progressBar.setVisibility(View.INVISIBLE);

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String memail     = email.getText().toString().trim();
                String mpassword  = password.getText().toString().trim();

                if(TextUtils.isEmpty(memail))
                {
                    email.setError("Email is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(mpassword))
                {
                    password.setError("Password is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    passwordIcon.setPasswordVisibilityToggleEnabled(false);
                    return;
                }

                fAuth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {

                            data = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            dbRef = FirebaseDatabase.getInstance().getReference("UserInfo");

                            dbRef.child(data).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    HashMap<String,Object> dbRead = new HashMap<>();
                                    dbRead= (HashMap<String, Object>) snapshot.getValue();

                                    info.strname=dbRead.get("Name").toString();
                                    info.stremail=dbRead.get("Email").toString();
                                    info.strpassword=dbRead.get("Password").toString();
                                    info.strcarName=dbRead.get("CarName").toString();
                                    info.strcarModel=dbRead.get("CarModel").toString();
                                    info.strtype=dbRead.get("Type").toString();
                                    info.strregistration=dbRead.get("RegistrationNumber").toString();
                                    info.strslot=dbRead.get("slot").toString();

                                    if(dbRead.get("Type").toString().equals("admin"))
                                    {
                                        Toast.makeText(mainActivity.this,"Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(mainActivity.this, adminScreen.class));
                                        finish();
                                    }
                                    if(dbRead.get("Type").toString().equals("user"))
                                    {
                                        Toast.makeText(mainActivity.this,"Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(mainActivity.this, userScreen.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    Toast.makeText(getApplicationContext(),"Error "+ error.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else{
                            Toast.makeText(mainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                });
            }
        });

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity.this, signup.class);
                Toast.makeText(getApplicationContext(),"Create a new account",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}