package com.example.javascreenlibrairy;

import android.app.Activity;
import android.view.*;
import android.widget.*;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Note> {

    public NotesAdapter(Activity context, List<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        Note note = getItem(pos);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(note.getTitle());

        return convertView;
    }
}
