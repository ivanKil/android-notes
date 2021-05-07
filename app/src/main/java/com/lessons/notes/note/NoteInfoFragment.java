package com.lessons.notes.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lessons.notes.R;
import com.lessons.notes.note.domain.Note;

import java.text.DateFormat;

public class NoteInfoFragment extends Fragment {
    public static final String ARG_NOTE = "ARG_NOTE";
    private Note note;
    DateFormat dateFormat;
    private NoteViewModel viewModel;
    private TextView tvName;
    private TextView tvText;
    private TextView dateTv;

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
        return inflater.inflate(R.layout.fragment_note_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateFormat = android.text.format.DateFormat.getDateFormat(view.getContext());
        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(), note -> {
            this.note = note;
            setData();
        });
        init(view);
        setData();
        initTopMenu(view);
    }

    private void initTopMenu(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.send) {
                Toast.makeText(requireContext(), "Не реализовано", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_NOTE, note);
        super.onSaveInstanceState(outState);
    }

    private void init(View view) {
        tvName = ((TextView) view.findViewById(R.id.note_name));
        tvText = ((TextView) view.findViewById(R.id.note_text));
        dateTv = view.findViewById(R.id.note_date);
        if (note == null) {
            ((TextView) view.findViewById(R.id.note_name)).setText(getResources().getString(R.string.select_note));
        }
    }

    private void setData() {
        if (note != null) {
            tvName.setText(note.getName());
            tvText.setText(note.getText());
            dateTv.setText(dateFormat.format(note.getDate()));
        }
    }
}