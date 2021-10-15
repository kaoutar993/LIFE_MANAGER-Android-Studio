package com.khadija.makhchoun.lifemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.khadija.makhchoun.lifemanager.R.id.bottom_navigation;

public class NoteDetailActivity extends AppCompatActivity {

    EditText note_title,note_description;
    Button create,cancel;
    Integer note_no = new Random().nextInt();
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth auth;
    String NoteID = Integer.toString(note_no);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        note_title = findViewById(R.id.title);
        note_description = findViewById(R.id.description);
        create = findViewById(R.id.create_note);
        cancel = findViewById(R.id.cancel);
        BottomNavigationView bottomNavigationView=findViewById(bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.notes);

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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = note_title.getText().toString().trim();
                String description = note_description.getText().toString().trim();

                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) )
                {
                    createNote(title,description);
                }
                else
                {
                    Snackbar.make(v,"Champs vides !", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNote(final String title, final String description) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid()).child("Note"+note_no);
        databaseReference.keepSynced(true);
        final Map<String,Object> noteMap = new HashMap<>();
        noteMap.put("title", title);
        noteMap.put("description", description);
        noteMap.put("timestamp", ServerValue.TIMESTAMP);
        noteMap.put("NoteID",NoteID);
        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                databaseReference.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(NoteDetailActivity.this, "Note ajoutée", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NoteDetailActivity.this,BlocNotesActivity.class);
                            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            if(task.getException()!= null) {
                                Toast.makeText(NoteDetailActivity.this, "ERREUR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                });
            }
        });
        mainThread.start();
    }
}