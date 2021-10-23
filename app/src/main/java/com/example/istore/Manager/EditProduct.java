package com.example.istore.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.istore.Employee.ViewStorage;
import com.example.istore.Model.Categories;
import com.example.istore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



public class EditProduct extends AppCompatActivity {
    // Keys
    private static final String  KEY_ID  = "id";
    // UI views
    Toolbar toolbar;
    private TextView itemcategory;
    private ImageButton  changeImage;
    private Button updatetemBtn;
    private ImageView selectDate;
    private ImageView itemImage;
    private EditText itemName, itemQuantity, itemPrice,itemExpriedDate , itemDesc;
    private RelativeLayout editDateRL;

    private  String productId;
    private  String timeStampImageStorage;

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
    private ProgressDialog progressDialog;

    Calendar cal;
    DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        toolbar = findViewById(R.id.editProductTb);
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
        // init ui views
//        gobackBtn = findViewById(R.id.backButton);
        changeImage = (ImageButton) findViewById(R.id.changeImageButton);
        itemImage = (ImageView) findViewById(R.id.imageEditIv);
        selectDate = (ImageView) findViewById(R.id.datePickImageView);
        itemName = (EditText) findViewById(R.id.etName);
        itemQuantity = (EditText) findViewById(R.id.etQuantity);
        itemExpriedDate = (EditText) findViewById(R.id.etExpDate);
        itemPrice = (EditText) findViewById(R.id.etPric);
        itemcategory = (TextView) findViewById(R.id.etCategory);
        itemDesc = (EditText) findViewById(R.id.etDescription);
        updatetemBtn = (Button) findViewById(R.id.updateBtn);
//        addImageBtn = (Button) findViewById(R.id.addImage);
        editDateRL = findViewById(R.id.dateSection);

        // get product id from intent(CustomAdapter)
        productId  = getIntent().getStringExtra("productId");

        // init permission arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};


        loadProductDetails();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

//        gobackBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(EditProduct.this, ViewStock.class));
//                finish();
//            }
//        });
        itemcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pick category
                categoryDialog();
            }
        });
        changeImage.setOnClickListener(new View.OnClickListener() {
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


                datePicker = new DatePickerDialog(EditProduct.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
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

        updatetemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    inputData();
            }
        });


    }

    private void loadProductDetails() {

        dbReference.document(productId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                //get data
                String productId = task.getResult().getString(KEY_ID);
                String productName = task.getResult().getString("name");
                String productCategory = task.getResult().getString("category");
                String productDescription = task.getResult().getString("description");
                String productPrice = task.getResult().getString("price");
                String productExpiry = task.getResult().getString("expiry");
                String productQuantity = task.getResult().getString("quantity");
                String productImageURL = task.getResult().getString("imageUrl");
                String timeStamp = task.getResult().getString("timeStamp");

                Log.i("TAG", "onComplete: \n" + productImageURL +"\n"
                                                        + productName+"\n"
                                                        + productExpiry+"\n"
                                                        + productPrice+"\n"
                                                        + productCategory+"\n");

                //set data to views
//                timeStampImageStorage = timeStamp;
                itemName.setText(productName);
                itemcategory.setText(productCategory);
                itemDesc.setText(productDescription);
                itemPrice.setText(productPrice);
                itemQuantity.setText(productQuantity);
                itemExpriedDate.setText(productExpiry);
                itemImage.setImageURI(Uri.parse(productImageURL));

                try {
                    Glide.with(getApplicationContext()).
                            load(productImageURL)
                            .placeholder(R.drawable.ic_outline_no_image_24).
                            into(itemImage);
                }
                catch (Exception e){
                    itemImage.setImageResource(R.drawable.ic_outline_no_image_24);

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProduct.this,
                                ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

                            editDateRL.setVisibility(View.GONE);
                        }
                        else{
                            editDateRL.setVisibility(View.VISIBLE);
                        }

                        // set picked Category
                        itemcategory.setText(category);
                    }
                }).show();
    }

    private String prodName, prodPrice,prodDescription,prodCategory,prodQuantity, prodExpire;
    private void inputData() {
        // input data
        prodName        = itemName.getText().toString().trim();
        prodCategory    = itemcategory.getText().toString().trim();
        prodPrice       = itemPrice.getText().toString().trim();
        prodQuantity    = itemQuantity.getText().toString().trim();
        prodExpire      = itemExpriedDate.getText().toString().trim();
        prodDescription = itemDesc.getText().toString().trim();


        // validate
        if(TextUtils.isEmpty(prodName)){
            Toast.makeText(this,
                    "Item name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(prodCategory)){
            Toast.makeText(this,
                    "Item category is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(prodQuantity)){
            Toast.makeText(this,
                    "Item Quantity is required", Toast.LENGTH_SHORT).show();
            return;
        }


        updateProduct();
    }

    private void updateProduct() {

        progressDialog.setTitle("Updating...");
        progressDialog.show();

        final String timestamp = ""+System.currentTimeMillis();
        if(image_uri == null){
            // upload without new photo
            Map<String, Object> hashMap = new HashMap<>();

            hashMap.put("name", prodName);
            hashMap.put("price", prodPrice);
            hashMap.put("category", prodCategory);
            hashMap.put("description", prodDescription);
            hashMap.put("quantity", prodQuantity);
            hashMap.put("expiry", prodExpire);
            hashMap.put("imageUrl", ""); //no image --> set empty
            hashMap.put("timeStamp", ""+timestamp); // date and time when uploaded

            // Add this data
            db.collection("Products")
                    .document(productId)
                    .update(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            clearData();
                            Toast.makeText(EditProduct.this,
                                    "Edited successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProduct.this,
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            // upload with photo
            // -1- upload image to storage
            // -2- name and path of image to be uploaded
            String filePathAndName = "product_images/" + ""+productId;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // upload image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();
                            final String imageUrl = downloadImageUri.toString();

                            if(uriTask.isSuccessful()){
                                // url of image received
                                String id = UUID.randomUUID().toString(); // random id to each data to be stored

                                Map<String, Object> hashMap = new HashMap<>();
                                hashMap.put("name", prodName);
                                hashMap.put("price", prodPrice);
                                hashMap.put("category", prodCategory);
                                hashMap.put("description", prodDescription);
                                hashMap.put("quantity", prodQuantity);
                                hashMap.put("expiry", prodExpire);
                                hashMap.put("imageUrl", imageUrl);
                                hashMap.put("timeStamp", ""+timestamp); // date and time when uploaded

                                db.collection("Products")
                                        .document(productId)
                                        .update(hashMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
//                                                timeStampImageStorage = ""; //clear image timestamp file name
                                                clearData();
                                                Toast.makeText(EditProduct.this,
                                                        "Edited successfully", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
//                                                timeStampImageStorage = ""; //clear image timestamp file name
                                                Toast.makeText(EditProduct.this,
                                                        e.getMessage(),Toast.LENGTH_LONG).show();
                                                Log.i("TAG", "onFailure: "+e.getMessage());
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProduct.this,
                                    ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void clearData() {

        itemName.setText("");
        itemPrice.setText("");
        itemcategory.setText("Select category");
        itemDesc.setText("");
        itemQuantity.setText("");
        itemExpriedDate.setHint("dd/MM/yyyy");
        itemImage.setImageResource(R.drawable.ic_outline_prod_image_24);
        image_uri = null;

        startActivity(new Intent(EditProduct.this, StorageManager .class));
        finish();
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
                            if(!checkCameraePermission()){

                                // permission NOT granted
                                requestCameraPermission();
                            }else{
                                // permission granted
//                                pickFromCamera();
                                pickFromGallery();
                            }
                        }
                        else if (i == 1){
                            // gallery clicked
                            if(!checkStoragePermission()){
                                // // permission NOT granted
                                requestStoragePermission();
                            }else{
                                // permission granted
                                pickFromGallery();
                            }
                        }
                    }
                }).show();
    }

    private void pickFromGallery() {

        CropImage.activity().start(this);
//        // intent to pick image from gallery
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
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
    // handle image image results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode ==RESULT_OK ){
                image_uri = result.getUri();
                Picasso.with(this)
                        .load(image_uri)
                        .into(itemImage);
            }
        }
//        if(resultCode == RESULT_OK){
//
//            if(requestCode == IMAGE_PICK_GALLERY_CODE){
//                // image picked from gallery
//
//                // save picked image uri
//                image_uri = data.getData();
//
//                // set image
//                itemImage.setImageURI(image_uri);
//            }
//            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
//                // image picked from camera
//
//                itemImage.setImageURI(image_uri);
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);
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
            break;
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
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}