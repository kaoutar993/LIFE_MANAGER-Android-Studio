package com.khadija.makhchoun.lifemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {

    TextView titlepage, subtitlepage, endpage;
    Button btnAddNew;
    DatabaseReference reference;
    RecyclerView ourdoes;
    ArrayList<MyDoes> list;
    DoesAdapter doesAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.todolist);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid = mUser.getUid();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        ToDoListActivity.this.finish();
                        return true;
                    case R.id.notes:
                        startActivity(new Intent(getApplicationContext(),BlocNotesActivity.class));
                        ToDoListActivity.this.finish();
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), com.khadija.makhchoun.lifemanager.BudgetActivity.class));
                        ToDoListActivity.this.finish();
                        return true;
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext(),ToDoListActivity.class));
                        ToDoListActivity.this.finish();
                        return true;
                    case R.id.documents:
                        startActivity(new Intent(getApplicationContext(),Documents.class));
                        ToDoListActivity.this.finish();
                        return true;
                    default:
                        return false;

                }

            }
        });
        titlepage= findViewById(R.id.titlepage);
        subtitlepage=findViewById(R.id.subtitlepage);
        endpage=findViewById(R.id.endpage);
        btnAddNew=findViewById(R.id.btnAddNew);

        btnAddNew.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent a = new Intent( ToDoListActivity.this, NewTaskActivity.class);
                startActivity(a);
            }
        });

        ourdoes= findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        list= new ArrayList<MyDoes>();

        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //code to recieve data
                for(DataSnapshot dataSnapshot1: datasnapshot.getChildren())
                {
                    MyDoes p= dataSnapshot1.getValue(MyDoes.class);
                    list.add(p);
                }
                doesAdapter=new DoesAdapter(ToDoListActivity.this,list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //show erreur
                Toast.makeText(getApplicationContext(), "Pas de données", Toast.LENGTH_SHORT).show();
            }
        });
    }
}