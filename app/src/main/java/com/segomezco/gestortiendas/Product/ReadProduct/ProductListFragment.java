package com.segomezco.gestortiendas.Product.ReadProduct;

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

        // Cargar datos desde Firebase
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        readProduct.readDatabase(uid);

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
