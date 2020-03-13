package com.example.bolasepak;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> listTitle = new ArrayList<>();
    private ArrayList<Fragment> listFragment = new ArrayList<>();
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        listTitle.add("Sekarang");
        listTitle.add("Sebelum");
        listFragment.add(new ListSekarang());
        listFragment.add(new ListSebelum());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }

}
