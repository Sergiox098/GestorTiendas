package com.segomezco.gestortiendas.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavOptions;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import com.google.firebase.auth.FirebaseAuth;

import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String currentUrlImage = null;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("store_prefs", Context.MODE_PRIVATE);
        String selectedStore = prefs.getString("selected_store", null);
        String uid = FirebaseAuth.getInstance().getUid();

        HomeVM homeVM = new ViewModelProvider(this).get(HomeVM.class);

        homeVM.readDatabaseName(uid);

        homeVM.getUserNameLiveData().observe(getViewLifecycleOwner(), userName -> {
            binding.tvUserName.setText(userName);
        });

        if (selectedStore != null && !selectedStore.isEmpty()) {
            homeVM.readDatabaseImage(uid, selectedStore);
            binding.scrollMenuHome.setVisibility(View.VISIBLE);
            binding.tvStoreName.setText(selectedStore);
            binding.cardStoreNotFound.setVisibility(View.GONE);

            homeVM.geturlImageLiveData().observe(getViewLifecycleOwner(), urlImage -> {
                if (urlImage != null && !urlImage.equals(currentUrlImage)) {
                    Glide.with(requireContext())
                            .load(urlImage)
                            .placeholder(R.drawable.ic_loading)
                            .error(R.drawable.ic_store)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                            .into(binding.ivStoreLogo);
                    binding.ivStoreLogo.setBackground(null);
                    currentUrlImage = urlImage;
                }
            });


        } else {
            binding.scrollMenuHome.setVisibility(View.GONE);
            binding.cardStoreNotFound.setVisibility(View.VISIBLE);
            binding.ivStoreLogo.setPadding(16,16,16,16);
        }
        binding.btnSelectStore.setOnClickListener(v -> NavHostFragment.findNavController
                (HomeFragment.this).navigate(R.id.action_homeFragment_to_storeListFragment));

        binding.cardProducts.setOnClickListener(v -> NavHostFragment.findNavController
                (HomeFragment.this).navigate(R.id.action_homeFragment_to_readProductFragment));

        binding.cardStoreHeader.setOnClickListener(v -> NavHostFragment.findNavController
                (HomeFragment.this).navigate(R.id.action_homeFragment_to_storeListFragment));

        binding.btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, true)
                    .build();

            prefs.edit().clear().apply();

            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_onBoardingFragment, null, navOptions);
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}