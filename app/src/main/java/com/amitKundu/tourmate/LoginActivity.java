package com.amitKundu.tourmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.amitKundu.tourmate.Activity.splashActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView registationTv,forgotPasswordTv;
    private EditText emailEt, passwordEt;
    private Button signinBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog loadinbar;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registationTv = findViewById(R.id.SignUpTvId);
        emailEt = findViewById(R.id.signInEmailEtId);
        passwordEt = findViewById(R.id.signInPasswordEtId);
        signinBtn = findViewById(R.id.signInBtnId);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        forgotPasswordTv = findViewById(R.id.forgotpassTvid);
        loadinbar = new ProgressDialog(this);






//////////////keep signed in/////////////

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out

            signinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = emailEt.getText().toString();
                    String password = passwordEt.getText().toString();


                    if (email.equals("") || password.equals("")) {
                        Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();

                    } else {

                        if (emailEt.getText().toString().trim().matches(emailPattern)) {
                            loadinbar.setTitle("SignIn");
                            loadinbar.setMessage("Signing In");
                            loadinbar.show();
                            loadinbar.setCanceledOnTouchOutside(true);
                            signInWithEmailAndPassword(email, password);

                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });


            registationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this,SignUp.class);
                    startActivity(intent);
                }
            });




            ///////////alart dialo
            forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
                    View view = layoutInflater.inflate(R.layout.alartdialog_fotget_password, null);

                    builder.setView(view);
                    final Dialog dialog = builder.create();
                    dialog.show();

                    final EditText email = view.findViewById(R.id.fotgetAlartDialogEmailEtId);
                    TextView sendActin = view.findViewById(R.id.fotgetpassAlartDialogSendTvId);
                    TextView cancel = view.findViewById(R.id.fotgetpassAlartDialogCancelTvId);

                    final String forgetEmail = email.getText().toString();

                    sendActin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (email.getText().toString().trim().matches(emailPattern)) {


                                String emailAddress =email.getText().toString();

                                firebaseAuth.sendPasswordResetEmail(emailAddress)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(LoginActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });

        }




    }

    private void signInWithEmailAndPassword(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    loadinbar.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,splashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
//                    String userId = firebaseAuth.getCurrentUser().getUid();
//                    showInformation(userId);
                }
                else {

                    Toast.makeText(LoginActivity.this, "Wrong Email or password", Toast.LENGTH_SHORT).show();
                    loadinbar.dismiss();
                }
            }
        });

    }
}
