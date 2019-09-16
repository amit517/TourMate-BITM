package com.amitKundu.tourmate.BottomSheet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.amitKundu.tourmate.Activity.MemoryActivity;
import com.amitKundu.tourmate.Classes.MemoryClass;
import com.amitKundu.tourmate.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class BottomSheet_AddMemory extends BottomSheetDialogFragment {

    private ImageView uploadimae;
    private EditText memories_title, memories_desc;
    private Button uploadMemory;
    private static final int Gallery_Pick = 1;
    private Uri ImageUri;
    private String savecurrentdate, savecurrenttime, postrandomname, downloadurl, currentuser;
    private StorageReference postimagesreference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, postRef;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference userDB;
    private MemoryClass memoryClass;
    String cID;
    private Bitmap bitmapcam;
    private ProgressDialog loadinbar;

    static final int REQUEST_TAKE_PHOTO = 0;
    String currentPhotoPath;
    private BitmapFactory.Options boptions;


    String mtitle, mdesc;
    private ProgressDialog progressDialog;
    private MemoryActivity memoryActivity;

    public void setcID(String cID) {
        this.cID = cID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_add_memory, container, false);

        loadinbar = new ProgressDialog(getContext());
        uploadimae = view.findViewById(R.id.image_icon_BottomId);
        memories_title = view.findViewById(R.id.imageCaptionBottomId);
        memories_desc = view.findViewById(R.id.imageDescriptionBottomId);
        uploadMemory = view.findViewById(R.id.uploadImageIdbtn);


        firebaseDatabase = FirebaseDatabase.getInstance();
        postimagesreference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = firebaseAuth.getCurrentUser().getUid();


        // cID= getIntent().getStringExtra("curre");


        //cID= eventIdClass.getEventId();

        // Toast.makeText(getContext(), "" + cID, Toast.LENGTH_SHORT).show();
        uploadimae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //openGalary();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view1=LayoutInflater.from(getContext()).inflate(R.layout.alart_camera_option,null);

                builder.setView(view1);
                final Dialog dialog = builder.create();
                dialog.show();
                ImageView opencamera,opengelery;
                opencamera=view1.findViewById(R.id.cameraIvID);
                opengelery=view1.findViewById(R.id.galleryIvID);

                opengelery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalary();
                        dialog.dismiss();
                    }
                });

                opencamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCamera();
                    }
                });


            }
        });

        uploadMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                validatepostinfo();


            }
        });


        return view;
    }

    private void openCamera() {

        dispatchTakePictureIntent();

    }


    private void validatepostinfo() {
        mtitle = memories_title.getText().toString();
        mdesc = memories_desc.getText().toString();
        if (mtitle.isEmpty()) {
            memories_title.setError("You do not write anything yet");
            return;
        }
        if (mdesc.isEmpty()) {
            memories_desc.setError("You do not write anything yet");
            return;
        } else if (ImageUri == null) {
            Toast.makeText(getContext(), "Please select an Image", Toast.LENGTH_SHORT).show();
        } else {
//            progressDialog.setTitle("Add new Post");
//            progressDialog.setMessage("Updateing new post");
//            progressDialog.show();
//            progressDialog.setCanceledOnTouchOutside(true);
            loadinbar.setTitle("Add new memory");
            loadinbar.setMessage("Uploading new memory");
            loadinbar.show();
            loadinbar.setCanceledOnTouchOutside(true);
            storingImagetostorage();
        }
    }

    private void storingImagetostorage() {
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMM-yyyy");
        savecurrentdate = currentdate.format(callForDate.getTime());

        Calendar callfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm");
        savecurrenttime = currenttime.format(callForDate.getTime());

        postrandomname = savecurrentdate + savecurrenttime;
        StorageReference filepath = postimagesreference.child("Post Images").child(ImageUri.getLastPathSegment() + postrandomname + ".jpg");

        filepath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
//                    Task<Uri> downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl();
//                    downloadurl = downloadUrl.toString();
//                    Toast.makeText(AddMemory.this, "" + task.getException(), Toast.LENGTH_SHORT).show();

                    Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadurl = uri.toString();
                            savingPostInformationtoDatabase(new MemoryClass(mdesc, mtitle, downloadurl, savecurrentdate, savecurrenttime));
                        }
                    });


                    //savingPostInformationtoDatabase(new MemoryClass(mdesc, mtitle, downloadurl));
                }
            }
        });

    }

    private void savingPostInformationtoDatabase(final MemoryClass memoryClass) {

        DatabaseReference userDB = firebaseDatabase.getReference().child("UserList").child(currentuser).child("Events").child(cID);

        String memoryId = userDB.push().getKey();
        memoryClass.setMemory_id(memoryId);

        userDB.child("Memories").child(memoryId).setValue(memoryClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                    loadinbar.dismiss();
                    dismiss();
//                    uploadimae.setImageBitmap(bitmapcam);
//                    memories_title.setText("");
//                    memories_desc.setText("");

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




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.bitmproject.tourmate",
                        photoFile);
                ImageUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            setPic();
//        }
//    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = uploadimae.getWidth();
        int targetH = uploadimae.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, boptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, boptions);
        uploadimae.setImageBitmap(bitmap);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            uploadimae.setImageURI(ImageUri);
        }

        if (requestCode == 0) {
          setPic();

        }


    }


}
