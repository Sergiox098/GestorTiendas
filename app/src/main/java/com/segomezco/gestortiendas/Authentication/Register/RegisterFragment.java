package com.segomezco.gestortiendas.Authentication.Register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.segomezco.gestortiendas.databinding.FragmentRegisterBinding;
import com.segomezco.gestortiendas.R;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //NAVIGATION

        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController(RegisterFragment.this)
                .navigate(R.id.action_registerFragment_to_onboardingFragment));

        binding.btnRegisterToLogin.setOnClickListener(v -> NavHostFragment.findNavController(RegisterFragment.this)
                .navigate(R.id.action_registerFragment_to_loginFragment));

        //AUTHENTICATION

        RegisterAuthVM registerAuthVM = new ViewModelProvider(this).get(RegisterAuthVM.class);

        registerAuthVM.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                binding.btnRegister.setEnabled(!isLoading);
            }
        });

        registerAuthVM.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
            }
        });

        registerAuthVM.getRegisterSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                Log.d("RegisterFragment", "Registro exitoso");
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_registerFragment_to_homeFragment);
            }
        });

        binding.btnRegister.setOnClickListener(v -> {

            binding.tilNombre.setError(null);
            binding.tilEmail.setError(null);
            binding.tilPhone.setError(null);
            binding.tilDocument.setError(null);
            binding.tilPassword.setError(null);
            binding.tilConfirmPassword.setError(null);

            boolean hasError = false;

            String userName = Objects.requireNonNull(binding.etName.getText()).toString();
            String email = Objects.requireNonNull(binding.etEmail.getText()).toString();
            String phone = Objects.requireNonNull(binding.etPhone.getText()).toString();
            String document = Objects.requireNonNull(binding.etDocument.getText()).toString();
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString();
            String confirmPassword = Objects.requireNonNull(binding.etConfirmPassword.getText()).toString();

            if (userName.isEmpty()) {
                binding.tilNombre.setError("El nombre es obligatorio");
                hasError = true;
            }
            if (!registerAuthVM.isValidEmail(email)) {
                binding.tilEmail.setError("Correo electrónico inválido");
                hasError = true;
            }
            if (phone.isEmpty()) {
                binding.tilPhone.setError("El teléfono es obligatorio");
                hasError = true;
            }
            if (document.isEmpty()) {
                binding.tilDocument.setError("El documento es obligatorio");
                hasError = true;
            }
            if (!registerAuthVM.isValidPassword(password)) {
                binding.tilPassword.setError("La contraseña debe tener al menos 8 caracteres");
                hasError = true;
            }
            if (password.isEmpty() ||!password.equals(confirmPassword)) {
                binding.tilConfirmPassword.setError("Las contraseñas no coinciden");
                hasError = true;
            }
            if (hasError) {return;}

            registerAuthVM.register(userName, email, phone, document, password, confirmPassword);
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}