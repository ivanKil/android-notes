package com.lessons.notes.note.domain;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotesRepositoryFirestore implements IRepository {
    private static final String NOTES_COLLECTION = "notes";
    private static final String TAG = "[NotesFirestore]";

    private final FirebaseFirestore store = FirebaseFirestore.getInstance();
    private final CollectionReference collection = store.collection(NOTES_COLLECTION);
    private ArrayList<Note> notes = new ArrayList<>();


    @Override
    public List<Note> getNotes() {
        return null;
    }

    @Override
    public void updateNote(Note note, OnUpdateNotesResponse onUpdateNotesResponse) {
        if (note.getId().equals("-1")) {
            collection.add(NoteMapping.toDocument(note)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    note.setId(documentReference.getId());
                    notes.add(note);
                    onUpdateNotesResponse.onUpdate(notes);
                }
            });
        } else {
            String id = note.getId();
            collection.document(id).set(NoteMapping.toDocument(note));
            notes.set(getNoteIndexById(id), note);
            onUpdateNotesResponse.onUpdate(notes);
        }
    }

    private int getNoteIndexById(String id) {
        for (int i = 0; i < notes.size(); i++) {
            Note n = notes.get(i);
            if (n.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ArrayList<Note> filterByName(String text) {
        if (text == null)
            return notes;
        ArrayList<Note> list = new ArrayList<>();
        for (Note n : notes) {
            if (n.getName() != null && n.getName().toUpperCase().contains(text.toUpperCase())) {
                list.add(n);
            }
        }
        return list;
    }

    public ArrayList<Note> sortByName() {
        Collections.sort(notes, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return notes;
    }

    public ArrayList<Note> sortByDate() {
        Collections.sort(notes, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return notes;
    }


    @Override
    public void delete(Note note, OnUpdateNotesResponse onUpdateNotesResponse) {
        collection.document(note.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                notes.remove(note);
                onUpdateNotesResponse.onUpdate(notes);
            }
        });
    }

    @Override
    public IRepository init(OnUpdateNotesResponse onUpdateNotesResponse) {
        collection.orderBy(NoteMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> doc = document.getData();
                            String id = document.getId();
                            Note cardData = NoteMapping.toNote(id, doc);
                            notes.add(cardData);
                        }
                        Log.d(TAG, "success " + notes.size() + " qnt");
                        onUpdateNotesResponse.onUpdate(notes);
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "get failed with ", e));
        return this;
    }
}
