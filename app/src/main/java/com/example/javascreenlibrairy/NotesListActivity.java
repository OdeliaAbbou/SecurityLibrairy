package com.example.javascreenlibrairy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securitylib.SecurityConfig;
import com.example.securitylib.SecuritySwitchDetector;
import com.example.securitylib.SecurityUtils;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class NotesListActivity extends BaseSecureActivity{
    private ListView listView;
    private NotesAdapter adapter;
    private ArrayList<Note> notes;

    @Override
    protected SecurityConfig provideSecurityConfig() {
        return new SecurityConfig.Builder()
                .disableScreenshots(true)
                .disableCopyPaste(true)
                .disableRecentAppsPreview(true)
                .build();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        listView = findViewById(R.id.notesListView);
        notes = new ArrayList<>();
        adapter = new NotesAdapter(this, notes);
        listView.setAdapter(adapter);

        findViewById(R.id.btnAddNote).setOnClickListener(v ->
                startActivity(new Intent(this, AddNoteActivity.class)));

        FirebaseDatabase.getInstance().getReference("notes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notes.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Note n = snap.getValue(Note.class);
                            if (n != null) notes.add(n);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Note clickedNote = notes.get(position);
            Intent intent = new Intent(this, NoteDetailActivity.class);
            intent.putExtra("title", clickedNote.getTitle());
            intent.putExtra("content", clickedNote.getContent());
            startActivity(intent);
        });
    }





}
