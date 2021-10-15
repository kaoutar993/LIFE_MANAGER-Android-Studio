package com.khadija.makhchoun.lifemanager.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * created by pc on 24/02/2021.
 **/
public class SectionPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList=new ArrayList<>();
    private List<String> titleList=new ArrayList<>();
    public SectionPagerAdapter(@NonNull FragmentManager fm,int i) {
        super(fm,i);;
    }

    public SectionPagerAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    public CharSequence getPageTitle(int position){
        return titleList.get(position);
    }
    public void addFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        titleList.add(title);
    }
}
