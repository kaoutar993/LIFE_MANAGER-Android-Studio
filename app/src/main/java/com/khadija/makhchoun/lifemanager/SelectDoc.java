package com.khadija.makhchoun.lifemanager;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SelectDoc extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    EditText file_name;
    Button upload_btn;
    private FirebaseAuth mAuth;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doc);
        bottomNavigationView=findViewById(R.id.bottomNavigationbar);
        file_name = (EditText) findViewById(R.id.file_name);
        upload_btn = (Button) findViewById(R.id.upload_btn);
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads").child(uid);

        bottomNavigationView.setSelectedItemId(R.id.documents);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        return true;
                    case R.id.notes:
                        startActivity(new Intent(getApplicationContext(), BlocNotesActivity.class));
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), BudgetActivity.class));
                        return true;
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext(), ToDoListActivity.class));
                        return true;
                    case R.id.documents:
                        startActivity(new Intent(getApplicationContext(),Documents.class));
                        return true;
                    default:
                        return false;
                }
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFFile();

            }
        });

    }

    private void selectPDFFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uploadPDFFile(data.getData());

        }
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(SelectDoc.this);
        progressDialog.show();

        StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();
                        if(file_name.getText().toString().isEmpty()){
                            file_name.setText("Unknown"+System.currentTimeMillis());
                            Toast.makeText( SelectDoc.this, "Nom du fichier est donné par défaut", Toast.LENGTH_SHORT).show();
                        }
                        UploadPDF uploadPDF = new UploadPDF(file_name.getText().toString(), url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
                        Toast.makeText(SelectDoc.this, "Document ajouté", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //startActivity(new Intent(getApplicationContext(), Documents.class));
                        Toast.makeText(getApplicationContext(),"SelectDoc",Toast.LENGTH_SHORT).show();
                        SelectDoc.this.finish();

                    }

                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Ajout du document..");

            }


        });

    }



}