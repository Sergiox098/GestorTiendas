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
            String userName = Objects.requireNonNull(binding.etName.getText()).toString();
            String email = Objects.requireNonNull(binding.etEmail.getText()).toString();
            String phone = Objects.requireNonNull(binding.etPhone.getText()).toString();
            String document = Objects.requireNonNull(binding.etDocument.getText()).toString();
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString();
            String confirmPassword = Objects.requireNonNull(binding.etConfirmPassword.getText()).toString();
            registerAuthVM.register(userName, email, phone, document, password, confirmPassword);
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}