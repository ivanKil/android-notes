package com.lessons.notes;

import com.lessons.notes.note.domain.Note;

import java.util.ArrayList;

public class Publisher {
    private final ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notify(Note note) {
        for (Observer observer : observers) {
            observer.updateNote(note);
        }
    }
}
