package com.example.topdoctest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputConfirmPassword ;

    private FirebaseAuth firebaseAuth ;
    private ProgressDialog mLoadBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //inputUserName = findViewById(R.id.inputUserName);
        inputEmail = findViewById(R.id.inputEmail); //username
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        mLoadBar = new ProgressDialog(SignUpActivity.this);

        findViewById(R.id.imageback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.textSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });
    }

    void checkCredentials() {

        //String username = inputUserName.getText().toString();
        String emailID = inputEmail.getText().toString();
        String pwd = inputPassword.getText().toString();
        String cpwd = inputConfirmPassword.getText().toString();

        if(emailID.isEmpty() || !emailID.contains("@"))
        {
            showError(inputEmail,"Your username is not valid");
        }
        else if(pwd.isEmpty() || pwd.length() < 5)
        {
            showError(inputPassword,"Your password is not valid");
        }
        else if(cpwd.isEmpty() || !(cpwd.equals(pwd)) )
        {
            showError(inputConfirmPassword,"Your password does not match");
        }
        else
        {
            mLoadBar.setTitle("Registeration");
            mLoadBar.setMessage("Please wait... Adding your credential to database");
            mLoadBar.setCanceledOnTouchOutside(false);
            mLoadBar.show();

            firebaseAuth.createUserWithEmailAndPassword(emailID, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        //get id of user and username and store

                        Toast.makeText(SignUpActivity.this, "Registeration is Successful", Toast.LENGTH_SHORT).show();
                        mLoadBar.dismiss();
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
