package com.lessons.notes.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lessons.notes.note.domain.IRepository;
import com.lessons.notes.note.domain.Note;
import com.lessons.notes.note.domain.NotesRepositoryFirestore;

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


    private final IRepository repository = new NotesRepositoryFirestore();

    public LiveData<List<Note>> getNotesLiveData() {
        return notesLiveData;
    }

    public void requestNotes() {
        repository.init(notes -> {
                    notesLiveData.setValue(notes);
                }
        );
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
        repository.updateNote(note, notes -> {
            notesLiveData.setValue(notes);
        });
        savedNote.setValue(null);

    }

    public void delete(Note note) {
        repository.delete(note, notes -> notesLiveData.setValue(notes));

    }
}