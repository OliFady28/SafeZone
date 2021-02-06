package com.example.safezone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerActivity extends AppCompatActivity {


    User user;

    EditText fName, lName, rPhone, rPassword, rvPassword , rage ;

    Button RegisterButton;

    DatabaseReference insertReference;

    Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterButton = findViewById(R.id.registerButton);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register(){

        fName = findViewById(R.id.rFname);
        lName = findViewById(R.id.rLname);;
        rPhone = findViewById(R.id.remailAddress);;
        rPassword = findViewById(R.id.rPassword);;
        rvPassword = findViewById(R.id.rvPassword);;
        rage = findViewById(R.id.rAge);

        if(fName.getText().toString().isEmpty()){
            fName.setError("First name filed is empty");
        }
        else if(lName.getText().toString().isEmpty()){
            lName.setError("Last name filed is empty");
        }
        else if(rPhone.getText().toString().isEmpty()){
            rPhone.setError("phone number filed is empty");
        }
        else if(rPhone.getText().toString().length() != 11){
            rPhone.setError("Phone number is not valid");
        }
        else if(rPassword.getText().toString().isEmpty()) {
            rPassword.setError("Password filed is empty");
        }
        else if(rvPassword.getText().toString().isEmpty()) {
            rvPassword.setError("Verify password filed is empty");
        }
        else if(rPassword.getText().toString().length() <= 8) {
            rPassword.setError("Password is too short ");
        }
        else if(rage.getText().toString().isEmpty()){
            rage.setError("Age filed is empty");
        }
        else if(rPassword.getText().toString().equals(rvPassword.getText().toString())){

            String s1 , s2 , s3 , s4 , userID;
            int s5;

            s1 = fName.getText().toString();
            s2 = lName.getText().toString();
            s3 = rPhone.getText().toString();
            s4 = rPassword.getText().toString();
            s5 = Integer.parseInt(rage.getText().toString());
            userID = s3;

            user  = new User( s5 , s1 , s2 , s3 , s4  , "Negative" );
            insertReference = FirebaseDatabase.getInstance().getReference("User");
            insertReference.child(userID).setValue(user);
            Toast.makeText(registerActivity.this,"Account Created Successfully ",Toast.LENGTH_LONG).show();
            intent = new Intent(registerActivity.this , loginActivity.class);
            startActivity(intent);
        }
        else{
            rPassword.setError("password is not match ");
            rvPassword.setError("password is not match");
        }
    }
}