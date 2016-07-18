package ir.armaani.hv.zabanak.activities.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ir.armaani.hv.zabanak.activities.fragments.TranslateFragment;

/**
 * Created by Amirhossein on 17/07/2016.
 */
public class TranslateTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TranslateTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TranslateFragment tab3 = new TranslateFragment();
                return tab3;
            case 1:
                TranslateFragment tab4 = new TranslateFragment();
                return tab4;
            case 2:
                TranslateFragment tab5 = new TranslateFragment();
                return tab5;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

