package com.khadija.makhchoun.lifemanager;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.util.Random;


public class NewTaskActivity extends AppCompatActivity {

    TextView titlepage, addtitle, adddesc, adddate;
    EditText titledoes, descdoes, datedoes;
    Button btnSaveTask, btnCancel;
    DatabaseReference reference;
    Integer doesNum = new Random().nextInt();
    String Keydoes = Integer.toString(doesNum);
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        titlepage = findViewById(R.id.titlepage);
        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);
        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid = mUser.getUid();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.todolist);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notes:
                        startActivity(new Intent(getApplicationContext(), BlocNotesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), com.khadija.makhchoun.lifemanager.BudgetActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext(), ToDoListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.documents:
                        startActivity(new Intent(getApplicationContext(), Documents.class));
                        overridePendingTransition(0, 0);
                        return true;
                    default:
                        return false;

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSaveTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // insert data to data base
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(uid).child("Does" + doesNum);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("titledoes").setValue(titledoes.getText().toString());
                        snapshot.getRef().child("descdoes").setValue(descdoes.getText().toString());
                        snapshot.getRef().child("datedoes").setValue(datedoes.getText().toString());
                        snapshot.getRef().child("Keydoes").setValue(Keydoes);
                        Intent a = new Intent(NewTaskActivity.this, ToDoListActivity.class);
                        NewTaskActivity.this.finish();
                        startActivity(a);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}