package com.segomezco.gestortiendas.Authentication.Splashscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentOnboardingSlideBinding;

public class OnBoardingSlideFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private FragmentOnboardingSlideBinding binding;

    public static OnBoardingSlideFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);

        OnBoardingSlideFragment fragment = new OnBoardingSlideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingSlideBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        int pos = getArguments().getInt(ARG_POSITION, 0);

        switch (pos) {
            case 0:
                binding.tituloOnboarding.setText(R.string.onboarding_title_0);
                binding.descripcionOnboarding.setText(R.string.onboarding_desc_0);
                binding.imagenOnboarding.setImageResource(R.drawable.slide_1);
                break;
            case 1:
                binding.tituloOnboarding.setText(R.string.onboarding_title_1);
                binding.descripcionOnboarding.setText(R.string.onboarding_desc_1);
                binding.imagenOnboarding.setImageResource(R.drawable.slide_2);
                break;
            case 2:
                binding.tituloOnboarding.setText(R.string.onboarding_title_2);
                binding.descripcionOnboarding.setText(R.string.onboarding_desc_2);
                binding.imagenOnboarding.setImageResource(R.drawable.slide_3);
                break;
            case 3:
                binding.tituloOnboarding.setText(R.string.onboarding_title_3);
                binding.descripcionOnboarding.setText(R.string.onboarding_desc_3);
                binding.imagenOnboarding.setImageResource(R.drawable.slide_4);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
