package com.amitKundu.tourmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private EditText emailEt, passwordEt, confirmpassEt, fnameET, lnameEt;
    private Button signUp;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference postimagesreference;
    private String savecurrentdate, savecurrenttime, postrandomname, downloadurl, currentuser;
    private String imageUrl;
    private ProgressDialog loadinbar;


    private static final int Gallery_Pick = 1;
    private Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEt = findViewById(R.id.emailSignUpEtId);
        passwordEt = findViewById(R.id.passwordSignUpEtId);
        confirmpassEt = findViewById(R.id.confirmpasswordSignUpEtId);
        fnameET = findViewById(R.id.firtnameSignUpEtId);
        lnameEt = findViewById(R.id.lastnameSignUpEtId);
        signUp = findViewById(R.id.SignUpbtnId);
        imageView = findViewById(R.id.cameraSignUpIvId);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        loadinbar = new ProgressDialog(this);

        postimagesreference = FirebaseStorage.getInstance().getReference();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalary();
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstName = fnameET.getText().toString();
                final String lastName = lnameEt.getText().toString();
                final String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();
                final String confirmPassword = confirmpassEt.getText().toString();


                Calendar callForDate = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMM-yyyy");
                savecurrentdate = currentdate.format(callForDate.getTime());

                Calendar callfortime = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm");
                savecurrenttime = currenttime.format(callForDate.getTime());

                postrandomname = savecurrentdate + savecurrenttime;

                //signUpWithEmailAndPassword(firstName, lastName, email, password);


                if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")) {

                    Toast.makeText(SignUp.this, "All the fields are required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.length() < 6) {
                    Toast.makeText(SignUp.this, "password would be upto 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ImageUri == null) {
                    Toast.makeText(SignUp.this, "Please select an Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (emailEt.getText().toString().trim().matches(emailPattern)) {

                    if (confirmPassword.contains(password)) {


                        loadinbar.setTitle("SignUp");
                        loadinbar.setMessage("Signing up");
                        loadinbar.show();
                        loadinbar.setCanceledOnTouchOutside(true);

                        final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Post Images").child(ImageUri.getLastPathSegment() + postrandomname + ".jpg");
                        filepath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            downloadurl = uri.toString();
                                            signUpWithEmailAndPassword(firstName, lastName, email, password, downloadurl);
                                            // savingPostInformationtoDatabase(new MemoryClass(mdesc, mtitle, downloadurl, savecurrentdate, savecurrenttime));
                                        }
                                    });


                                    //savingPostInformationtoDatabase(new MemoryClass(mdesc, mtitle, downloadurl));
                                }
                            }
                        });


                    } else {

                        Toast.makeText(SignUp.this, "password not match", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(SignUp.this, "Invalid Email address", Toast.LENGTH_SHORT).show();
                }
//

            }
        });


    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void signUpWithEmailAndPassword(final String firstName, String lastName, String email, final String password, final String image) {

        final User user = new User(firstName, lastName, email);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    String userId = firebaseAuth.getCurrentUser().getUid();
                    user.setUserId(userId);
                    user.setProfilePhoto(image);

                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserList").child(userId);

                    databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                emailEt.setText("");
                                passwordEt.setText("");
                                confirmpassEt.setText("");
                                fnameET.setText("");
                                lnameEt.setText("");
                                imageView.setVisibility(View.INVISIBLE);
                                loadinbar.dismiss();
                                Toast.makeText(SignUp.this, "Sign Up success", Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(SignUp.this, LoginActivity.class);
                                 startActivity(intent);
                            } else {
                                Toast.makeText(SignUp.this, "Sign Up not success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });


    }

    private void openGalary() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Gallery_Pick);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }

}
