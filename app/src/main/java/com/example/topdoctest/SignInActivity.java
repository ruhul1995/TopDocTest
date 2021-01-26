package com.example.topdoctest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Timestamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends SignUpActivity {

    private EditText inputEmail, inputPassWord;
    private FirebaseAuth mFirebaseAuth ;
    private ProgressDialog mLoadingBar;
    private String emailId, pwd, loginTime;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        inputEmail = findViewById(R.id.inputEmailID);
        inputPassWord = findViewById(R.id.inputPassword);
        mFirebaseAuth = FirebaseAuth.getInstance();
        myRef = database.getReference().child("Users");

        mLoadingBar = new ProgressDialog(SignInActivity.this);
        findViewById(R.id.textSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredential();
            }
        });
        findViewById(R.id.buttonForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
            }
        });
    }

    private void insertDataToFirebase()
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        emailId = inputEmail.getText().toString();
        loginTime = timestamp.toString();

        UserActivity usersData = new UserActivity(emailId,loginTime);
        //inserting the data to the firebase database
        //generating new id with every new item.

        myRef.push().setValue(usersData);
        Toast.makeText(SignInActivity.this, "Data inserted into firebase", Toast.LENGTH_SHORT).show();
    }

    private void checkCredential() {

        emailId = inputEmail.getText().toString();
        pwd = inputPassWord.getText().toString();

        //login event time
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        loginTime = timestamp.toString();

        if(emailId.isEmpty() || !emailId.contains("@") )
        {
            showError(inputEmail,"Your username is not valid");
        }
        else if(pwd.isEmpty() || pwd.length() < 5)
        {
            showError(inputPassWord,"Your password is not valid");
        }
        else
        {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait checking your credential in database");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mFirebaseAuth.signInWithEmailAndPassword(emailId,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        //get id of user and username and store
                        Toast.makeText(SignInActivity.this, "Login is Successful", Toast.LENGTH_SHORT).show();
                        mLoadingBar.dismiss();

                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);

                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("username" , emailId);
                        intent.putExtra("current_time",loginTime);
                        startActivity(intent);
                        insertDataToFirebase();
                    }
                    else
                    {
                        mLoadingBar.dismiss();
                        showError(inputEmail,"Your username does not exist in database");
                        Toast.makeText(SignInActivity.this, "Please sign up your account", Toast.LENGTH_LONG).show();
                        //Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }
    private void showError(EditText input, String s)
    {
        input.setError(s);
        input.requestFocus();
    }
}
