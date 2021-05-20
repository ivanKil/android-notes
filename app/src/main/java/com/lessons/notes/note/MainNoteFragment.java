package com.lessons.notes.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lessons.notes.AppRouter;
import com.lessons.notes.AppRouterHolder;
import com.lessons.notes.R;

public class MainNoteFragment extends Fragment {
    AppRouter appRouter;

    public static MainNoteFragment newInstance() {
        return new MainNoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof AppRouterHolder) {
            appRouter = ((AppRouterHolder) getActivity()).getRouter();

            NoteViewModel viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
            viewModel.getSelected().observe(getViewLifecycleOwner(), appRouter::showNoteInfo);
            viewModel.getSavedNote().observe(getViewLifecycleOwner(), appRouter::showNoteEdit);

            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    appRouter.showNotes();
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }
    }

    @Override
    public void onDetach() {
        appRouter = null;
        super.onDetach();
    }
}
