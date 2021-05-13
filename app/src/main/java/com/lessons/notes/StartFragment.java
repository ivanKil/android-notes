package com.lessons.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StartFragment extends Fragment {
    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";
    private GoogleSignInClient googleSignInClient;
    private MaterialButton buttonSignInGoogle;
    private MaterialButton buttonSignInVK;
    private TextView emailView;
    private MaterialButton butContinue;
    private MaterialButton butExit;

    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initGoogleSign();
        initView(view);
        enableSign();
        return view;
    }

    // Инициализация запроса на аутентификацию
    private void initGoogleSign() {
        // Конфигурация запроса на регистрацию пользователя, чтобы получить
        // идентификатор пользователя, его почту и основной профайл
        // (регулируется параметром)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Получаем клиента для регистрации и данные по клиенту
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    // Инициализация пользовательских элементов
    private void initView(View view) {
        // Кнопка регистрации пользователя
        buttonSignInGoogle = view.findViewById(R.id.sign_in_button_google);
        buttonSignInVK = view.findViewById(R.id.sign_in_button_vk);
        buttonSignInGoogle.setOnClickListener(v -> signInForGoogle());
        buttonSignInVK.setOnClickListener(v -> {
            ArrayList<VKScope> scopes = new ArrayList<>();
            scopes.add(VKScope.WALL);
            scopes.add(VKScope.PHOTOS);
            VK.login(getActivity(), scopes);
        });

        emailView = view.findViewById(R.id.email);
        butContinue = view.findViewById(R.id.continue_);
        butContinue.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_notes);
        });
        butExit = view.findViewById(R.id.exit);
        butExit.setOnClickListener(v -> {
            if (VK.isLoggedIn()) {
                VK.logout();
            } else {
                googleSignInClient.signOut().addOnCompleteListener(task -> enableSign());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null || VK.isLoggedIn()) {
            disableSign();
            updateUI(account != null ? account.getEmail() : getResources().getString(R.string.signed_vk));
        }
    }

    private void signInForGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Здесь получим ответ от системы, что пользователь вошёл
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            VKAuthCallback cb = new VKAuthCallback() {
                @Override
                public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                    disableSign();
                    updateUI(getResources().getString(R.string.signed_vk));
                }

                @Override
                public void onLoginFailed(int i) {
                    Toast.makeText(requireContext(), getView().getResources().getString(R.string.error_sign_in), Toast.LENGTH_SHORT).show();
                }
            };
            if (data == null || !VK.onActivityResult(requestCode, resultCode, data, cb)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            disableSign();
            updateUI(account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(requireContext(), getView().getResources().getString(R.string.error_sign_in), Toast.LENGTH_SHORT).show();
        }
    }

    // Обновляем данные о пользователе на экране
    private void updateUI(String email) {
        emailView.setText(email);
    }

    // Разрешить аутентификацию и запретить остальные действия
    private void enableSign() {
        buttonSignInGoogle.setEnabled(true);
        buttonSignInVK.setEnabled(true);
        butContinue.setEnabled(false);
        emailView.setText("");
    }

    // Запретить аутентификацию (уже прошла) и разрешить остальные действия
    private void disableSign() {
        buttonSignInGoogle.setEnabled(false);
        butContinue.setEnabled(true);
        buttonSignInVK.setEnabled(false);
    }
}