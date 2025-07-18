package com.segomezco.gestortiendas.Authentication.Login;

import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.segomezco.gestortiendas.databinding.FragmentLoginBinding;

import java.util.Objects;

public class LoginAuthVM extends ViewModel{

    private FragmentLoginBinding binding;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void login(String email, String password) {

        isLoading.setValue(true);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    isLoading.setValue(false);
                    if (task.isSuccessful()) {
                        loginSuccess.setValue(true);
                    } else {
                        errorMessage.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }
    public boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean isValidPassword(String password) {
        return password != null && password.trim().length() >= 8;
    }

}
