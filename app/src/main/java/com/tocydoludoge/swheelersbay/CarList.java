package com.tocydoludoge.swheelersbay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.tocydoludoge.swheelersbay.Common.Common;
import com.tocydoludoge.swheelersbay.Interface.ItemClickListener;
import com.tocydoludoge.swheelersbay.Model.Car;
import com.tocydoludoge.swheelersbay.Model.Category;
import com.tocydoludoge.swheelersbay.ViewHolder.CarViewHolder;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class CarList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton floatingActionButton;

    RelativeLayout rootLayout;

    StorageReference storageReference;

    Uri saveUri;


    DatabaseReference databaseReference;

    MaterialEditText edtName,edtRate,edtDescription;
    FButton btnSelect,btnUpload;
    Car newCar;

    String categoryId="";
    FirebaseRecyclerAdapter<Car, CarViewHolder>adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);




        //fb

        databaseReference=FirebaseDatabase.getInstance().getReference("Cars");

        storageReference=FirebaseStorage.getInstance().getReference();


        //views
        recyclerView=(RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);

        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            showAddCarDialog();


            }
        });

        if(getIntent() !=null)
            categoryId=getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty())



            loadCarList(categoryId);

    }

    private void showAddCarDialog() {



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CarList.this);
        alertDialog.setTitle("Add New Car");
        alertDialog.setMessage("Enter Full Details About Category");





        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_car_layout, null);
        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtRate = add_menu_layout.findViewById(R.id.edtRate);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);


        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);



        //Event for Button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);




        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                //snack dialog
                if (newCar != null){
                    databaseReference.push().setValue(newCar);
                    Snackbar.make(rootLayout, "New Car "+ newCar.getName()+" has been Added", Snackbar.LENGTH_SHORT)
                       .show();

                }
            }
        });



        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        alertDialog.show();





    }

    private void uploadImage() {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(CarList.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {



                                    newCar = new Car();
                                    newCar.setDescription(edtDescription.getText().toString());
                                    newCar.setName(edtName.getText().toString());
                                    newCar.setRate(edtRate.getText().toString());
                                    newCar.setMenuId(categoryId);
                                    newCar.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(CarList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage(" "+progress+" %");
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"\n" +
                "Select Image"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadCarList(String categoryId) {

    adapter=new FirebaseRecyclerAdapter<Car, CarViewHolder>(Car.class,
            R.layout.car_item,
            CarViewHolder.class,
            databaseReference.orderByChild("menuId").equalTo(categoryId)) {
        @Override
        protected void populateViewHolder(CarViewHolder viewHolder, Car model, int position) {


            viewHolder.carName.setText(model.getName());
            Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.carImage);

            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    //later
                }
            });
        }
    };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            saveUri = data.getData();
            btnSelect.setText("Selected images!");
        }


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE)){

            showUpdateCarDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));



        }else  if(item.getTitle().equals(Common.DELETE))
            deleteCar(adapter.getRef(item.getOrder()).getKey());

        return super.onContextItemSelected(item);
}

    private void deleteCar(String key) {
        databaseReference.child(key).removeValue();
    }

    private void showUpdateCarDialog(final String key, final Car item) {




            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CarList.this);
            alertDialog.setTitle("Edit Car");
            alertDialog.setMessage("Enter Full Details About Category");





            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.add_new_car_layout, null);
            edtName = add_menu_layout.findViewById(R.id.edtName);
            edtRate = add_menu_layout.findViewById(R.id.edtRate);
            edtDescription = add_menu_layout.findViewById(R.id.edtDescription);




            //set default values on view
            edtName.setText(item.getName());
        edtRate.setText(item.getRate());
        edtDescription.setText(item.getDescription());




        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
            btnUpload = add_menu_layout.findViewById(R.id.btnUpload);



            //Event for Button
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeImage(item);
                }
            });

            alertDialog.setView(add_menu_layout);
            alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);




            //Set button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();

                        item.setName(edtName.getText().toString());
                        item.setDescription(edtDescription.getText().toString());


                        databaseReference.child(key).setValue(item);
                        Snackbar.make(rootLayout, " Car "+ item.getName()+" was Edited", Snackbar.LENGTH_SHORT)
                                .show();


                }
            });



            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();










    }
   /* private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"\n" +
                "Select Image"),Common.PICK_IMAGE_REQUEST);
    }*/

    private void changeImage(final Car item) {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(CarList.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // atur nilai untuk newCategory jika gambar upload dan kita ambil download linknya.
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(CarList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage(" "+progress+" %");
                        }
                    });
        }
    }
}
