package com.lessons.notes.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lessons.notes.Publisher;
import com.lessons.notes.PublisherHolder;
import com.lessons.notes.R;
import com.lessons.notes.note.domain.Note;

public class BaseNoteFragment extends Fragment implements PublisherHolder, NotesFragment.OnNoteClicked {
    private Publisher publisher = new Publisher();

    @Override
    public void onNoteClicked(Note note) {
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_note_info, NoteInfoFragment.newInstance(note))
                .addToBackStack(null).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        if (!getResources().getBoolean(R.bool.isLandscape)) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }
}
