package com.lessons.notes;

import com.lessons.notes.domain.Note;

public interface Observer {
    void updateNote(Note note);
}
