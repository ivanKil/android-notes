package com.lessons.notes.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lessons.notes.note.domain.Note;
import com.lessons.notes.note.domain.NotesRepository;

import java.util.List;

public class NoteViewModel extends ViewModel {

    private final MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Note> savedNote = new MutableLiveData<>();
    private final MutableLiveData<Note> selected = new MutableLiveData<>();

    public void select(Note item) {
        selected.setValue(item);
    }

    public LiveData<Note> getSelected() {
        return selected;
    }

    public void saveNote(Note item) {
        savedNote.setValue(item);
    }

    public LiveData<Note> getSavedNote() {
        return savedNote;
    }


    private final NotesRepository repository = new NotesRepository();

    public LiveData<List<Note>> getNotesLiveData() {
        return notesLiveData;
    }

    public void requestNotes() {
        List<Note> notes = repository.getNotes();
        notesLiveData.setValue(notes);
    }

    public void filterByName(String text) {
        notesLiveData.setValue(repository.filterByName(text));
    }

    public void sortByName() {
        notesLiveData.setValue(repository.sortByName());
    }

    public void sortByDate() {
        notesLiveData.setValue(repository.sortByDate());
    }

    public void updateNote(Note note) {
        note.setForEdit(false);
        repository.updateNote(note);
        savedNote.setValue(null);
    }

    public void delete(Note note) {
        int ind = repository.delete(note);
        notesLiveData.setValue(repository.getNotes());
        select(repository.getNearNote(ind));
    }
}