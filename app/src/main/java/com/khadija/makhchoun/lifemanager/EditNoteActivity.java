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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import static com.khadija.makhchoun.lifemanager.R.id.bottom_navigation;

public class EditNoteActivity extends AppCompatActivity {

    EditText edit_title,edit_des;
    Button edit_note,delete_note;
    DatabaseReference reference;
    String Key;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("EditNote") ;
        }
        edit_title = findViewById(R.id.title_edit);
        edit_des = findViewById(R.id.description_edit);
        edit_note = findViewById(R.id.edit_note);
        delete_note= findViewById(R.id.delete_note);
        edit_title.setText(getIntent().getStringExtra("title"));
        edit_des.setText(getIntent().getStringExtra("description"));
        Key = getIntent().getStringExtra("NoteID");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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

        edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid()).child("Note"+ Key);
                final Map<String,Object> noteMap = new HashMap<>();
                noteMap.put("title", edit_title.getText().toString());
                noteMap.put("description", edit_des.getText().toString());
                noteMap.put("timestamp", ServerValue.TIMESTAMP);
                noteMap.put("NoteID",Key);
                final Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        reference.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditNoteActivity.this, "Note modifiée", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditNoteActivity.this,BlocNotesActivity.class);
                                    overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
                                    startActivity(intent);
                                }
                                else
                                {
                                    if (task.getException()!=null) {
                                        Toast.makeText(EditNoteActivity.this, "ERREUR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        });
                    }
                });
                mainThread.start();
            }
        });

        delete_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid()).child("Note"+ Key);
                reference.removeValue();
                comeout(edit_title.getText().toString(),edit_des.getText().toString());
                Intent a=new Intent(EditNoteActivity.this,BlocNotesActivity.class);
                startActivity(a);

            }
        });

    }

    private void comeout(String title,String desc) {
        Intent intent = new Intent(EditNoteActivity.this,MainActivity.class);
        overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
        startActivity(intent);
        finish();
    }
}