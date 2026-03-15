package com.example.cleardayapplication.ui.activitys;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ActivityOnBoardingBinding;
import com.example.cleardayapplication.ui.adapters.OnBoardAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ActivityOnBoardingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("clear_day_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();

        OnBoardAdapter adapter = new OnBoardAdapter(this);
        binding.onBoardViewPager.setAdapter(adapter);
        binding.onBoardTabIndicator.setViewPager2(binding.onBoardViewPager);
        binding.onBoardViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 2) {
                    binding.onBoardAddYourAccount.setVisibility(VISIBLE);
                }else{
                    binding.onBoardAddYourAccount.setVisibility(INVISIBLE);
                }
                binding.onBoardAddYourAccount.setOnClickListener(v->{
                    startActivity(new Intent(OnBoardingActivity.this, AccountingActivity.class));
                    finish();
                });

            }
        });
    }
}