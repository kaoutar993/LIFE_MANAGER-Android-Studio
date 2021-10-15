package com.khadija.makhchoun.lifemanager;

/**
 * created by pc on 03/03/2021.
 **/
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.khadija.makhchoun.lifemanager.Fragment.DashBoardFragment;
import com.khadija.makhchoun.lifemanager.Fragment.ExpenseFragment;
import com.khadija.makhchoun.lifemanager.Fragment.IncomeFragment;

public class PageAdapter extends FragmentPagerAdapter
{
    int tabcount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return new ExpenseFragment();
            case 1 : return new DashBoardFragment();
            case 2 : return new IncomeFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}

