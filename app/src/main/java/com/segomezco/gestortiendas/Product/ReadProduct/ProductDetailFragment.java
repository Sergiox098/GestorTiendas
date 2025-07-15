package com.segomezco.gestortiendas.Product.ReadProduct;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import com.google.firebase.auth.FirebaseAuth;

import com.segomezco.gestortiendas.databinding.FragmentProductDetailBinding;
import com.segomezco.gestortiendas.R;

import java.util.Objects;

public class ProductDetailFragment extends Fragment {
    private FragmentProductDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("store_prefs", Context.MODE_PRIVATE);
        String selectedStore = prefs.getString("selected_store", null);
        ProductDeleteVM productDeleteVM = new ViewModelProvider(this).get(ProductDeleteVM.class);

        if (getArguments() != null) {
            Bundle args = getArguments();
            binding.textName.setText(args.getString("name", ""));
            binding.textRef.setText(args.getString("ref", ""));
            binding.textDescription.setText(args.getString("description", ""));
            binding.textPrice.setText("$" + args.getString("price", ""));
            binding.textCategory.setText(args.getString("category", ""));
            binding.textBrand.setText(args.getString("brand", ""));
            binding.textStock.setText(args.getString("stock", ""));
            binding.textWeight.setText(args.getString("weight", ""));
            binding.textSupplier.setText(args.getString("supplier", ""));
            binding.textContact.setText(args.getString("contact", ""));

            String imageUrl = args.getString("imageUrl", "");
            if (!imageUrl.isEmpty()) {
                Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_warning)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                        .into(binding.ivProductImage);
            } else {
                binding.ivProductImage.setImageResource(R.drawable.ic_shopping_bag);
            }

        }

        binding.btnBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProductDetailFragment.this)
                    .navigate(R.id.action_productDetailFragment_to_productListFragment);
        });

        binding.btnEditProduct.setOnClickListener(v -> {
            if (getArguments() != null) {
                Bundle args = getArguments();
                NavHostFragment.findNavController(ProductDetailFragment.this)
                        .navigate(R.id.action_productDetailFragment_to_editProductFragment, args);
            }
        });

        productDeleteVM.getSuccess().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                NavHostFragment.findNavController(ProductDetailFragment.this)
                        .navigate(R.id.action_productDetailFragment_to_productListFragment);
            }
        });

        binding.btnDeleteProduct.setOnClickListener( v -> {

            String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            String productName = binding.textName.getText().toString();

                new AlertDialog.Builder(requireContext())
                        .setTitle("Eliminar producto")
                        .setMessage("¿Estás seguro de que quieres eliminar este producto?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            productDeleteVM.deleteProduct(getContext(),userID, selectedStore, productName);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


