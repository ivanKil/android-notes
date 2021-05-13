package com.lessons.notes.note.domain;

import java.util.ArrayList;
import java.util.List;

public interface IRepository {
    List<Note> getNotes();

    void updateNote(Note note, OnUpdateNotesResponse onUpdateNotesResponse);

    ArrayList<Note> filterByName(String text);

    ArrayList<Note> sortByName();

    ArrayList<Note> sortByDate();

    void delete(Note note, OnUpdateNotesResponse onUpdateNotesResponse);

    IRepository init(OnUpdateNotesResponse onUpdateNotesResponse);
}
