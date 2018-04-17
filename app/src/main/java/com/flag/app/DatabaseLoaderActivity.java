package com.flag.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import model.Image;

public class DatabaseLoaderActivity extends AppCompatActivity {
    private final int PICK_IMAGE = 1;

    private MarkerOptions marker;

    private long identificatorsAmount;

    //Database and Storage
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = ref.child("markers");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("img/");

    private Uri selectedImage = null;

    //UI
    private EditText name_editText, photoName_editText, desc_editText, date_editText;
    private Button send_btn, cancel_btn, pickImg_btn;
    private ImageView imageView;

    //Data
    private String name, photoName, desc, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_loader);

        Intent intent = getIntent();
        marker = (MarkerOptions) Objects.requireNonNull(intent.getExtras()).get("marker");

        initWidgets();

        pickImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, PICK_IMAGE);
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = name_editText.getText().toString();
                photoName = photoName_editText.getText().toString();
                desc = desc_editText.getText().toString();
                date = date_editText.getText().toString();

                if (name.equals("") || photoName.equals("") || desc.equals("") || date.equals("")){
                    Toast.makeText(DatabaseLoaderActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
                } else if (selectedImage == null) {
                    Toast.makeText(DatabaseLoaderActivity.this, "Select an image!", Toast.LENGTH_LONG).show();
                } else {
                    final HashMap<String, String> imgInfo = new HashMap<>();
                    StorageReference imgRef = storageReference.child(photoName.toLowerCase() + ".jpg");

                    imgRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Image imageUpload = new Image(photoName, taskSnapshot.getDownloadUrl().toString());
                            imgInfo.put("author", name);
                            imgInfo.put("date", date);
                            imgInfo.put("description", desc);
                            imgInfo.put("icon", "");
                            imgInfo.put("id", String.valueOf(identificatorsAmount));
                            imgInfo.put("url", imageUpload.url);
                            imgInfo.put("isChecked", "false");
                            imgInfo.put("lat", String.valueOf(marker.getPosition().latitude));
                            imgInfo.put("lng", String.valueOf(marker.getPosition().longitude));
                            imgInfo.put("photoName", photoName);

                            userRef.child(String.valueOf(identificatorsAmount)).setValue(imageUpload);

                            Toast.makeText(DatabaseLoaderActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DatabaseLoaderActivity.this, "Failed to upload image", Toast.LENGTH_LONG).show();
                        }
                    });

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            identificatorsAmount = dataSnapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Intent intent = new Intent(DatabaseLoaderActivity.this, MapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DatabaseLoaderActivity.this, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    //Selecting an image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView = findViewById(R.id.image_view);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setImageURI(selectedImage);
        }
    }

    private void initWidgets() {
        name_editText = findViewById(R.id.name_editText);
        photoName_editText = findViewById(R.id.photoName_editText);
        desc_editText = findViewById(R.id.desc_editText);
        date_editText = findViewById(R.id.date_editText);

        imageView = findViewById(R.id.image_view);

        pickImg_btn = findViewById(R.id.pickImg_btn);
        send_btn = findViewById(R.id.send_btn);
        cancel_btn = findViewById(R.id.cancel_btn);
    }
}
