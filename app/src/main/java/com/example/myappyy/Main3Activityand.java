package com.example.myappyy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class Main3Activityand extends AppCompatActivity {
    private ImageView profile;
    private EditText eventname,date,targeted,info,link;
    private Button uploadBtn1,save;
    ProgressDialog progressDialog;

    private Uri postImageUri = null;


    String postdate,posttime,postrandomname,downloadurl;
    DatabaseReference useref,postref;

    String event,date1,target,info1,link1;



    private StorageReference storageReference;

    private FirebaseAuth firebaseAuth;



    String thumb_downloadurl;

    private Bitmap compressedImageFile=null;

    FirebaseAuth auth;
    String uid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_activityand);
        firebaseAuth = FirebaseAuth.getInstance();



        storageReference = FirebaseStorage.getInstance().getReference();


        postref= FirebaseDatabase.getInstance().getReference().child("Clubs").child("Androidclub");

        eventname=(EditText)findViewById(R.id.edandeventname) ;
        date=(EditText)findViewById(R.id.edanddate) ;
        targeted=(EditText)findViewById(R.id.eandtarget) ;
        uploadBtn1=(Button)findViewById(R.id.bandapload);
        info=(EditText)findViewById(R.id.eandprofile);
        link=(EditText)findViewById(R.id.edandlink);
        save=(Button)findViewById(R.id.bandsave);
        profile=(ImageView)findViewById(R.id.imgand) ;



        progressDialog=new ProgressDialog(this);

        uploadBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(Main3Activityand.this);

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                event=eventname.getText().toString();
                date1=date.getText().toString();
                info1=info.getText().toString();
                target=targeted.getText().toString();
                if(TextUtils.isEmpty(event))
                {
                    Toast.makeText(Main3Activityand.this,"Plaese enter the Branch Name",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(date1))
                {
                    Toast.makeText(Main3Activityand.this,"Plaese fill the Event Name",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(info1))
                {
                    Toast.makeText(Main3Activityand.this,"Plaese fill the Event Name",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(target))
                {
                    Toast.makeText(Main3Activityand.this,"Plaese fill the Event Name",Toast.LENGTH_LONG).show();
                }
                else if(postImageUri==null)
                {
                    Toast.makeText(Main3Activityand.this,"Plaese Select the Image to Post",Toast.LENGTH_LONG).show();
                }
                else {

                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();
                    progressDialog.setCancelable(false);


                    Calendar calendardate=Calendar.getInstance();
                    SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
                    postdate=currentdate.format(calendardate.getTime());

                    Calendar calendartime=Calendar.getInstance();
                    SimpleDateFormat currenttime=new SimpleDateFormat("hh:mm a");
                    posttime=currenttime.format(calendartime.getTime());

                    postrandomname=postdate+posttime;


                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);

                    final byte[] cropbyte=byteArrayOutputStream.toByteArray();

                    StorageReference filepath=storageReference.child("androidimages").child(postdate+posttime+".jpg");

                    final StorageReference thumb_filepath=storageReference.child("androidimages").child(postdate+posttime+".jpg");

                    filepath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                UploadTask uploadTask=thumb_filepath.putBytes(cropbyte);

                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task_thump) {

                                        thumb_downloadurl=task_thump.getResult().getDownloadUrl().toString();

                                        if(task_thump.isSuccessful())
                                        {
                                            savingpostinfo();

                                        }

                                    }
                                });


                            }

                        }
                    });


                }
            }
        });




    }

    private void savingpostinfo() {

        auth=FirebaseAuth.getInstance();
       uid=auth.getCurrentUser().getUid();
        link1=link.getText().toString();

        HashMap postmap=new HashMap();
        postmap.put("eventname",event);
        postmap.put("date",date1);
        postmap.put("info",info1);
        postmap.put("students",target);
        postmap.put("image",thumb_downloadurl);
        postmap.put("link",link1);
        postmap.put("uid",uid);
        postref.child(uid).updateChildren(postmap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Intent intent=new Intent(Main3Activityand.this,Main3Activityand_retrieve.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    progressDialog.dismiss();
                    String msg=task.getException().toString();
                    Toast.makeText(Main3Activityand.this,"Error Occured :"+msg,Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageUri = result.getUri();
                Picasso.get().load(postImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(profile);

                File crop_path=new File(postImageUri.getPath());


                try {
                    compressedImageFile=new Compressor(this)
                            .setMaxHeight(300)
                            .setMaxWidth(300).setQuality(100).compressToBitmap(crop_path);
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                }






            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();


            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int s=item.getItemId();
        switch (s){
            case R.id.i2:
                Intent i=new Intent(Main3Activityand.this,Main3Activityand_retrieve.class);
                break;

        }
        return true;
    }

}