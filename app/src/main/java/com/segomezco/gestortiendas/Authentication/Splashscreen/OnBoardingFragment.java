package com.segomezco.gestortiendas.Authentication.Splashscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.databinding.FragmentOnboardingBinding;

public class OnBoardingFragment extends Fragment {

    private FragmentOnboardingBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnboardingAdapter adapter = new OnboardingAdapter(this);
        binding.viewPagerOnboarding.setAdapter(adapter);

        binding.bttNext.setOnClickListener(v -> {
            int current = binding.viewPagerOnboarding.getCurrentItem();
            binding.viewPagerOnboarding.setCurrentItem(current + 1);
        });

        binding.bttRegister.setOnClickListener(v -> NavHostFragment.findNavController(OnBoardingFragment.this)
                .navigate(R.id.action_onboardingFragment_to_registerFragment));
        binding.bttLogin.setOnClickListener(v -> NavHostFragment.findNavController(OnBoardingFragment.this)
                .navigate(R.id.action_onboardingFragment_to_loginFragment));

        binding.viewPagerOnboarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int lastPageIndex = adapter.getItemCount() - 1;

                if (position == lastPageIndex) {
                    binding.bttNext.setVisibility(View.GONE);
                    binding.bttLogin.setVisibility(View.VISIBLE);
                    binding.bttRegister.setVisibility(View.VISIBLE);
                } else {
                    binding.bttNext.setVisibility(View.VISIBLE);
                    binding.bttLogin.setVisibility(View.GONE);
                    binding.bttRegister.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}