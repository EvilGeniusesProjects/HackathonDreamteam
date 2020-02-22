package com.example.demoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.demoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    public EditText editTextEmail;
    public EditText editTextPassword;
    public EditText editTextVerify;
    public Button buttonEnter;
    public TextView textViewRegistration;
    public LinearLayout linearLayout;
    private LoginFragment.OnUserLoginListener onUserLoginListener;
    private ChangeToLoginListener changeToLoginListener;
    private ProgressBar spinner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        linearLayout = rootView.findViewById(R.id.linearLayout);
        editTextEmail = rootView.findViewById(R.id.editTextEmail);
        editTextPassword = rootView.findViewById(R.id.editTextPassword);
        editTextVerify = rootView.findViewById(R.id.editTextVerify);
        textViewRegistration = rootView.findViewById(R.id.textViewRegistration);
        buttonEnter = rootView.findViewById(R.id.buttonEnter);
        textViewRegistration.setOnClickListener(this);
        buttonEnter.setOnClickListener(this);
        spinner = rootView.findViewById(R.id.spiner);
        mAuth = FirebaseAuth.getInstance();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonEnter:
                if (checkField()) {
                    spinner.setVisibility(View.VISIBLE);
                    createAccount(String.valueOf(editTextEmail.getText()), String.valueOf(editTextPassword.getText()));
                }
                break;
            case R.id.textViewRegistration:
                changeToLoginListener.changeToLogin();
                break;

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ChangeToLoginListener){
            changeToLoginListener = (ChangeToLoginListener) context;
        }
        if (context instanceof LoginFragment.OnUserLoginListener){
            onUserLoginListener = (LoginFragment.OnUserLoginListener) context;
        }
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            onUserLoginListener.login(user.getUid());
                        } else {
                            spinner.setVisibility(View.GONE);
                            Snackbar.make(getView(), "Ошибка: недопустимый формат полей", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                });
    }

    public boolean checkField() {
        boolean boolInf = false;
        if ((editTextEmail.getText().length() != 0)) {
            if ((editTextVerify.getText().length() != 0)) {
                if ((editTextPassword.getText().length() != 0)) {
                    boolInf = true;
                } else {
                    Snackbar.make(getView(), "Ошибка: заполните поле \"Пароль\"", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            } else {
                Snackbar.make(getView(), "Ошибка: заполниет поле \"Код подтверждения\"", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } else {
            Snackbar.make(getView(), "Ошибка: заполниет поле \"Email\"", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        return boolInf;
    }

    public interface ChangeToLoginListener{
        public void changeToLogin();
    }
}
