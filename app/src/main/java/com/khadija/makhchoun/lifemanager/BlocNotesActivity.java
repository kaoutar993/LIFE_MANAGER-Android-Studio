package com.khadija.makhchoun.lifemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khadija.makhchoun.lifemanager.Model.ModelNotes;

import java.util.ArrayList;

import static com.khadija.makhchoun.lifemanager.R.id.bottom_navigation;

public class BlocNotesActivity extends AppCompatActivity {

    ImageView Username_img,search_btn;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView Username;
    EditText search;
    RecyclerView recyclerView;
    DatabaseReference reference;
    ArrayList<ModelNotes> list;
    NoteAdapter adapter;
    LottieAnimationView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloc_notes2);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Username_img = findViewById(R.id.Username_img);
        Username = findViewById(R.id.Username);
        auth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView=findViewById(bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.notes);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        BlocNotesActivity.this.finish();
                        return true;
                    case R.id.notes:
                        startActivity(new Intent(getApplicationContext(),BlocNotesActivity.class));
                        BlocNotesActivity.this.finish();
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), com.khadija.makhchoun.lifemanager.BudgetActivity.class));
                        BlocNotesActivity.this.finish();
                        return true;
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext(),ToDoListActivity.class));
                        BlocNotesActivity.this.finish();
                        return true;
                    case R.id.documents:
                        startActivity(new Intent(getApplicationContext(),Documents.class));
                        BlocNotesActivity.this.finish();
                        return true;
                    default:
                        return false;

                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlocNotesActivity.this,NoteDetailActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user!=null){
            reference = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());
        }
        reference.keepSynced(true);
        empty = findViewById(R.id.empty);
        search = findViewById(R.id.search);
        search_btn = findViewById(R.id.search_btn);
    }

    public void searchData(View view) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());
        Query query = ref.orderByChild("title").equalTo(search.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ModelNotes model = postSnapshot.getValue(ModelNotes.class);
                    list.add(model);
                }
                adapter = new NoteAdapter(BlocNotesActivity.this, list);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BlocNotesActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BlocNotesActivity.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    Refresh(1000);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void Refresh(int time) {
        final Handler handler= new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showAll();
            }
        };
        handler.postDelayed(runnable,time);
    }

    public void showAll() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    ModelNotes model =dataSnapshot1.getValue(ModelNotes.class);
                    list.add(model);

                }
                adapter = new NoteAdapter(BlocNotesActivity.this,list);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BlocNotesActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount()==0){
                    recyclerView.setVisibility(View.INVISIBLE);
                    empty.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showAll();
    }
}