package com.lessons.notes.note.edit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.lessons.notes.R;
import com.lessons.notes.note.NoteViewModel;
import com.lessons.notes.note.domain.Note;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NoteEditFragment extends DialogFragment {
    public static final String ARG_NOTE = "ARG_NOTE";
    private Note note;
    private TextView dateTv;
    private TextInputEditText tveName;
    private TextInputEditText tveText;
    private Date notesDate;
    DateFormat dateFormat;
    private NoteViewModel viewModel;

    public static NoteEditFragment newInstance(Note param1) {
        NoteEditFragment fragment = new NoteEditFragment();
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
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
    }

    private void init(View view) {
        view.findViewById(R.id.save_note_btn).setVisibility(note == null ? View.GONE : View.VISIBLE);
        dateFormat = android.text.format.DateFormat.getDateFormat(view.getContext());
        tveName = ((TextInputEditText) view.findViewById(R.id.note_name));
        tveText = ((TextInputEditText) view.findViewById(R.id.note_text));
        dateTv = view.findViewById(R.id.note_date);
        notesDate = note.getDate() == null ? new Date() : note.getDate();
        setData();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(notesDate);
        dateTv.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), changeDateNotesListeners(),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        });
        view.findViewById(R.id.save_note_btn).setOnClickListener(v -> saveNote());
        view.findViewById(R.id.cancel_edit_btn).setOnClickListener(v -> dismiss());
    }

    private void setData() {
        if (!note.getId().equals("-1")) {
            tveName.setText(note.getName());
            tveText.setText(note.getText());
        }
        dateTv.setText(dateFormat.format(notesDate));
    }

    private DatePickerDialog.OnDateSetListener changeDateNotesListeners() {
        return (view, year, monthOfYear, dayOfMonth) -> {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            notesDate = c.getTime();
            dateTv.setText(dateFormat.format(notesDate));
        };
    }

    private void saveNote() {
        if (tveName.getText() == null || "".equals(tveName.getText().toString())) {
            Toast.makeText(requireContext(), getView().getResources().getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
            return;
        }
        Note newN = new Note(note.getId(), tveName.getText().toString(), tveText.getText().toString(), new Date(notesDate.getTime()));
        viewModel.saveNote(newN);
        dismiss();
    }

    @Override
    public int getTheme() {
        return R.style.AlertDialogStyle;
    }
}