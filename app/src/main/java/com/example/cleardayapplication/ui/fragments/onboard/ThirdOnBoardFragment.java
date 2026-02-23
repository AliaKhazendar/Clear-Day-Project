package com.example.cleardayapplication.ui.fragments.onboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cleardayapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdOnBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdOnBoardFragment extends Fragment {

    public ThirdOnBoardFragment() {
        // Required empty public constructor
    }
    public static ThirdOnBoardFragment newInstance() {
        return new ThirdOnBoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third_on_board, container, false);

    }
}