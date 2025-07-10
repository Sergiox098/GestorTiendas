package com.segomezco.gestortiendas.Product.ReadProduct;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;

import com.segomezco.gestortiendas.databinding.FragmentProductDetailBinding;
import com.segomezco.gestortiendas.R;

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

        // Obtener el Bundle con los datos del producto
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
                        .into(binding.ivProductImage);
            } else {
                binding.ivProductImage.setImageResource(R.drawable.ic_warning);
            }

        }
        binding.btnBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProductDetailFragment.this)
                    .navigate(R.id.action_productDetailFragment_to_productListFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


