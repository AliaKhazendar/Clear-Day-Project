package com.example.cleardayapplication.ui.fragments.onboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentFirstOnBoardBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstOnBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstOnBoardFragment extends Fragment {

    public FirstOnBoardFragment() {
        // Required empty public constructor
    }

    public static FirstOnBoardFragment newInstance() {
        return new FirstOnBoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return FragmentFirstOnBoardBinding
                .inflate(inflater,container,false)
                .getRoot();
    }
}