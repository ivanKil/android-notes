package com.lessons.notes.note.domain;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteMapping {
    public static class Fields {
        public final static String ID = "id";
        public final static String NAME = "name";
        public final static String TEXT = "text";
        public final static String DATE = "date";

    }

    public static Note toNote(String id, Map<String, Object> doc) {
        Timestamp timeStamp = (Timestamp) doc.get(Fields.DATE);
        return new Note(id,
                (String) doc.get(Fields.NAME),
                (String) doc.get(Fields.TEXT),
                timeStamp.toDate());
    }

    public static Map<String, Object> toDocument(Note cardData) {
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.NAME, cardData.getName());
        answer.put(Fields.TEXT, cardData.getText());
        answer.put(Fields.DATE, cardData.getDate());
        return answer;
    }
}
