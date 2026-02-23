package com.example.cleardayapplication.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cleardayapplication.ui.fragments.onboard.FirstOnBoardFragment;
import com.example.cleardayapplication.ui.fragments.onboard.SecondOnBoardFragment;
import com.example.cleardayapplication.ui.fragments.onboard.ThirdOnBoardFragment;

public class OnBoardAdapter extends FragmentStateAdapter {

    public OnBoardAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FirstOnBoardFragment();
            case 1: return new SecondOnBoardFragment();
            case 2: return new ThirdOnBoardFragment();
            default: return new FirstOnBoardFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
