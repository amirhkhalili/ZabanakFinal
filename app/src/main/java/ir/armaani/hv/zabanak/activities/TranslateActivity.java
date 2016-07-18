package ir.armaani.hv.zabanak.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.activities.adapters.TranslateTabAdapter;

public class TranslateActivity extends ActionBarActivity {
    ImageView okWord_btn,nkWord_btn;
    TextView word,wordTranslate;
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

        okWord_btn = (ImageView)findViewById(R.id.okWord);
        nkWord_btn = (ImageView)findViewById(R.id.nkWord);
        word = (TextView)findViewById(R.id.word_txt);
        wordTranslate = (TextView)findViewById(R.id.wordTranslate_txt);

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


