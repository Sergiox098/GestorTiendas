package com.segomezco.gestortiendas.Store.CreateStore;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentStoreCreateBinding;

import java.util.Objects;

public class CreateStoreFragment extends Fragment {

    private FragmentStoreCreateBinding binding;
    private Uri selectedImageUri;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStoreCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pickMediaLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        binding.imagePreviewStore.setVisibility(View.VISIBLE);
                        binding.imagePreviewStore.setImageURI(uri);
                    } else {
                        Toast.makeText(requireContext(), "No seleccionaste ninguna imagen", Toast.LENGTH_SHORT).show();
                    }
                });


        StoreCreateVM storeCreateVM = new ViewModelProvider(this).get(StoreCreateVM.class);

        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController
                (CreateStoreFragment.this).navigate
                (R.id.action_createStoreFragment_to_storeListFragment));

        storeCreateVM.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                binding.btnSaveStore.setEnabled(!isLoading);
            }
        });

        storeCreateVM.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.btnSaveStore.setEnabled(true);
            }
        });

        storeCreateVM.getRegisterSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                Log.d("RegisterFragment", "Registro exitoso");
                NavHostFragment.findNavController(CreateStoreFragment.this)
                        .navigate(R.id.action_createStoreFragment_to_storeListFragment);
            }
        });


        binding.btnSaveStore.setOnClickListener(v -> {

            binding.fieldStoreName.setError(null);
            binding.fieldAddress.setError(null);
            binding.fieldPhone.setError(null);

            boolean hasError = false;

            String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            String StoreName = Objects.requireNonNull(binding.etStoreName.getText()).toString().trim();
            String StoreDescription = Objects.requireNonNull(binding.etStoreDescription.getText()).toString().trim();
            String StoreCategory = Objects.requireNonNull(binding.etStoreCategory.getText()).toString().trim();
            String StoreOpenHours = Objects.requireNonNull(binding.etOpenHours.getText()).toString().trim();
            String StoreAddress = Objects.requireNonNull(binding.etAddress.getText()).toString().trim();
            String StorePhone = Objects.requireNonNull(binding.etPhone.getText()).toString().trim();
            String StoreOwner = Objects.requireNonNull(binding.etOwner.getText()).toString().trim();

            if (StoreName.isEmpty()) {
                binding.fieldStoreName.setError("El nombre es obligatorio");
                hasError = true;
            } else if (StoreName.contains(".") || StoreName.contains("$") ||
                    StoreName.contains("#") || StoreName.contains("[") ||
                    StoreName.contains("]") || StoreName.contains("/")) {
                binding.fieldStoreName.setError("El nombre no puede contener . $ # [ ] /");
                hasError = true;
            }

            if (StoreAddress.isEmpty()) {
                binding.fieldAddress.setError("La referencia es obligatoria");
                hasError = true;
            }
            if (StorePhone.isEmpty()) {
                binding.fieldPhone.setError("El precio es obligatorio");
                hasError = true;
            }

            if (hasError) return;

            storeCreateVM.CreateStore(userID, StoreName, StoreDescription, StoreCategory,
                    StoreOpenHours, StoreAddress, StorePhone, StoreOwner, selectedImageUri);
        });


        binding.btnAddStoreImage.setOnClickListener(v -> {
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build();
            pickMediaLauncher.launch(request);
        });

    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}