package com.segomezco.gestortiendas.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

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

        binding.cardProducts.setOnClickListener(v -> NavHostFragment.findNavController
                        (HomeFragment.this).navigate(R.id.action_homeFragment_to_readProductFragment));

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}