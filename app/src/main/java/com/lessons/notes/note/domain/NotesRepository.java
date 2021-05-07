package com.lessons.notes.note.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NotesRepository {
    private static ArrayList<Note> notes;

    static {
        init();
    }

    public List<Note> getNotes() {
        return notes;
    }

    public boolean updateNote(Note note) {
        Note n = getNoteById(note.getId());
        if (n == null) {
            synchronized (this) {
                long nextId = notes.size();
                Note newNote = new Note(nextId, note.getName(), note.getText(), note.getDate());
                notes.add(newNote);
            }
            return true;
        } else {
            notes.set(notes.indexOf(n), note);
            return true;
        }
    }

    public Note getNoteById(long id) {
        for (Note n : notes) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    private static void init() {
        notes = new ArrayList<>();
        notes.add(new Note(1, "Заметка1 длинный заголовок на несколько строк", "Текст1", new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)));
        notes.add(new Note(2, "Заметка2", "Текст2", new Date(System.currentTimeMillis() - 2000 * 60 * 60 * 24)));
        notes.add(new Note(3, "Заметка3", "Текст3", new Date(System.currentTimeMillis() - 3000 * 60 * 60 * 24)));
        notes.add(new Note(4, "Заметка4", "Текст4", new Date(System.currentTimeMillis() - 4000 * 60 * 60 * 24)));
        notes.add(new Note(5, "Отрывок \"Крестьянские дети\" ", "Однажды, в студеную зимнюю пору\n" +
                "Я из лесу вышел; был сильный мороз.\n" +
                "Гляжу, поднимается медленно в гору\n" +
                "Лошадка, везущая хворосту воз.\n" +
                "И шествуя важно, в спокойствии чинном,\n" +
                "Лошадку ведет под уздцы мужичок\n" +
                "В больших сапогах, в полушубке овчинном,\n" +
                "В больших рукавицах… а сам с ноготок!\n" +
                "«Здорово парнище!» — «Ступай себе мимо!»\n" +
                "— «Уж больно ты грозен, как я погляжу!\n" +
                "Откуда дровишки?» — «Из лесу, вестимо;\n" +
                "Отец, слышишь, рубит, а я отвожу».\n" +
                "(В лесу раздавался топор дровосека.)\n" +
                "«А что, у отца-то большая семья?»\n" +
                "— «Семья-то большая, да два человека\n" +
                "Всего мужиков-то: отец мой да я…»\n" +
                "— «Так вот оно что! А как звать тебя?» — «Власом».\n" +
                "— «А кой-тебе годик?» — «Шестой миновал…\n" +
                "Ну, мертвая!» — крикнул малюточка басом,\n" +
                "Рванул под уздцы и быстрей зашагал.Год написания: без даты", new Date(System.currentTimeMillis() - 5000 * 60 * 60 * 24)));
        notes.add(new Note(6, "Заметка6", "Текст6", new Date(System.currentTimeMillis() - 6000 * 60 * 60 * 24)));
        notes.add(new Note(7, "Заметка7", "Текст7", new Date(System.currentTimeMillis() - 7000 * 60 * 60 * 24)));
    }

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

    public int delete(Note note) {
        int ind = notes.indexOf(note);
        if (ind != -1) {
            notes.remove(note);
            return ind;
        }
        return -1;
    }

    public Note getNearNote(int ind) {
        if (ind > 0 && ind - 1 < notes.size()) {
            return notes.get(ind - 1);
        } else if (ind + 1 < notes.size())
            return notes.get(ind + 1);
        else if (ind < notes.size())
            return notes.get(ind);
        return null;
    }
}
