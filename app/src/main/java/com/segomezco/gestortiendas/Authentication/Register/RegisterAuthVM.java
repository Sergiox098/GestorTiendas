package com.segomezco.gestortiendas.Authentication.Register;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class RegisterAuthVM extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }

    public void register(String userName, String email, String phone,
             String document, String password, String confirmPassword) {

        isLoading.setValue(true);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    isLoading.setValue(true);
                    if (task.isSuccessful()) {
                        String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        DatabaseReference userRef = firebaseDatabase.getReference("Users").child(uid);
                        RegisterUserM userData = new RegisterUserM(userName, email, phone, document);
                        userRef.setValue(userData).addOnCompleteListener(dbTask -> {
                            isLoading.setValue(false);
                            if (dbTask.isSuccessful()) {
                                registerSuccess.setValue(true);
                            } else {
                                errorMessage.setValue("Error al guardar los datos en la base de datos"+
                                        Objects.requireNonNull(dbTask.getException()).getMessage());
                            }
                        });
                    } else {
                        errorMessage.setValue("Error al registrar el usuario"+ Objects.requireNonNull(task.getException()).getMessage());
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
