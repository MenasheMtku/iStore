package com.example.istore.Employee;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.istore.Model.Categories;
import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProduct extends AppCompatActivity {
    // Keys
      private static final String  KEY_ID         = "id";
    // UI views
    private TextView categoryTv, activityTitle;
    private Button saveItemBtn;
    private ImageView selectDate;
    private ImageView itemImage;
    private ImageButton addImage;
    private EditText itemName, itemQuantity,itemExpriedDate;
    private RelativeLayout addDateRL;

    // permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    // permission arrays
    private String [] cameraPermissions;
    private String [] storagePermissions;
    // image picked URI
    private Uri image_uri;

    // Fireestore instance
    FirebaseFirestore db;
    CollectionReference dbReference;
    private FirebaseAuth firebaseAuth;

    // Progress Dialog
    ProgressDialog pd;
    Calendar cal;
    DatePickerDialog datePicker;
    String pId, pName, pQuantity,pExpiry;

    ProdModel prod;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        toolbar = findViewById(R.id.addToStorageTb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // init firestore
        db = FirebaseFirestore.getInstance();
        dbReference = db.collection("Products");
        firebaseAuth = FirebaseAuth.getInstance();
        // init model
        prod = new ProdModel();


        // init ui views
//        activityTitle = (TextView)findViewById(R.id.activityTitleTV);
        itemImage = (ImageView) findViewById(R.id.prodImageView);
        addImage = (ImageButton) findViewById(R.id.addImageButton);
        selectDate = (ImageView) findViewById(R.id.datePickImageView);
        itemName = (EditText) findViewById(R.id.etName);
        itemQuantity = (EditText) findViewById(R.id.etQuantity);
        itemExpriedDate = (EditText) findViewById(R.id.etExpiredDate);
        addDateRL = (RelativeLayout) findViewById(R.id.AddDateSection);
        categoryTv = (TextView) findViewById(R.id.etCategory);
        saveItemBtn = (Button) findViewById(R.id.saveBtn);
//        clearFields = (Button) findViewById(R.id.clearBtn);
//        addImageBtn = (Button) findViewById(R.id.addImage);


        // init permission arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);

        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pick category
                categoryDialog();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();        
            }
        });

        //choose date
        selectDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);


                datePicker = new DatePickerDialog(AddProduct.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

                        mMonth+=1;
                        String mt,dy;   //local variable
                        if(mMonth<10)
                            mt="0"+mMonth; //if month less than 10 then ad 0 before month
                        else mt=String.valueOf(mMonth);

                        if(mDay<10)
                            dy = "0"+mDay;
                        else dy = String.valueOf(mDay);

                        itemExpriedDate.setText(dy + "-" + mt  + "-" + mYear);
                        Log.i("Date Picked: ", dy+" - "+mt+" - "+mYear);
                    }
                }, day, month, year);


                datePicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePicker.show();
            }

        });

        saveItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    inputData();
                }
        });

    }

    private String prodNmae, prodCategory,prodQuantity,prodExpiary;
    private void inputData() {
        // input data
        prodNmae     = itemName.getText().toString().trim();
        prodCategory = categoryTv.getText().toString().trim();
        prodQuantity = itemQuantity.getText().toString().trim();
        prodExpiary  = itemExpriedDate.getText().toString().trim();

        // validate
        if(TextUtils.isEmpty(prodNmae)){
            Toast.makeText(this, "Item name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(prodCategory)){
            Toast.makeText(this, "Item category is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(prodQuantity)){
            Toast.makeText(this, "Item name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        // upload product to firstore
        uploadData(prodNmae,prodCategory ,prodQuantity, prodExpiary);
    }

    private void categoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Products Categories")
                .setItems(Categories.productCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get picked category
                        String category = Categories.productCategories[i];

                        if(category != "Food" && category != "Beverages"){

                            addDateRL.setVisibility(View.GONE);
                        }
                        else{
                            addDateRL.setVisibility(View.VISIBLE);
                        }

                        // set picked Category
                        categoryTv.setText(category);
                    }
                }).show();
    }

    private void showImagePickDialog() {

        String[] options = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image Source")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(i ==0){
                            // camera clicked
                            if(checkCameraePermission()){
                                // permission granted
                                pickFromCamera();
                            }else{
                                // // permission NOT granted
                                requestCameraPermission();
                            }
                        }
                        else{
                            // gallery clicked
                            if(checkStoragePermission()){
                                // permission granted
                                pickFromGallery();
                            }else{
                                // // permission NOT granted
                                requestStoragePermission();
                            }
                        }
                    }
                }).show();
    }

    private void pickFromGallery() {
        // intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {

        // intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result; // return true or false
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1; // return true or false
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,storagePermissions, CAMERA_REQUEST_CODE);
    }

    // handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        // both permission granted
                        pickFromCamera();
                    } else{
                        // both or one of permissions denied
                        Toast.makeText(this, "Camera and Storage permissions are required..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        // both permission granted
                        pickFromGallery();
                    } else{
                        // both or one of permissions denied
                        Toast.makeText(this, "Storage permission is required..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // handle image image results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){

            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                // image picked from gallery

                // save picked image uri
                image_uri = data.getData();

                // set image
                itemImage.setImageURI(image_uri);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                // image picked from camera

                itemImage.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadData(String name,String category, String qty, String expiry) {
        pd.setTitle("Adding To FireStore...");
        pd.show();

        final String timestamp = ""+System.currentTimeMillis();
        final String id = UUID.randomUUID().toString(); // random id to each data to be stored
        if(image_uri == null) {
            // upload without image

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("id",id);
            hashMap.put("name", name);
            hashMap.put("price", "");
            hashMap.put("description", "");
            hashMap.put("category", category);
            hashMap.put("quantity", ""+qty);
            hashMap.put("expiry", ""+expiry);
            hashMap.put("imageUrl", ""); //no image --> set empty
            hashMap.put("timeStamp", timestamp); // date and time when uploaded

            // Add this data
            db.collection("Products")
                    .document(id)
                    .set(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            clearData();
                            Toast.makeText(AddProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            // upload with image
            // -1- upload image to storage
            // -2- name and path of image to be uploaded
            String filePathAndName = "product_images/" + ""+id;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // upload image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();
                            String imageUrl = downloadImageUri.toString();

                            if(uriTask.isSuccessful()){
                                // url of image received
                                Log.i("message", ""+ downloadImageUri);
//                                String id = UUID.randomUUID().toString(); // random id to each data to be stored

                                Map<String, Object> hashMap = new HashMap<>();
                                hashMap.put(KEY_ID,""+id);
                                hashMap.put("name", name);
                                hashMap.put("price", "");
                                hashMap.put("description", "");
                                hashMap.put("category", category);
                                hashMap.put("quantity", ""+qty);
                                hashMap.put("expiry", ""+expiry);
                                hashMap.put("imageUrl", imageUrl);
                                hashMap.put("timeStamp", timestamp); // date and time when item uploaded/edited

                                db.collection("Products")
                                        .document(id)
                                        .set(hashMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                pd.dismiss();
                                                clearData();
                                                Toast.makeText(AddProduct.this,
                                                        "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddProduct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }


    }
    private void clearData() {
        itemName.setText("");
//        itemPrice.setText("");
        categoryTv.setText("Category");
        itemQuantity.setText("");
        itemExpriedDate.setText("");
        itemImage.setImageResource(R.drawable.ic_outline_prod_image_24);
        image_uri = null;
    }

//    private void editData(String id, String name,String categrory, String quantity, String expiry,String price) {
//        pd.setTitle("Updating Data...");
//        pd.show();
//        final String timestamp = ""+System.currentTimeMillis();
//        db.collection("Products").document(id)
//                .update(KEY_NAME, name,KEY_PRICE ,price,
//                        KEY_CATEGORY,categrory,KEY_QUANTITY,quantity,
//                        KEY_EXPIRY,expiry,"TimeStamp",timestamp)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        pd.dismiss();
//                        clearData();
//                        Toast.makeText(AddProduct.this, "Updated!", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                        Toast.makeText(AddProduct.this,e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }

}
