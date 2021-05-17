package com.lessons.notes.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.lessons.notes.R;
import com.lessons.notes.note.edit.NoteEditFragment;

public class MainNoteFragment extends Fragment {
    private final String STACK_BEFORE_EDIT = "STACK_BEFORE_EDIT";
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getChildFragmentManager();
        if (!getResources().getBoolean(R.bool.isLandscape)) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        NoteViewModel viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(), note -> {
                    if (note != null && note.isForEdit()) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_note_info, NoteEditFragment.newInstance(note))
                                .addToBackStack(STACK_BEFORE_EDIT).commit();
                    } else {
                        showInfo();
                    }
                }
        );
        viewModel.getSavedNote().observe(getViewLifecycleOwner(), note -> {
            fragmentManager.popBackStack(STACK_BEFORE_EDIT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (getResources().getBoolean(R.bool.isLandscape)) {
                showInfo();
            }
        });
    }

    private void showInfo() {
        fragmentManager.beginTransaction()
                .replace(R.id.frame_note_info, NoteInfoFragment.newInstance(null))
                .addToBackStack(null).commit();
    }


}
