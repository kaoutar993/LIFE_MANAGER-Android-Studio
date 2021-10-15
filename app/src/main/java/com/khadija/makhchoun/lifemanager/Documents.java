package com.khadija.makhchoun.lifemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Documents extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView listView;
    private DatabaseReference databaseReference;
    private List<UploadPDF> uploadPDFList;
    private SearchView searchView;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    final ArrayList<String> keyList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        bottomNavigationView=findViewById(R.id.bottomNavigationbar);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads").child(uid);
        listView = (ListView) findViewById(R.id.myListView);
        uploadPDFList = new ArrayList<>();

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
                        startActivity(new Intent(getApplicationContext(),BlocNotesActivity.class));
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(),BudgetActivity.class));
                        return true;
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext(),ToDoListActivity.class));
                        return true;
                    case R.id.documents:
                        startActivity(new Intent(getApplicationContext(),Documents.class));
                        return true;
                    default:
                        return false;
                }
            }
        });

        searchView=(SearchView) findViewById(R.id.searchView);


        viewAllFiles();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UploadPDF uploadPDF = uploadPDFList.get(position);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(uploadPDF.getUrl()),"application/pdf");
                startActivity(intent);
            }
        });
    }

    private void viewAllFiles() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadPDFList.clear();
                Toast.makeText(getApplicationContext(),"Upload Documents ",Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadPDF uploadPDF = postSnapshot.getValue(UploadPDF.class);
                    uploadPDFList.add(uploadPDF);
                    keyList.add(postSnapshot.getKey());
                }
                ArrayList <String> uploads = new ArrayList<String>();



                for (int i =0; i <uploadPDFList.size(); i++) {
                    uploads.add(uploadPDFList.get(i).getName());

                }



                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, uploads);

                listView.setAdapter(adapter);
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        final int which_item=position;
                        new AlertDialog.Builder(Documents.this).setIcon(android.R.drawable.ic_delete)
                                .setTitle("Message de confirmation").setMessage("Est-ce que vous êtes sûr de vouloir supprimer ce document?")
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        uploadPDFList.remove(which_item);
                                        uploads.remove(which_item);
                                        adapter.notifyDataSetChanged();

                                        databaseReference.child(keyList.get(position)).removeValue();

                                    }
                                }).setNegativeButton("Non",null)
                                .show();
                        return true;

                    }


                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return false;

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void add(View view) {

        startActivity(new Intent(getApplicationContext(), SelectDoc.class));

    }

}