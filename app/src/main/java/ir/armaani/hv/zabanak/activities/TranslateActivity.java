package ir.armaani.hv.zabanak.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import ir.armaani.hv.zabanak.R;

public class TranslateActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        TabLayout t = (TabLayout) findViewById(R.id.tab_layout_translate2);
        t.addTab(t.newTab().setText("ترجمه فارسی"));
        t.addTab(t.newTab().setText("ترجمه انگلیسی"));
        t.addTab(t.newTab().setText("مثال ها"));
        t.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_translate2);
        final TranslateTabAdapter adapter = new TranslateTabAdapter(getSupportFragmentManager(), t.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(t));
        t.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}


