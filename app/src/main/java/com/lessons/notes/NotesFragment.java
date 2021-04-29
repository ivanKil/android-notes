package com.lessons.notes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lessons.notes.domain.Note;
import com.lessons.notes.domain.NotesRepository;

public class NotesFragment extends Fragment implements Observer {
    private boolean isLandscape;
    private static final String CURRRENT_NOTE = "CURRRENT_NOTE";
    private Note currNote;
    private NotesRepository notesRepository;
    private Publisher publisher;
    public static int REQUEST_CHANGE_NOTE = 1;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currNote = savedInstanceState.getParcelable(CURRRENT_NOTE);
        }

        if (isLandscape) {
            showLandNoteInfo(currNote);
        }
    }

    private void showNoteInfo(Note note) {
        if (isLandscape) {
            showLandNoteInfo(note);
        } else {
            showPortCoatOfArms(note);
        }
    }

    private void showLandNoteInfo(Note note) {
        NoteInfoFragment detail = NoteInfoFragment.newInstance(note);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_note_info, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
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
                showNoteInfo(n);
            });
        }
    }

    private void showPortCoatOfArms(Note note) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NoteInfoActivity.class);
        intent.putExtra(NoteInfoFragment.ARG_NOTE, note);
        startActivityForResult(intent, REQUEST_CHANGE_NOTE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (REQUEST_CHANGE_NOTE == requestCode && resultCode == NoteInfoFragment.NOTE_CHANGED) {
            Note n = data.getParcelableExtra(NoteInfoFragment.EDITED_NOTE);
            updateNote(n);
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
    }

    @Override
    public void onDetach() {
        if (publisher != null) {
            publisher.removeObserver(this);
        }
        super.onDetach();
    }
}