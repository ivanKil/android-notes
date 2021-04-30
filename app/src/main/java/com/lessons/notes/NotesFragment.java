package com.lessons.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lessons.notes.domain.Note;
import com.lessons.notes.domain.NotesRepository;

public class NotesFragment extends Fragment implements Observer {

    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }

    private OnNoteClicked onCityClicked;
    private static final String CURRRENT_NOTE = "CURRRENT_NOTE";
    private Note currNote;
    private NotesRepository notesRepository;
    private Publisher publisher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    private void openNoteDetail(Note note) {
        if (onCityClicked != null) {
            onCityClicked.onNoteClicked(note);
        }
    }

    private void initList(View view) {
        LinearLayout layoutView = view.findViewById(R.id.list_layot);
        for (Note note : (notesRepository = new NotesRepository()).getNotes()) {
            TextView tv = new TextView(getContext());
            tv.setText(note.getName());
            tv.setTextSize(30);
            layoutView.addView(tv);
            tv.setOnClickListener(v -> {
                Note n = notesRepository.getNoteById(note.getId());
                currNote = n;
                openNoteDetail(n);
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRRENT_NOTE, currNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateNote(Note note) {
        notesRepository.updateNote(note);
        currNote = note;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PublisherHolder) {
            publisher = ((PublisherHolder) context).getPublisher();
            publisher.addObserver(this);
        }
        if (context instanceof OnNoteClicked) {
            onCityClicked = (OnNoteClicked) context;
        }
    }

    @Override
    public void onDetach() {
        onCityClicked = null;
        if (publisher != null) {
            publisher.removeObserver(this);
        }
        super.onDetach();
    }
}