package com.example.demoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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


public class LoginFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonEnter;
    private TextView textViewRegistration;
    private LinearLayout linearLayout;
    private OnUserLoginListener onUserLoginListener;
    private ChangeToRegisterListener changeToRegisterListener;
    private ProgressBar spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        linearLayout = rootView.findViewById(R.id.linearLayout);
        editTextEmail = rootView.findViewById(R.id.editTextEmail);
        editTextPassword = rootView.findViewById(R.id.editTextPassword);
        textViewRegistration = rootView.findViewById(R.id.textViewRegistration);
        buttonEnter = rootView.findViewById(R.id.buttonEnter);
        spinner = rootView.findViewById(R.id.spiner);
        textViewRegistration.setOnClickListener(this);
        buttonEnter.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonEnter:
                if (checkField() == true) {
                    spinner.setVisibility(View.VISIBLE);
                    signIn(String.valueOf(editTextEmail.getText()), String.valueOf(editTextPassword.getText()));
                }
                break;
            case R.id.textViewRegistration:
                changeToRegisterListener.changeToRegister();
                break;
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    onUserLoginListener.login(user.getUid());
                } else {
                    spinner.setVisibility(View.GONE);
                    Animation sunRiseAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    linearLayout.startAnimation(sunRiseAnimation);
                    Snackbar.make(getView(), "Ошибка: некорректный логин или пароль", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnUserLoginListener) {
            onUserLoginListener = (OnUserLoginListener) context;
        }
        if (context instanceof ChangeToRegisterListener) {
            changeToRegisterListener = (ChangeToRegisterListener) context;
        }
    }

    public boolean checkField() {
        boolean boolInf = false;
        if ((editTextEmail.getText().length() != 0)) {
            if ((editTextPassword.getText().length() != 0)) {
                boolInf = true;
            } else {
                Snackbar.make(getView(), "Ошибка: заполните поле \"Пароль\"", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(getView(), "Ошибка: заполните поле \"Email\"", Snackbar.LENGTH_LONG).show();
        }
        return boolInf;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            onUserLoginListener.login(currentUser.getUid());
        }
    }

    public interface OnUserLoginListener {
        public void login(String uId);
    }

    public interface ChangeToRegisterListener {
        public void changeToRegister();
    }
}
