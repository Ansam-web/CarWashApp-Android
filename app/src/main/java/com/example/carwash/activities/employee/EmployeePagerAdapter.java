package com.example.carwash.activities.employee;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.carwash.activities.employee.EmployeeBookingsFragment;
import com.example.carwash.activities.employee.EmployeeTeamFragment;

public class EmployeePagerAdapter extends FragmentStateAdapter {

    public EmployeePagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new EmployeeBookingsFragment();
        return new EmployeeTeamFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}