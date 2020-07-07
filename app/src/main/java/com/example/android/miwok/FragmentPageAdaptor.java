package com.example.android.miwok;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentPageAdaptor extends FragmentPagerAdapter {
    public FragmentPageAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new NumbersFragment();
        if(position == 1)
            return new FamilyFragment();
        if(position == 2)
            return new ColorsFragment();
        if(position == 3)
            return new PhrasesFragment();
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Numbers";
            case 1: return "Family";
            case 2: return "Colors";
            case 3: return "Phrases";
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
