package com.segomezco.gestortiendas.Product.CreateProduct;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import com.segomezco.gestortiendas.databinding.FragmentCreateProductBinding;
import com.segomezco.gestortiendas.R;

import java.util.Objects;

public class CreateProductFragment extends Fragment {

    private FragmentCreateProductBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProductCreateVM productCreateVM = new ViewModelProvider(this).get(ProductCreateVM.class);

        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController
                (CreateProductFragment.this).navigate(R.id.action_createProductFragment_to_homeFragment));

        productCreateVM.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                binding.btnSaveProduct.setEnabled(!isLoading);
            }
        });

        productCreateVM.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.btnSaveProduct.setEnabled(true);
            }
        });

        productCreateVM.getRegisterSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                Log.d("RegisterFragment", "Registro exitoso");
                NavHostFragment.findNavController(CreateProductFragment.this)
                        .navigate(R.id.action_createProductFragment_to_homeFragment);
            }
        });


        binding.btnSaveProduct.setOnClickListener(v -> {

            String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            String ProductName = Objects.requireNonNull(binding.etProductName.getText()).toString().trim();
            String ProductRef = Objects.requireNonNull(binding.etProductRef.getText()).toString().trim();
            String ProductDescription = Objects.requireNonNull(binding.etProductDescription.getText()).toString().trim();
            String ProductPrice = Objects.requireNonNull(binding.etProductPrice.getText()).toString().trim();
            String ProductCategory = Objects.requireNonNull(binding.etProductCategory.getText()).toString().trim();
            String ProductBrand = Objects.requireNonNull(binding.etProductBrand.getText()).toString().trim();
            String ProductStock = Objects.requireNonNull(binding.etProductStock.getText()).toString().trim();
            String ProductWeight = Objects.requireNonNull(binding.etProductWeight.getText()).toString().trim();
            String ProductSupplier = Objects.requireNonNull(binding.etProductSupplier.getText()).toString().trim();
            String ProductContact = Objects.requireNonNull(binding.etSupplierContact.getText()).toString().trim();

            productCreateVM.CreateObject(userID, ProductName, ProductRef, ProductDescription,
                    ProductPrice, ProductCategory, ProductBrand, ProductStock, ProductWeight,
                    ProductSupplier, ProductContact);
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}