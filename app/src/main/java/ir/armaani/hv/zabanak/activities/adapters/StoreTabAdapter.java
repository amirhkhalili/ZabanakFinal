package ir.armaani.hv.zabanak.activities.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ir.armaani.hv.zabanak.activities.fragments.OfflineStoreFragment;
import ir.armaani.hv.zabanak.activities.fragments.OnlineStoreFragment;

/**
 * Created by Amirhossein on 15/07/2016.
 */
public class StoreTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public StoreTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OfflineStoreFragment tab1 = new OfflineStoreFragment();
                return tab1;
            case 1:
                OnlineStoreFragment tab2 = new OnlineStoreFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
