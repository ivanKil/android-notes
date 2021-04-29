package com.lessons.notes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lessons.notes.domain.Note;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NoteInfoFragment extends Fragment {
    public static final String ARG_NOTE = "ARG_NOTE";
    public static final String EDITED_NOTE = "EDITED_NOTE";
    private Note note;
    private Date notesDate;
    private Publisher publisher;
    public static final int NOTE_CHANGED = 2;
    DateFormat dateFormat;

    public static NoteInfoFragment newInstance(Note param1) {
        NoteInfoFragment fragment = new NoteInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_info, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        view.findViewById(R.id.save_note_btn).setVisibility(note == null ? View.GONE : View.VISIBLE);
        if (note != null) {
            dateFormat = android.text.format.DateFormat.getDateFormat(view.getContext());
            ((TextView) view.findViewById(R.id.note_name)).setText(note.getName());
            ((TextView) view.findViewById(R.id.note_text)).setText(note.getText());
            TextView dateTv = view.findViewById(R.id.note_date);
            notesDate = note.getDate();
            dateTv.setText(dateFormat.format(notesDate));

            DatePicker datePicker = view.findViewById(R.id.datePicker);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(notesDate);
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), changeDateNotesListeners(dateTv, datePicker));
            dateTv.setOnClickListener(v -> datePicker.setVisibility(View.VISIBLE));
            view.findViewById(R.id.save_note_btn).setOnClickListener(v -> saveNote());
        } else {
            ((TextView) view.findViewById(R.id.note_name)).setText(getResources().getString(R.string.select_note));
        }
    }


    private DatePicker.OnDateChangedListener changeDateNotesListeners(TextView dateTv, DatePicker datePicker) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            notesDate = c.getTime();
            dateTv.setText(dateFormat.format(notesDate));
            datePicker.setVisibility(View.GONE);
        };
    }

    private void saveNote() {
        note.setDate(new Date(notesDate.getTime()));
        if (publisher != null) {
            publisher.notify(note);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent();
            intent.putExtra(EDITED_NOTE, note);
            getActivity().setResult(NOTE_CHANGED, intent);
            getActivity().finish();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PublisherHolder) {
            publisher = ((PublisherHolder) context).getPublisher();
        }
    }
}