package com.lessons.notes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.lessons.notes.domain.Note;

public class MainActivity extends AppCompatActivity implements PublisherHolder, NotesFragment.OnNoteClicked {

    private Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!getResources().getBoolean(R.bool.isLandscape)) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    @Override
    public void onNoteClicked(Note note) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_note_info, NoteInfoFragment.newInstance(note))
                .addToBackStack(null).commit();
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

}