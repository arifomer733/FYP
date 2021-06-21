package com.example.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class signup extends AppCompatActivity {

    Button oldUser, signup;

    TextInputLayout passwordIcon;
    TextInputEditText fullName, email, password, carName, carModel, regNumber;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    data info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Log.e("oncreate","onCreate: signup");


        oldUser = (Button) findViewById(R.id.oldUser);
        signup  = (Button) findViewById(R.id.signup_btn);

        fullName = findViewById(R.id.fullName);
        email    = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordIcon = findViewById(R.id.passwordIcon);
        carName  = findViewById(R.id.carname);
        carModel = findViewById(R.id.carmodel);
        regNumber= findViewById(R.id.regnumber);

        fAuth   =   FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting & Converting objects to Strings.

                String mname      = fullName.getText().toString().trim();
                String memail     = email.getText().toString().trim();
                String mpassword  = password.getText().toString().trim();
                String mcarModel  = carModel.getText().toString().trim();
                String mcarName   = carName.getText().toString().trim();
                String mregNumber = regNumber.getText().toString().trim();

                //Validating input fields.

                if(TextUtils.isEmpty(mname))
                {
                    fullName.setError("Name is Required.");
                    return;
                }
                if(TextUtils.isEmpty(memail))
                {
                    email.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(mpassword))
                {
                    password.setError("Password is Required.");
                    passwordIcon.setPasswordVisibilityToggleEnabled(false);
                    return;
                }
                if(mpassword.length()<8)
                {
                    password.setError("Password must be at least 8 character long.");
                    return;
                }
                if(TextUtils.isEmpty(mcarName))
                {
                    carName.setError("Car name is Required.");
                    return;
                }
                if(TextUtils.isEmpty(mcarModel))
                {
                    carModel.setError("Car model is Required.");
                    return;
                }
                if(TextUtils.isEmpty(mregNumber))
                {
                    regNumber.setError("Registration Number is Required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Registering the user.

                fAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            HashMap<String,Object> dbWrite = new HashMap<>();
                            dbWrite.put("Name",mname);
                            dbWrite.put("Email",memail);
                            dbWrite.put("Password",mpassword);
                            dbWrite.put("CarName",mcarName);
                            dbWrite.put("CarModel",mcarModel);
                            dbWrite.put("RegistrationNumber",mregNumber);
                            dbWrite.put("Type","user");
                            dbWrite.put("slot","free");

                            info.strname= mname;
                            info.stremail= memail;
                            info.strpassword= mpassword;
                            info.strcarName= mcarName;
                            info.strcarModel= mcarModel;
                            info.strtype= "user";
                            info.strregistration= mregNumber;
                            info.strslot= "free";

                            FirebaseDatabase.getInstance().getReference("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(dbWrite).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(signup.this,"User registered Successfully",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),userScreen.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(signup.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }

                        else {
                        //    if(task.getException().getMessage()=="The email address is already in use by another account.") {
                        //        progressBar.setVisibility(View.INVISIBLE);
                        //        Toast.makeText(signup.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        //    }
                        //    else {
                                Toast.makeText(signup.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                        //    }
                        }

                    }
                });
            }
        });


        oldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, mainActivity.class);
                Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }
}