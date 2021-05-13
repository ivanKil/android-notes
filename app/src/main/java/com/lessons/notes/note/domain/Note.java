package com.lessons.notes.note.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Objects;

public class Note implements Parcelable {
    private String id;
    private String name;
    private String text;
    private Date date;
    private transient boolean forEdit = false;

    public boolean isForEdit() {
        return forEdit;
    }

    public Note setForEdit(boolean forEdit) {
        this.forEdit = forEdit;
        return this;
    }

    public Note() {
        id = "-1";
    }

    public Note(String id, String name, String text, Date date) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected Note(Parcel in) {
        id = in.readString();
        name = in.readString();
        text = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(text);
        dest.writeLong(date.getTime());
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                Objects.equals(name, note.name) &&
                Objects.equals(text, note.text) &&
                Objects.equals(date, note.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text, date);
    }
}