package com.segomezco.gestortiendas.Authentication.Splashscreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OnboardingAdapter extends FragmentStateAdapter {

    public OnboardingAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return OnBoardingSlideFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
