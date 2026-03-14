package com.example.cleardayapplication.ui.activitys;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
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

        OnBoardAdapter adapter = new OnBoardAdapter(this);
        binding.onBoardViewPager.setAdapter(adapter);
        binding.onBoardTabIndicator.setViewPager2(binding.onBoardViewPager);
        binding.onBoardViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 2) {
                    binding.onBoardSingInYourAccount.setText(R.string.on_board_sing_in_you_account);
                    binding.onBoardSingInYourAccount.setVisibility(VISIBLE);
                }else if(position == 0 ){
                    binding.onBoardSingInYourAccount.setText(R.string.skip_message);
                    binding.onBoardSingInYourAccount.setVisibility(VISIBLE);
                }else{
                    binding.onBoardSingInYourAccount.setVisibility(INVISIBLE);
                }
                binding.onBoardSingInYourAccount.setOnClickListener(v->{
                    if(position == 2) {
                        startActivity(new Intent(OnBoardingActivity.this, AccountingActivity.class));
                        finish();
                    }
                    else
                        binding.onBoardViewPager.setCurrentItem(2, true);

                });

            }
        });
    }
}