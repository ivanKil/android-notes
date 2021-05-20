package com.lessons.notes;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lessons.notes.note.NoteInfoFragment;
import com.lessons.notes.note.domain.Note;
import com.lessons.notes.note.edit.NoteEditFragment;

public class AppRouter {
    private final String EDIT_TAG = "EDIT_TAG";
    private final FragmentManager fragmentManager;
    private final Context context;

    public AppRouter(FragmentManager fragmentManager, Context context) {
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    private void showInfo(Note note) {
        fragmentManager.beginTransaction()
                .replace(R.id.frame_note_info, NoteInfoFragment.newInstance(note))
                .addToBackStack(null).commit();
    }

    public void showNoteEdit(Note note) {
        if (context.getResources().getBoolean(R.bool.isLandscape)) {
            if (note != null)
                showInfo(note);
        } else {
            showNotes();
        }
    }

    public void showNoteInfo(Note note) {
        if (note != null && note.isForEdit()) {
            Fragment f = fragmentManager.findFragmentByTag(EDIT_TAG);
            if (f != null) {
                if (((NoteEditFragment) f).isHidden())
                    ((NoteEditFragment) f).show(fragmentManager, EDIT_TAG);
            } else {
                NoteEditFragment.newInstance(note).show(fragmentManager, EDIT_TAG);
            }
        } else {
            showInfo(note);
        }
    }

    public void showNotes() {
        if (!context.getResources().getBoolean(R.bool.isLandscape)) {
            Fragment info = fragmentManager.findFragmentById(R.id.frame_note_info);
            if (info != null)
                fragmentManager.beginTransaction().hide(info).commit();
        }
    }
}
