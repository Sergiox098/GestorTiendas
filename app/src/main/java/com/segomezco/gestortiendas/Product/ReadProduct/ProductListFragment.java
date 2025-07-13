package com.segomezco.gestortiendas.Product.ReadProduct;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentProductListBinding;

import java.util.Objects;

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("store_prefs", Context.MODE_PRIVATE);
        String selectedStore = prefs.getString("selected_store", null);

        ReadProductVM readProduct = new ViewModelProvider(this).get(ReadProductVM.class);

        readProduct.getProductListLiveData().observe(getViewLifecycleOwner(), products -> {
            ProductAdapter adapter = new ProductAdapter(products, product -> {
                Bundle bundle = new Bundle();
                bundle.putString("name", product.getName());
                bundle.putString("ref", product.getRef());
                bundle.putString("description", product.getDescription());
                bundle.putString("price", product.getPrice());
                bundle.putString("category", product.getCategory());
                bundle.putString("brand", product.getBrand());
                bundle.putString("stock", product.getStock());
                bundle.putString("weight", product.getWeight());
                bundle.putString("supplier", product.getSupplier());
                bundle.putString("contact", product.getContact());
                bundle.putString("imageUrl", product.getImageUrl());

                NavHostFragment.findNavController(ProductListFragment.this)
                        .navigate(R.id.action_productListFragment_to_productDetailFragment, bundle);
            });

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.recyclerView.setAdapter(adapter);
        });

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        readProduct.readDatabase(uid, selectedStore);

        binding.btnBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProductListFragment.this)
                    .navigate(R.id.action_productListFragment_to_homeFragment);
        });

        binding.btnCircular.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProductListFragment.this)
                    .navigate(R.id.action_productListFragment_to_createProductFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
