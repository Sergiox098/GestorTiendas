package com.segomezco.gestortiendas.Store.ReadStore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.firebase.auth.FirebaseAuth;
import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentStoreListBinding;

import java.util.Objects;

public class StoreListFragment extends Fragment {

    private FragmentStoreListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStoreListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StoreReadVM readStore = new ViewModelProvider(this).get(StoreReadVM.class);

        readStore.getProductListLiveData().observe(getViewLifecycleOwner(), stores -> {
            StoreAdapter adapter = new StoreAdapter(requireContext(),stores, store -> {
                SharedPreferences prefs = requireContext().getSharedPreferences("store_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("selected_store", store.getName());
                editor.apply();
            });

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.recyclerView.setAdapter(adapter);
        });

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        readStore.readDatabase(uid);

        binding.btnBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(StoreListFragment.this)
                    .navigate(R.id.action_storeListFragment_to_homeFragment);
        });

        binding.btnAddTiendaCircular.setOnClickListener(v -> {
            NavHostFragment.findNavController(StoreListFragment.this)
                    .navigate(R.id.action_storeListFragment_to_storeCreateFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
