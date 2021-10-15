package com.khadija.makhchoun.lifemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTaskDesk extends AppCompatActivity {

    EditText titledoes, descdoes, datedoes;
    Button btnSaveUpdate, btnDelete;
    DatabaseReference reference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_desk);

        titledoes=findViewById(R.id.titledoes);
        descdoes=findViewById(R.id.descdoes);
        datedoes=findViewById(R.id.datedoes);
        btnSaveUpdate=findViewById(R.id.btnSaveUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid = mUser.getUid();
        // get data from previous page ( doesAdapter)
        titledoes.setText(getIntent().getStringExtra("titledoes"));
        descdoes.setText(getIntent().getStringExtra("descdoes"));
        datedoes.setText(getIntent().getStringExtra("datedoes"));
        final String keykeydoes = getIntent().getStringExtra("keydoes");
        reference= FirebaseDatabase.getInstance().getReference().child("DoesApp").child(uid).child("Does"+ keykeydoes);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.todolist);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notes:
                        startActivity(new Intent(getApplicationContext(),BlocNotesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), com.khadija.makhchoun.lifemanager.BudgetActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext(),ToDoListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.documents:
                        startActivity(new Intent(getApplicationContext(),Documents.class));
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return false;

                }

            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>(){

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent a=new Intent(EditTaskDesk.this,ToDoListActivity.class);
                            startActivity(a);
                        }else{
                            Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //button
        btnSaveUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("titledoes").setValue(titledoes.getText().toString());
                        snapshot.getRef().child("descdoes").setValue(descdoes.getText().toString());
                        snapshot.getRef().child("datedoes").setValue(datedoes.getText().toString());
                        snapshot.getRef().child("Keydoes").setValue(keykeydoes);
                        Intent a= new Intent(EditTaskDesk.this,ToDoListActivity.class);
                        EditTaskDesk.this.finish();
                        startActivity( a);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}