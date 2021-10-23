//package com.example.istore.Adapter.Admin;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.istore.Model.ProdModel;
//import com.example.istore.R;
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//
//public class DisplayProductAdapter extends FirestoreRecyclerAdapter<ProdModel,DisplayProductAdapter.ProdHolder> {
//
//
//    private Context context;
//    EditProduct  editProduct;
//    private static final String TAG1 = "onBind";
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//    public DisplayProductAdapter(@NonNull FirestoreRecyclerOptions<ProdModel> options, Context context) {
//        super(options);
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ProdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        return new ProdHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.manger_stock_row, parent, false));
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull ProdHolder holder, int position, @NonNull ProdModel model) {
//        holder.pName.setText(model.getName());
//        holder.pCategory.setText(model.getCategory());
////        holder.pDesc.setText(model.getDescription());
//        holder.pQty.setText(model.getQuantity());
//        try {
//            // method to check Expiration date
//            checkProdExpiry(holder ,model);
//
//        } catch (Exception e){
//
//            holder.pExp.setText(model.getExpiry());
//            holder.pExp.setTextColor(Color.BLACK);
//        }
////        holder.pExp.setText(model.getExpiry());
//
//        try {
//            Glide.with(context).
//                    load(model.getImageUrl()).
//                    placeholder(R.drawable.ic_outline_no_image_24).
//                    into(holder.pImageView);
//        }
//        catch (Exception e){
//            holder.pImageView.
//                    setImageResource(R.drawable.ic_outline_no_image_24);
//
//        }
//        // onClick
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                detailsBottomSheet(model);
//            }
//        });
//
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void checkProdExpiry(ProdHolder holder, ProdModel model) {
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
////            int twoWeeks = 14;
////            int alreadyExpired = 0;
//
//        LocalDate todayDate = LocalDate.now();
//        String today = todayDate.format(formatter);
//        String futreDate = String.format(model.getExpiry(), formatter);
//
//        LocalDate date_1 = LocalDate.parse(today,formatter);
//        LocalDate date_2 = LocalDate.parse(futreDate,formatter);
//
//        // calculate difference between the dates
//        float diff = ChronoUnit.DAYS.between(date_1,date_2);
//
//        int i = (int) diff;
//
//        int myColor = Color.BLUE;
//
//        switch (i){
//
//            case 0:
//                holder.pExp.setText("Expired");
//                holder.pExp.setTextColor(Color.RED);
//                break;
//            case 1:
//                holder.pExp.setText("1" + " day");
//                holder.pExp.setTextColor(myColor);
//                break;
//            case 2:
//                holder.pExp.setText("2" + " days");
//                holder.pExp.setTextColor(myColor);
//                break;
//            case 3:
//                holder.pExp.setText("3" + " days");
//                holder.pExp.setTextColor(myColor);
//                break;
//            case 4:
//                holder.pExp.setText("4" + " days");
//                holder.pExp.setTextColor(myColor);
//                break;
//            case 5:
//                holder.pExp.setText("5" + " days");
//                holder.pExp.setTextColor(myColor);
//                break;
//            case 6:
//                holder.pExp.setText("6" + " days");
//                holder.pExp.setTextColor(myColor);
//                break;
//            case 7:
//                holder.pExp.setText("7" + " days");
//                holder.pExp.setTextColor(myColor);
//                break;
//            default:
//                // display
//                holder.pExp.setText(model.getExpiry());
//                holder.pExp.setTextColor(Color.BLACK);
//                holder.notifyAll();
//        }
//
//    }
//
//
//
//    private void detailsBottomSheet(ProdModel model) {
//
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//        View view = LayoutInflater.from(context)
//                .inflate(R.layout.bs_product_details_admin,null);
//        //set views to BottomSheet
//        bottomSheetDialog.setContentView(view);
//        // init views of BottomSheet
//        ImageButton backButton = view.findViewById(R.id.backBtn);
//        ImageButton deleteButton = view.findViewById(R.id.deleteBtn);
//        ImageButton editButton = view.findViewById(R.id.editBtn);
//        ImageView   prodImage = view.findViewById(R.id.productImageIV);
//        TextView    prodName = view.findViewById(R.id.prodNameTV);
//        TextView    prodCategory = view.findViewById(R.id.prodCategoryTV);
//        TextView    prodPrice = view.findViewById(R.id.prodPriceTV);
//        TextView    prodQuantity = view.findViewById(R.id.prodQuantityTV);
//        TextView    prodExpiryDate = view.findViewById(R.id.prodExpiryTV);
//        TextView    prodDesc = view.findViewById(R.id.prodDescriptionTV);
//
//        // get data
//        String uID = model.getId();
////        String image = model.getImageUrl();
//        String name = model.getName();
////        String price = model.getPrice();
////        String cat = model.getCategory();
////        String qty = model.getQuantity();
////        String exp = model.getExpiry();
////        String desc = model.getDescription();
//
//        //set data
//        prodName.setText("Name: "+model.getName());
//        prodCategory.setText("Category: "+model.getCategory());
//        prodDesc.setText("Desc: "+model.getDescription());
//        prodExpiryDate.setText("Last Date: "+model.getExpiry());
//        prodPrice.setText("Price: "+model.getPrice());
//        prodQuantity.setText("Quantity: "+model.getQuantity());
//        try {
//            Glide.with(context)
//                    .load(model.getImageUrl())
//                    .placeholder(R.drawable.ic_outline_no_image_24)
//                    .into(prodImage);
//        }
//        catch (Exception e){
//            prodImage.setImageResource(R.drawable.ic_outline_no_image_24);
//        }
//
//        bottomSheetDialog.show();
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog.dismiss();
//                // open edit product activity
//                Intent intent  = new Intent(context,EditProduct.class);
//                intent.putExtra("productId", uID);
//                context.startActivity(intent);
//
//            }
//        });
//
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bottomSheetDialog.dismiss();
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Delete")
//                        .setMessage("You confirm to delete " + name+" ?")
//                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                deleteProduct(uID);
//
//                            }
//                        })
//                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .show();
//            }
//        });
//
//    }
//
//
//    private void deleteProduct(String uID) {
//
//        firestore.collection("Products")
//                .document(uID)
//                .delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(context, "Item Deleted", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//    }
//
//    class ProdHolder extends RecyclerView.ViewHolder {
//
//        private ImageView pImageView;
//        private TextView pName, pQty, pExp, pCategory, pDesc;
//
//        public ProdHolder(@NonNull View itemView) {
//            super(itemView);
//
//            // ui init
//            pName      = itemView.findViewById(R.id.prodName_ID);
////            pDesc      = itemView.findViewById(R.id.prodDescId);
//            pCategory  = itemView.findViewById(R.id.itemCategoryTV);
//            pQty       = itemView.findViewById(R.id.prodQtyCardTV_ID);
//            pExp       = itemView.findViewById(R.id.prodExpiryCardTV_ID);
//            pImageView = itemView.findViewById(R.id.cardImage_id);
//        }
//    }
//}
