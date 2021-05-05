package com.lessons.notes;

import com.lessons.notes.note.domain.Note;

public interface Observer {
    void updateNote(Note note);
}
