package com.segomezco.gestortiendas.Product.EditProduct;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;

import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentProductEditBinding;

import java.util.Objects;

public class EditProductFragment extends Fragment {

    private FragmentProductEditBinding binding;

    private Uri selectedImageUri;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductEditBinding.inflate(inflater, container, false);
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
                        binding.imagePreview.setVisibility(View.VISIBLE);
                        binding.imagePreview.setImageURI(uri);
                    } else {
                        Toast.makeText(requireContext(), "No seleccionaste ninguna imagen", Toast.LENGTH_SHORT).show();
                    }
                });

        ProductEditVM productEditVM = new ViewModelProvider(this).get(ProductEditVM.class);

        Bundle args = getArguments();
        if (args != null) {

            binding.etProductName.setText(args.getString("name", ""));
            binding.etProductRef.setText(args.getString("ref", ""));
            binding.etProductDescription.setText(args.getString("description", ""));
            binding.etProductPrice.setText(args.getString("price", ""));
            binding.etProductCategory.setText(args.getString("category", ""));
            binding.etProductBrand.setText(args.getString("brand", ""));
            binding.etProductStock.setText(args.getString("stock", ""));
            binding.etProductWeight.setText(args.getString("weight", ""));
            binding.etProductSupplier.setText(args.getString("supplier", ""));
            binding.etSupplierContact.setText(args.getString("contact", ""));

            String imageUrl = args.getString("imageUrl", "");
            if (!imageUrl.isEmpty()) {
                binding.imagePreview.setVisibility(View.VISIBLE);
                Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_warning)
                        .into(binding.imagePreview);
            } else {
                binding.imagePreview.setVisibility(View.GONE);
            }
        }

        binding.btnAddImage.setOnClickListener(v -> {
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build();
            pickMediaLauncher.launch(request);
        });

        productEditVM.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                binding.btnEditProduct.setEnabled(!isLoading);
            }
        });

        productEditVM.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.btnEditProduct.setEnabled(true);
            }
        });

        productEditVM.getRegisterSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Edicion exitosa", Toast.LENGTH_SHORT).show();
                Log.d("RegisterFragment", "Edicion exitosa");
                NavHostFragment.findNavController(EditProductFragment.this)
                        .navigate(R.id.action_editProductFragment_to_productListFragment);
            }
        });

        binding.btnEditProduct.setOnClickListener(v -> {

            assert args != null;
            String oldName = args.getString("name", "");

            String oldImage = args.getString("imageUrl", "");

            binding.fieldProductName.setError(null);
            binding.fieldProductRef.setError(null);
            binding.fieldProductPrice.setError(null);

            boolean hasError = false;

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

            SharedPreferences prefs = requireContext().getSharedPreferences("store_prefs", Context.MODE_PRIVATE);
            String selectedStore = prefs.getString("selected_store", null);

            if (ProductName.isEmpty()) {
                binding.fieldProductName.setError("El nombre es obligatorio");
                hasError = true;

            } else if (ProductName.contains(".") || ProductName.contains("$") ||
                    ProductName.contains("#") || ProductName.contains("[") ||
                    ProductName.contains("]") || ProductName.contains("/")) {
                binding.fieldProductName.setError("El nombre no puede contener . $ # [ ] /");
                hasError = true;
            }

            if (ProductRef.isEmpty()) {
                binding.fieldProductRef.setError("La referencia es obligatoria");
                hasError = true;
            }
            if (ProductPrice.isEmpty()) {
                binding.fieldProductPrice.setError("El precio es obligatorio");
                hasError = true;
            }
            if (ProductStock.isEmpty()) {
                binding.fieldProductStock.setError("El stock es obligatorio");
                hasError = true;
            }

            if (selectedStore.isEmpty() || selectedStore == null)  {
                Toast.makeText(getContext(), "Selecciona una tienda", Toast.LENGTH_SHORT).show();
                hasError = true;
            }


            if (hasError) return;

            productEditVM.EditObject(userID, oldName, ProductName, ProductRef, ProductDescription,
                    ProductPrice, ProductCategory, ProductBrand, ProductStock, ProductWeight,
                    ProductSupplier, ProductContact, selectedImageUri, oldImage, selectedStore);
        });

        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController
                (EditProductFragment.this).navigate
                (R.id.action_editProductFragment_to_productListFragment));
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}