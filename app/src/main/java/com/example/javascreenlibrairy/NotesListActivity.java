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

public class NotesListActivity extends AppCompatActivity {
    private ListView listView;
    private NotesAdapter adapter;
    private ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        // Sécurité (lib perso)
        SecurityConfig config = new SecurityConfig.Builder()
                .disableScreenshots(true)
                .disableCopyPaste(true)
                .disableRecentAppsPreview(true)
                .build();
        SecurityUtils.applySecurity(this, config);

        listView = findViewById(R.id.notesListView);
        notes = new ArrayList<>();
        adapter = new NotesAdapter(this, notes);
        listView.setAdapter(adapter);

        // Aller à la page d’ajout
        findViewById(R.id.btnAddNote).setOnClickListener(v ->
                startActivity(new Intent(this, AddNoteActivity.class)));

        // Chargement Firebase
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

        // Clic sur une note → Détails
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Note clickedNote = notes.get(position);
            Intent intent = new Intent(this, NoteDetailActivity.class);
            intent.putExtra("title", clickedNote.getTitle());
            intent.putExtra("content", clickedNote.getContent());
            startActivity(intent);
        });
    }

@Override
protected void onResume() {
    super.onResume();
    SecuritySwitchDetector.onAppResume(this);
    Log.d(getClass().getSimpleName(), "🟢 Activity resumed");
}

    @Override
    protected void onPause() {
        super.onPause();
        SecuritySwitchDetector.onAppPause(this);
        Log.d(getClass().getSimpleName(), "🟡 Activity paused");
    }


}
