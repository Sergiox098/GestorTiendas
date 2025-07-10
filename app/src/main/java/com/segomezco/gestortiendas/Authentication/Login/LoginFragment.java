package com.segomezco.gestortiendas.Authentication.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentLoginBinding;

import java.util.Objects;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //NAVIGATION

        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_onboardingFragment));

        binding.btnLoginToRegister.setOnClickListener(v -> NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_registerFragment));

        //AUTHENTICATION

        LoginAuthVM loginAuthVM = new ViewModelProvider(this).get(LoginAuthVM.class);


        binding.btnLogin.setOnClickListener(v -> {

            binding.tilCorreoElectronico.setError(null);
            binding.tilPassword.setError(null);

            boolean hasError = false;

            String email = Objects.requireNonNull(binding.etEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString();

            if (!loginAuthVM.isValidEmail(email)) {
                binding.tilCorreoElectronico.setError("Correo electrónico inválido");
                hasError = true;
            }
            if (!loginAuthVM.isValidPassword(password)) {
                binding.tilPassword.setError("La contraseña debe tener al menos 8 caracteres");
                hasError = true;
            }
            if (hasError) {return;}

            loginAuthVM.login(email, password);
        });

        loginAuthVM.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnLogin.setEnabled(!isLoading);
        });

        loginAuthVM.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                binding.btnLogin.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        loginAuthVM.getLoginSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Login exitoso", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_homeFragment);
            }
        });

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}